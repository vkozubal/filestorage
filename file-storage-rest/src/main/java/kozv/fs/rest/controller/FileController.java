package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.rest.IFilesClient;
import kozv.fs.service.api.IFileStorageService;
import kozv.fs.service.exception.FSServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@Controller
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class FileController implements IFilesClient {
    private static final String ATTACHMENT_FILENAME = "attachment; filename=\"%s\"";

    private final IFileStorageService fileStorageService;

    @Override
    public Resources<FileAttributes> findAll() {
        final List<FileAttributes> all = fileStorageService.findAll();
        all.forEach(this::addHateoasLinks);
        return new Resources<>(all);
    }

    @Override
    public FileAttributes findOne(@PathVariable String fileId) {
        final FileAttributes fileAttrs = fileStorageService.findOne(fileId).getFileAttrs();
        addHateoasLinks(fileAttrs);
        return fileAttrs;
    }

    @Override
    public ResponseEntity<Resource> serveFile(@PathVariable String fileId) {
        DataFile file = fileStorageService.findOne(fileId);
        final InputStreamResource inputStreamResource = new InputStreamResource(file.getDataStream());
        return ResponseEntity
                .ok()
                .header(CONTENT_DISPOSITION, getAttachmentHeader(file.getFileAttrs()))
                .body(inputStreamResource);
    }

    @Override
    public FileAttributes saveFile(@RequestParam("file") MultipartFile file) {
        FileAttributes fileAttrs = new FileAttributes();
        fileAttrs.setContentType(file.getContentType());
        fileAttrs.setFileName(file.getOriginalFilename());

        final FileAttributes SavedFileAttributes = fileStorageService
                .save(createDataFile(file, fileAttrs));

        addHateoasLinks(SavedFileAttributes);
        return SavedFileAttributes;
    }

    @Override
    public void deleteFile(@PathVariable String fileId) {
        fileStorageService.deleteFile(fileId);
    }

    @ExceptionHandler(FSServiceException.class)
    public ResponseEntity handleNotFound(FSServiceException e) {
        return ResponseEntity.notFound().build();
    }

    private void addHateoasLinks(FileAttributes fileAttributes) {
        final String fileId = fileAttributes.getFileId();
        fileAttributes.add(linkTo(methodOn(getClass()).findOne(fileId)).withSelfRel());
        fileAttributes.add(linkTo(methodOn(getClass()).serveFile(fileAttributes.getFileId())).withRel("download"));
        fileAttributes.add(linkTo(methodOn(CommentsController.class).getComments(fileId)).withRel("comments"));
    }

    private DataFile createDataFile(MultipartFile file, FileAttributes fileAttrs) {
        try {
            DataFile dataFile = new DataFile();
            dataFile.setDataStream(file.getInputStream());
            dataFile.setFileAttrs(fileAttrs);
            return dataFile;
        } catch (IOException e) {
            throw new TemporaryStorageFailedException("Access errors. Permanent storage failed.");
        }
    }

    private static String getAttachmentHeader(FileAttributes fileAttrs) {
        return String.format(ATTACHMENT_FILENAME, fileAttrs.getFileName());
    }
}
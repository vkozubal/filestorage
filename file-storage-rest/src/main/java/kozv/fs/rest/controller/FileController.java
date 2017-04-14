package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.service.IFileStorageService;
import kozv.fs.api.service.exception.FSServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@Controller
@RequestMapping("/api/files")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class FileController {
    private static final String ATTACHMENT_FILENAME = "attachment; filename=\"%s\"";

    private final IFileStorageService fileStorageService;

    @GetMapping
    @ResponseBody
    public Collection<FileAttributes> findAll() {
        return fileStorageService.findAll();
    }

    @GetMapping("/{fileId}/attributes")
    @ResponseBody
    public FileAttributes findOne(@PathVariable String fileId) {
        final FileAttributes fileAttrs = fileStorageService.findOne(fileId).getFileAttrs();
        addHateoasLinks(fileAttrs);
        return fileAttrs;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String fileId) {
        DataFile file = fileStorageService.findOne(fileId);
        try {
            return ResponseEntity
                    .ok()
                    .header(CONTENT_DISPOSITION, getAttachmentHeader(file.getFileAttrs()))
                    .body(toByteArray(file.getDataStream()));
        } catch (IOException e) {
            e.printStackTrace();// todo
            throw new RuntimeException();
        }
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public FileAttributes saveFile(@RequestParam("file") MultipartFile file) {
        FileAttributes fileAttrs = new FileAttributes();
        fileAttrs.setContentType(file.getContentType());
        fileAttrs.setFileName(file.getOriginalFilename());

        final FileAttributes SavedFileAttributes = fileStorageService.save(createDataFile(file, fileAttrs));

        addHateoasLinks(SavedFileAttributes);
        return SavedFileAttributes;
    }

    @ExceptionHandler(FSServiceException.class)
    public ResponseEntity handleStorageFileNotFound(FSServiceException e) {
        return ResponseEntity.notFound().build();
    }

    private void addHateoasLinks(FileAttributes fileAttributes) {
        final String fileId = fileAttributes.getFileId();
        fileAttributes.add(linkTo(methodOn(getClass()).findOne(fileId)).withSelfRel());
        fileAttributes.add(linkTo(methodOn(getClass()).serveFile(fileId)).withRel("download"));
        fileAttributes.add(linkTo(methodOn(CommentsController.class).getComments(fileId)).withRel("comments"));
    }

    private DataFile createDataFile(MultipartFile file, FileAttributes fileAttrs) {
        try {
            DataFile dataFile = new DataFile();
            dataFile.setDataStream(file.getInputStream());
            dataFile.setFileAttrs(fileAttrs);
            return dataFile;
        } catch (IOException e) {
            throw new RuntimeException();// todo
        }
    }

    private static String getAttachmentHeader(FileAttributes fileAttrs) {
        return String.format(ATTACHMENT_FILENAME, fileAttrs.getFileName());
    }
}
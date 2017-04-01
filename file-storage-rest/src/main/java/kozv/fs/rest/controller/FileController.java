package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.service.IFileStorageService;
import kozv.fs.api.service.exception.PersistentFileNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Controller
@RequestMapping("/api/files")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
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
        return fileStorageService.findOne(fileId).getFileAttrs();
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String fileId) throws IOException {
        DataFile file = fileStorageService.findOne(fileId);
        return ResponseEntity
                .ok()
                .header(CONTENT_DISPOSITION, getAttachmentHeader(file.getFileAttrs()))
                .body(toByteArray(file.getData()));
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public FileAttributes saveFile(@RequestParam("file") MultipartFile file/*, @RequestParam("metadata") GridFileMetadata metadata*/) throws IOException {
        FileAttributes fileAttrs = new FileAttributes();
        fileAttrs.setContentType(file.getContentType());
        fileAttrs.setFileName(file.getOriginalFilename());

        return fileStorageService.save(createDataFile(file, fileAttrs));
    }

    @ExceptionHandler(PersistentFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(PersistentFileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }


    private DataFile createDataFile(@RequestParam("file") MultipartFile file, FileAttributes fileAttrs) throws IOException {
        DataFile dataFile = new DataFile();
        dataFile.setData(file.getInputStream());
        dataFile.setFileAttrs(fileAttrs);
        return dataFile;
    }

    private static String getAttachmentHeader(FileAttributes fileAttrs) {
        return String.format(ATTACHMENT_FILENAME, fileAttrs.getFileName());
    }
}
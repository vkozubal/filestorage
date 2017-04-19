package kozv.fs.api.rest;

import kozv.fs.api.model.FileAttributes;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Describes the restful api for {@link FileAttributes}
 */
@RequestMapping(value = "/api/files", produces = "application/hal+json")
public interface IFilesClient {
    @GetMapping
    @ResponseBody
    Resources<FileAttributes> findAll();

    @GetMapping("/{fileId}/attributes")
    @ResponseBody
    FileAttributes findOne(@PathVariable String fileId);

    @GetMapping(value = "/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Resource> serveFile(@PathVariable String fileId);

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    FileAttributes saveFile(@RequestParam("file") MultipartFile file);
}
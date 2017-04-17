package kozv.fs.api.rest;

import kozv.fs.api.model.FileAttributes;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{fileId}")
    ResponseEntity<byte[]> serveFile(@PathVariable String fileId);

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    FileAttributes saveFile(@RequestParam("file") MultipartFile file);
}
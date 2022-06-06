package com.app.fileManager.web;

import com.app.fileManager.FileManagerApplication;
import com.app.fileManager.domain.Document;
import com.app.fileManager.service.AdminFileService;
import com.app.fileManager.service.UserFilesService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FilesController {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);
    private final UserFilesService userFilesService;
    private final AdminFileService adminFileService;

    public FilesController(UserFilesService userFilesService, AdminFileService adminFileService) {
        this.userFilesService = userFilesService;
        this.adminFileService = adminFileService;
    }

    @GetMapping("/files")
    public ResponseEntity<List<Document>> getAllByUser(Authentication authentication){
        return new ResponseEntity<>(userFilesService.findAll(authentication), HttpStatus.OK);
    }

    @PostMapping(path = "/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart(required = true) MultipartFile file, Authentication authentication){
        if (file.isEmpty()) {
            logger.error("failed to upload file: " + file);
            return new ResponseEntity<>("failed to upload", HttpStatus.BAD_REQUEST);
        } else {
            userFilesService.addFile(file, authentication);
            return new ResponseEntity<>("file was uploaded successfully", HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/files/{bucketName}/{fileName}")
    public ResponseEntity<Object> getFile(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName ,Authentication authentication) throws IOException {
        if (bucketName.equals(authentication.getName()) ||
                authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("admin"))){
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(IOUtils.toByteArray(userFilesService.getObject(bucketName, fileName)));
        }else {
            logger.error("unauthorized to access /"+bucketName+'/'+fileName+ " authentication: " + authentication);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/files/allfiles")
    public ResponseEntity<List<Document>> getAll(Authentication authentication){
        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("admin"))){
            logger.debug("returning all files");
            return new ResponseEntity<>(adminFileService.findAll(), HttpStatus.OK);
        } else{
            logger.error("unauthorized to get all files | " + authentication);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/files/{bucketName}/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable("bucketName") String bucketName, @PathVariable("fileName") String fileName ,Authentication authentication){
        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("admin")) ||
                bucketName.equals(authentication.getName())){
            adminFileService.deleteFile(bucketName, fileName);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            logger.error("authorities " + authentication.getAuthorities());
            logger.error("unauthorized to delete /"+bucketName+'/'+fileName+ " authentication: " + authentication);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

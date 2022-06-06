package com.app.fileManager.service;

import com.app.fileManager.FileManagerApplication;
import com.app.fileManager.domain.Document;
import com.app.fileManager.domain.User;
import com.app.fileManager.repository.DocumentRepository;
import com.app.fileManager.repository.UserRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserFilesService {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);
    private final MinioClient minioClient;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final BucketService bucketService;

    public UserFilesService(MinioClient minioClient, DocumentRepository documentRepository, UserRepository userRepository, BucketService bucketService) {
        this.minioClient = minioClient;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.bucketService = bucketService;
    }

    public List<Document> findAll(Authentication authentication) {
        User user = userRepository.findUserByFirstName(authentication.getName());
        logger.debug("getting all documents for user: " + user.getId());
        return user.getDocuments().stream().toList();
    }

    public void addFile(MultipartFile file, Authentication authentication){
        bucketService.createBucket(authentication.getName());
        try {
            String uuid = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(authentication.getName())
                    .object(uuid + file.getOriginalFilename())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
            Document document = new Document(
                    file.getOriginalFilename(),
                    authentication.getName().concat('/' + uuid + file.getOriginalFilename()),
                    file.getSize(),
                    file.getContentType()
                    );
            documentRepository.save(document);
            userRepository.findUserByFirstName(authentication.getName()).addDocument(document);
        } catch (Exception e) {
            logger.error("Error Happened when uploading file: ", e);
        }
    }

    public InputStream getObject(String bucketName, String filename){
        InputStream stream;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            logger.error("Error happened while  objects from minio: ", e);
            return null;
        }
        return stream;
    }
}

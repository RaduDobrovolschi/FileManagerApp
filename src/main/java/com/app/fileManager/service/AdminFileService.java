package com.app.fileManager.service;

import com.app.fileManager.FileManagerApplication;
import com.app.fileManager.domain.Document;
import com.app.fileManager.repository.DocumentRepository;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@Transactional
public class AdminFileService {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);
    private final BucketService bucketService;
    private final MinioClient minioClient;
    private final DocumentRepository documentRepository;

    public AdminFileService(BucketService bucketService, MinioClient minioClient, DocumentRepository documentRepository) {
        this.bucketService = bucketService;
        this.minioClient = minioClient;
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll() {
        logger.debug("getting all documents ");
        return documentRepository.findAll();
    }

    public void deleteFile(String bucketName, String fileName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (ServerException | InsufficientDataException | ErrorResponseException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException |
                 InvalidResponseException | XmlParserException | InternalException e) {
            throw new RuntimeException(e);
        }
    }

}

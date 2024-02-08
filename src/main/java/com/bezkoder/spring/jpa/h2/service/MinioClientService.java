package com.bezkoder.spring.jpa.h2.service;

import com.bezkoder.spring.jpa.h2.exception.MinioUploadException;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class MinioClientService {

    private final MinioClient minioClient;
    /**
     * Constructeur de MinioClientService
     * @param minioUrl
     * @param accessKey
     * @param secretKey
     */
    public MinioClientService(@Value("${minio.url}") String minioUrl,
                              @Value("${minio.accessKey}") String accessKey,
                              @Value("${minio.secretKey}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
    /**
     * Méthode pour téléverser un fichier PDF dans un bucket Minio
     * @param bucketName
     * @param objectName
     * @param content
     * @throws Exception
     */
    public void uploadPdf(String bucketName, String objectName, byte[] content) throws MinioUploadException {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(content), content.length, -1)
                            .contentType("application/pdf")
                            .build());
        } catch (Exception e) {
            throw new MinioUploadException("Failed to upload PDF to Minio", e);
        }
    }
}


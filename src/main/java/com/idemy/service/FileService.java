package com.idemy.service;

import com.idemy.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String saveFile(MultipartFile file) {
        return saveFile(file, "videos");
    }

    public String saveFile(MultipartFile file, String folder) {
        try {
            String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalName;
            String objectKey = folder + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
            return objectKey;
        } catch (IOException | SdkException e) {
            throw new FileUploadException("Fayl S3-ə yüklənərkən xəta baş verdi!", e);
        }
    }

    public InputStream loadFileAsStream(String objectKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(getObjectRequest);
            return stream;
        } catch (SdkException e) {
            throw new FileUploadException("S3-dən fayl oxunarkən xəta baş verdi!", e);
        }
    }
}
package com.lookatme.server.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cdnDomain;

    private final AmazonS3Client amazonS3Client;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String fileType = imageTypeCheck(multipartFile);
        File uploadFile = convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.FILE_CONVERT_FAILED));
        return upload(uploadFile, dirName, fileType);
    }

    private String upload(File file, String dirName, String fileType) {
        String fileName = String.format("%s/%s.%s", dirName, UUID.randomUUID(), fileType);
        putS3(file, fileName);
        removeNewFile(file);

        return cdnDomain + fileName;
    }

    private void putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            log.error("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 파일 타입이 이미지 타입인지 검증 (image/png, image/jpeg)
    private String imageTypeCheck(MultipartFile file) {
        String fileType = file.getContentType();
        if (!fileType.startsWith("image")) {
            throw new ErrorLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
        switch (fileType) {
            case "image/png":
                return "png";
            case "image/jpeg":
                return "jpg";
            default:
                throw new ErrorLogicException(ErrorCode.FILE_TYPE_NOT_SUPPORTED);
        }
    }
}

package com.diaryservice.webservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3Service {


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    @Transactional
    public String upload(MultipartFile multipartFile) throws IOException {
        String s3FileName = UUID.randomUUID().toString() + "-"  + multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    @Transactional
    public void delete(String fileName) throws IOException{
        String s = amazonS3.getUrl(bucket, fileName).toString();
        amazonS3.deleteObject(bucket, s);
        amazonS3.deleteObject(bucket, fileName);
    }

}

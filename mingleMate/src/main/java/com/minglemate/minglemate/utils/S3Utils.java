package com.minglemate.minglemate.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.minglemate.minglemate.config.AWSConfig;
import com.minglemate.minglemate.enums.HttpStatusEnum;
import com.minglemate.minglemate.exception.MingleMateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
@Import(AWSConfig.class)
public class S3Utils {

    private AmazonS3 s3Client;

    @Value("${aws.s3.Bucket}")
    private String bucketName;

    public String getImageUri(String s3Path) {
        String s3Key = s3Path.substring(s3Path.indexOf(bucketName + "/"));
        try {
            return s3Client.generatePresignedUrl(
                    new GeneratePresignedUrlRequest(bucketName, s3Key)
            ).toURI().toString();
        } catch (URISyntaxException e) {
            throw new MingleMateException(HttpStatusEnum.CONFLICT, "URI Syntax Exception");
        }
    }
}

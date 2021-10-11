package com.lc.boot.oss.autoconfigure.core;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * aliyun oss client
 */
@Slf4j
@AllArgsConstructor
public class AliOssClient implements OssClient{

    private final OssProperties.AliYunProperties ossProperties;

    /**
     * get aliyun OSS client
     * @return
     */
    private OSSClient initClient() {
        return new OSSClient(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }

    /**
     * get file extension
     * @param FilenameExtension
     * @return
     */
    private String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") || FilenameExtension.equalsIgnoreCase("jpg")
                || FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        return "application/x-msdownload";
    }

    /**
     * upload file
     * @param instream
     * @param fileName
     * @return
     */
    @Override
    public String uploadInputStreamFile(InputStream instream, String fileName) {
        String result = "";
        OSSClient ossClient = null;
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            ossClient = initClient();
            PutObjectResult putResult = ossClient.putObject(ossProperties.getBucketName(),
                    ossProperties.getRootPath() + fileName,
                    instream, objectMetadata);
            result = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != ossClient) {
                ossClient.shutdown();
            }
        }
        return result;
    }

    /**
     * check bucket name exist
     * @param name
     * @return
     */
    @Override
    public boolean exit(String name) {
        OSSClient ossClient = initClient();
        return ossClient.doesBucketExist(name);
    }
}

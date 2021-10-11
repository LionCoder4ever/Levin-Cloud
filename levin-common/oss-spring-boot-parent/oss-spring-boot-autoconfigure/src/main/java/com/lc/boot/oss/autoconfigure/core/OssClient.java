package com.lc.boot.oss.autoconfigure.core;

import java.io.InputStream;

/**
 * client interface for multi type third party service provider like amazon, aliyun
 */
public interface OssClient {

    // upload InputStream
    String uploadInputStreamFile(InputStream inputStream, String fileName);

    // chekc exit the bucket name
    boolean exit(String name);
}

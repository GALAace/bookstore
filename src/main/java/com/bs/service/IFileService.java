package com.bs.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件处理
 * @Auther: 杨博文
 * @Date: 2019/5/16 00:48
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}

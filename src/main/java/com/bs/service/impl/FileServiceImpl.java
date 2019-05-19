package com.bs.service.impl;

import com.bs.service.IFileService;
import com.bs.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description:
 * @Auther: 杨博文
 * @Date: 2019/5/16 00:49
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("." )+ 1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("上传文件名:{},上传路径:{},新文件名:{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功
            //将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除upload里的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常");
            return null;
        }
        return targetFile.getName();
    }

}

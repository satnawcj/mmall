package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        // 先声明目录
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        // 再声明目录下的文件
        File targetFile = new File(path, uploadFileName);
        try {
            // 上传成功
            file.transferTo(targetFile);
            //上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 如果上传到 ftp 后，就要把程序中的图片删除。
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常~~", e);
            return null;
        }
        // 要把文件名称返回，方便系统调用。
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }
}

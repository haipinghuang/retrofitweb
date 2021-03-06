package com.hai.controller;

import com.hai.base.BaseController;
import com.hai.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 文件上传下载服务类
 * Created by huanghp on 2018/10/31.
 * Email h1132760021@sina.com
 */
@Controller
public class FileController extends BaseController {
    private static final String PATH_UPLOAD = "E:\\download\\upload_test\\";

    /**
     * 文件下载
     *
     * @param file
     * @param name
     * @return String
     */
    @RequestMapping(value = "uploadFileWithName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8" /*produces：解决中文乱码*/)
    @ResponseBody
    private String uploadFileWithName(@RequestParam(value = "file") CommonsMultipartFile file, @RequestParam(value = "name", required = false) String name) {
        System.out.println("recevied file name=" + file.getOriginalFilename() + ",file length=" + file.getSize() + ",and name=" + name);
        String s = filesUpload(file);
        logger.info("文件上传：" + s);
        return s;
    }

    /**
     * 上传文件到 F:/fileUpload
     *
     * @param file
     * @return String
     */
    @RequestMapping("uploadFile")
    @ResponseBody
    private String filesUpload(CommonsMultipartFile file) {
        if (file != null && !file.isEmpty()) {
            logger.info("recevied file name=" + file.getOriginalFilename() + ",file length=" + file.getSize());
            File destFile = new File(PATH_UPLOAD, new Date().getTime() + file.getOriginalFilename());
            if (!destFile.getParentFile().exists()) destFile.getParentFile().mkdir();
            try {
                file.transferTo(destFile);
                return "file:" + file.getOriginalFilename() + " upload success";
            } catch (IOException e) {
                e.printStackTrace();
                destFile.delete();
                return "file:" + file.getOriginalFilename() + " upload failed";
            }
        }
        return "file is null";
    }

    /**
     * 文件下载
     *
     * @param fileNmae
     * @return ResponseEntity
     */
    @RequestMapping("downloadFile")
    private ResponseEntity<byte[]> download(@RequestParam("fileName") String fileNmae) {
        if (!StringUtils.isEmpty(fileNmae)) {
            File file = new File(PATH_UPLOAD, fileNmae);
            if (file.exists() && file.length() > 0) {
                logger.info("服务器准备下载服务：" + file.getName() + "文件大小=" + file.length());
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                httpHeaders.setContentDispositionFormData("attachment", file.getAbsolutePath());
                try {
                    return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.CREATED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

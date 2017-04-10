package com.hai.controller;

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
 * Created by 黄海 on 2017/4/10.
 */
@Controller
public class FileController {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    //解决中文乱码
    @RequestMapping(value = "uploadFileWithName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    private String uploadFileWithUsername(@RequestParam("file") CommonsMultipartFile file, @RequestParam("name") String name) {
        System.out.println("recevied file name=" + file.getOriginalFilename() + ",file length=" + file.getSize() + ",and name=" + name);
        String s = filesUpload(file);
        System.out.println("文件上传：" + s);
        return s;
    }

    @RequestMapping("uploadFile")
    @ResponseBody
    private String filesUpload(CommonsMultipartFile file) {
        if (file != null && !file.isEmpty()) {
            System.out.println("recevied file name=" + file.getOriginalFilename() + ",file length=" + file.getSize());
            String path = "F:/fileUpload";
            File destFile = new File(path, new Date().getTime() + file.getOriginalFilename());
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

    @RequestMapping("downloadFile")
    private ResponseEntity<byte[]> download(@RequestParam("fileName") String fileNmae) {
        if (!StringUtils.isEmpty(fileNmae)) {
            File file = new File("F:\\fileUpload\\" + fileNmae);
            if (file.exists() && file.length() > 0) {
                logger.info(file.getName() + "文件大小=" + file.length());
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

package com.hlp.agrisys.controller;

import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr.Han
 * @create 2022-07-17 22:34
 */
@RestController
public class UploadController {
    @Autowired
    private IUploadService iUploadService;

    @PostMapping("/upload")
    public Result uploadImg(MultipartFile img){
        return iUploadService.uploadImg(img);
    }
}

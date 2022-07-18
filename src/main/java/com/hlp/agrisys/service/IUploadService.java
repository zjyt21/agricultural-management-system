package com.hlp.agrisys.service;

import com.hlp.agrisys.entity.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Mr.Han
 * @create 2022-07-17 22:36
 */
public interface IUploadService {
    Result uploadImg(MultipartFile img);
}

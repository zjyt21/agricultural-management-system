package com.hlp.agrisys.service.impl;

import com.google.gson.Gson;
import com.hlp.agrisys.entity.Result;
import com.hlp.agrisys.service.IUploadService;
import com.hlp.agrisys.util.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import jdk.management.resource.ResourceRequestDeniedException;
import lombok.Data;
import org.omg.CORBA.SystemException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author Mr.Han
 * @create 2022-07-17 22:37
 */
@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements IUploadService {
    //...生成上传凭证，然后准备上传
    private String accessKey; // 要和配置类的名字一致
    private String secretKey;
    private String bucket;
    private String domainName;

    @Override
    public Result uploadImg(MultipartFile img) {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw new RuntimeException("The file type is wrong, please upload the file in png or jpg format");
        }
        //如果判断通过，上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img, filePath);
        return new Result(200, "上传成功", url);
    }

    private String uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名，这边我们自己指定文件名
        String key = filePath;

        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
                return domainName + key; //域名 + 文件名
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}

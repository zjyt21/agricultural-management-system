package com.hlp.agrisys.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * generate the path of file
 *
 * @author Mr.Han
 * @create 2022-07-17 23:08
 */
public class PathUtils {
    public static String generateFilePath(String fileName){
        //Generate path based on date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = sdf.format(new Date());
        //user UUID as file name
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // suffix is the same as file
        int index = fileName.lastIndexOf(".");
        // for example: abc.jpg -> .jpg
        String fileType = fileName.substring(index);
        return  new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}

package com.bloomtoget.util;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

public class Imgupload {
    static final int THUMB_WIDTH = 300;
    static final int THUMB_HEIGHT = 300;

    public static String fileUpload(String uploadPath,
                                    String fileName, MultipartFile fileData,
//                                    String fileName,byte[] fileData,
                                    String datePath) throws Exception {
            UUID uid = UUID.randomUUID();

            String newFileName = uid + "_" + fileName;
            String imgPath = uploadPath + File.separator + datePath;
            File target = new File(imgPath, newFileName);   // imgPathuid_fileName.img 로 생성
            fileData.transferTo(target);
//            FileCopyUtils.copy(fileData, target);   //byte 타입 배열(in)을 지정 file에 복사(out)

            String thumbFileName = "thumb_" + newFileName;
                       // File.seperator = "\"
                       // new File(URI uri) : file uri 경로에 대한 파일의 File 객체를 생성
            File image = new File(imgPath + File.separator + newFileName);  // target의 파일을 객체생성?
            File thumbnail = new File(imgPath + File.separator + "thumb" + File.separator + thumbFileName); // imgPath\thumb\thumb_uid_fileName.img 생성

            if (image.exists()) {
                thumbnail.getParentFile().mkdirs(); //  imgPath\thumb\ 디렉토리 생성
                Thumbnails.of(image).size(THUMB_WIDTH, THUMB_HEIGHT).toFile(thumbnail);
            }
            return newFileName;
        }

        public static String calcPath(String uploadPath) {
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
            String datePath = format.format(now);
            makeDir(uploadPath, datePath);
            return datePath;
        }

    private static void makeDir(String uploadPath, String datePath) {
        if (new File(uploadPath + File.separator + datePath).exists()) {
            return;
        }
        File dirPath = new File(uploadPath + File.separator + datePath);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }
    }
    }


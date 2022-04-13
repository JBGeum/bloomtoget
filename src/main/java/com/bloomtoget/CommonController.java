package com.bloomtoget;

import com.bloomtoget.user.login.service.LoginService;
import com.bloomtoget.util.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.bloomtoget.util.Imgupload;
import java.io.File;


@Controller
public class CommonController {
    @Autowired
    LoginService loginService;

    @RequestMapping("/main")
    public String mainPage(){
        return "/main";
    }

    @RequestMapping("/imgUpload")
    public String imgUpload(MultipartFile file) throws Exception {
        String uploadPath = Path.UPLOAD_PATH;
        String imgUploadPath = uploadPath + File.separator + "imgUpload";
        String datePath = Imgupload.calcPath(imgUploadPath);
        String fileName = null;

        if (file != null) {
//            fileName =Imgupload.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(),datePath);
            fileName = Imgupload.fileUpload(imgUploadPath, file.getOriginalFilename(), file, datePath);
        } else {
            fileName = uploadPath + File.separator + "img" + File.separator + "default.png";
        }
        return File.separator + "imgUpload" + File.separator + datePath + File.separator + fileName;
    }
}

package com.bloomtoget.user;

import com.bloomtoget.user.service.UserService;
import com.bloomtoget.util.Imgupload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Resource(name="uploadPath")
    private String uploadPath;

    @GetMapping("/join")
    public String join(){
        return "user/join/join";
    }

    @PostMapping("/join")
    public ModelAndView insertUser(UserVO user,MultipartFile file) throws Exception {
        String s = File.separator;
        String imgUploadPath = uploadPath +s+ "imgUpload";
        String datePath = Imgupload.calcPath(imgUploadPath);
        String fileName = null;

        if(file != null) {
            fileName =Imgupload.fileUpload(imgUploadPath, file.getOriginalFilename(),file,datePath);
        } else {
            fileName = uploadPath +s+ "img" +s+ "default.png";
        }

        user.setProfImg(s+"imgUpload"+s+datePath+s+fileName);
        user.setProfImgThumb(s+"imgUpload"+s+datePath+s+"thumb"+s+"thumb_"+fileName);
        int userNo = userService.insertUser(user);
        System.out.println("userNo : "+userNo);

        ModelAndView view = new ModelAndView();
        view.addObject("user",userService.getUserByNo(userNo));
        view.setViewName("user/join/welcome");
        return view;
    }
    @GetMapping("/idCheck")
    public String idCheckWindow(){
        return "/user/join/idCheck";
    }

    @PostMapping("/idCheck")
    public ModelAndView idCheck(String userId){
        boolean idExists = userService.idExistsCheck(userId);
        ModelAndView view = new ModelAndView();
        view.addObject("userId",userId);
        view.addObject("idExists",idExists);
        view.setViewName("user/join/idCheck");
        return view;
    }


}

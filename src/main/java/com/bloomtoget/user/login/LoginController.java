package com.bloomtoget.user.login;

import com.bloomtoget.user.login.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class LoginController {
    @Autowired
    LoginService loginService;

    @GetMapping("/login")   //로그인 주소가 입력되었을때
    public String checkLogin(HttpServletRequest request,Model model) {
        boolean alreadyLoggedIn = loginService.alreadyLoggedIn(request);
        model.addAttribute("alreadyLoggedIn",alreadyLoggedIn);
        if(alreadyLoggedIn){
            return "redirect:/loginStatus";
        }
        return "/user/login";
    }
    @PostMapping("/login")
    public ModelAndView login(String userId, String userPwd,HttpSession session) {
        ModelAndView view = new ModelAndView();
        if(loginService.tryLogin(userId, userPwd)) {
            session.setAttribute("userNo",loginService.getLoginUserByID(userId).getUserNo());
            session.setAttribute("userId",userId);
            session.setAttribute("profImgThumb",loginService.getLoginUserByID(userId).getProfImgThumb());
            view.addObject("user",loginService.getLoginUserByID(userId));
            view.setViewName("/main");
            return view;
        }
        view.addObject("loginResult",false);
        view.setViewName("/user/login");
        return view;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if(session!=null && session.getAttribute("userNo")!=null) {
            session.invalidate();
        }
        return "redirect:/main";
    }

    @RequestMapping("/loginStatus")
    public ModelAndView getUserNo(@SessionAttribute("userNo") int userNo){
        ModelAndView view = new ModelAndView();
        view.addObject("alreadyLoggedIn",true);
        view.addObject("user",loginService.getLoginUserByNo(userNo));
        view.setViewName("/user/login");
        //sessionAttributes 활용해서 session에 존재하는 user을 더 쉽게 가져올 수 있는지 확인
        return view;
    }
}


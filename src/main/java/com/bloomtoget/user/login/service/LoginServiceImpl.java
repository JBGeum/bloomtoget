package com.bloomtoget.user.login.service;


import com.bloomtoget.user.UserDAO;
import com.bloomtoget.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;


@Service("loginService")
public class LoginServiceImpl implements LoginService{
    @Autowired
    UserDAO userDAO;
    @Override
    public boolean alreadyLoggedIn(HttpServletRequest request){
        HttpSession session = request.getSession(false);
       if(session != null && session.getAttribute("userNo")!=null){
            return true;    //로그인 되어있음
        }
        return false;
    }

    @Override
    public boolean tryLogin(String userId, String userPwd) {
        HashMap<String,String> loginInfo = new HashMap<>();
        loginInfo.put("userId", userId);
        loginInfo.put("userPwd",userPwd);
        return userDAO.tryLogin(loginInfo);
    }

    @Override
    public UserVO getLoginUserByID(String userId){
        return userDAO.getLoginUserByID(userId);
    }

    @Override
    public UserVO getLoginUserByNo(int userNo) {
        return userDAO.getLoginUserByNo(userNo);
    }

}

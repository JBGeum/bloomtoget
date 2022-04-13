package com.bloomtoget.user.login.service;




import com.bloomtoget.user.UserVO;

import javax.servlet.http.HttpServletRequest;


public interface LoginService {
    public boolean alreadyLoggedIn(HttpServletRequest request);
    public boolean tryLogin(String memberId, String memberPw);
    public UserVO getLoginUserByID(String userId);
    public UserVO getLoginUserByNo(int userNo);
}

package com.bloomtoget.user.service;

import com.bloomtoget.user.UserVO;

import java.util.List;

public interface UserService {
    public int insertUser(UserVO user);
    public void updateUser(UserVO user);
    public void deleteUser(UserVO user);
    public UserVO getUser(UserVO user);
    public UserVO getUserByNo(int userNo);
    public UserVO getUserById(String userId);
    public List<UserVO> getUserList(UserVO user);
    public boolean idExistsCheck(String userId);
}

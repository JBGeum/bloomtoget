package com.bloomtoget.user.service;

import com.bloomtoget.user.UserDAO;
import com.bloomtoget.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("UserService")
public class UserServiceImpl implements UserService{
    @Autowired UserDAO userDAO;

    @Override
    public int insertUser(UserVO user) {
       return userDAO.insertUser(user);
    }

    @Override
    public void updateUser(UserVO user) {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(UserVO user) {
userDAO.deleteUser(user);
    }

    @Override
    public UserVO getUser(UserVO user) {
        return userDAO.getUser(user);
    }

    @Override
    public UserVO getUserByNo(int userNo) {
        return userDAO.getUserByNo(userNo);
    }

    @Override
    public UserVO getUserById(String userId) {
        return userDAO.getUserById(userId);
    }


    @Override
    public List<UserVO> getUserList(UserVO user) {
        return userDAO.getUserList(user);
    }

    @Override
    public boolean idExistsCheck(String userId) {
        return userDAO.idExistCheck(userId);  //결과값이 존재하면(=중복아이디있음) true, 없으면 false를 리턴
    }
}

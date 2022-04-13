package com.bloomtoget.user;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class UserDAO {
    @Autowired
    private SqlSessionTemplate mybatis;

    public int insertUser(UserVO user){
         mybatis.insert("UserDAO.insertUser",user);
         return (int) user.getUserNo();
    }
    public void updateUser(UserVO user){
        mybatis.update("UserDAO.updateUser",user);
    }
    public void deleteUser(UserVO user){
        mybatis.delete("UserDAO.deleteUser",user);
    }
    public UserVO getUser(UserVO user){
        return mybatis.selectOne("UserDAO.getUser", user);
    }
    public UserVO getUserByNo(int userNo){
       return mybatis.selectOne("UserDAO.getUserByNo", userNo);
    }
    public UserVO getUserById(String userId){
        return mybatis.selectOne("UserDAO.getUserById", userId);
    }
    public boolean idExistCheck(String userId){
        //받은 id와 일치하는 회원이 존재하면(null이 아님) true를 반환
        return mybatis.selectOne("UserDAO.getUserById", userId) != null;
    }

    public List<UserVO> getUserList(UserVO user){
        return mybatis.selectList("UserDAO.getUserList",user);
    }

    public boolean tryLogin(Map<String, String> loginInfo) {
        return mybatis.selectOne("LoginDAO.tryLogin",loginInfo);
    }
    public UserVO getLoginUserByID(String userId){
        return mybatis.selectOne("LoginDAO.getLoginUserByID",userId);
    }

    public UserVO getLoginUserByNo(int userNo) {
        return mybatis.selectOne("LoginDAO.getLoginUserByNo",userNo);
    }
}

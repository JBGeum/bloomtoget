package com.bloomtoget.todo;

import com.bloomtoget.user.UserTd;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class TdtDAO {
    @Autowired
    private SqlSessionTemplate mybatis;

    public int getUserGroupNo(int userNo) {
        return mybatis.selectOne("GroupDAO.getUserGroupNo", userNo);
    }

    public int insertTodoTask(TodoTaskVO tdt) {
        mybatis.insert("TdtDAO.insertTodoTask", tdt);
        return (int) tdt.getTdt_No();
    }

    public TodoDetail getTaskByTdtNo(int tdtNo) {
        return mybatis.selectOne("TdtDAO.getTaskByTdtNo", tdtNo);
    }

    public void updateTodoTask(TodoTaskVO tdt) {
        mybatis.update("TdtDAO.updateTodoTask", tdt);
    }

    public List<TodoDetail> getGroupTdtList(int groupNo) {
        return mybatis.selectList("TdtDAO.getGroupTdtList", groupNo);
    }

    public List<TodoDetail> getUserTdtList(int userNo) {
        return mybatis.selectList("TdtDAO.getUserTdtList", userNo);
    }

    public List<TodoDetail> getOtherTdtList(int userNo) {
        return mybatis.selectList("TdtDAO.getOtherTdtList", userNo);
    }


    //    UserTdDAO
    public void joinWriterToTask(TodoTaskVO tdt) {
        mybatis.insert("UserTdDAO.joinWriterToTask", tdt);
    }

    public void joinUserToTask(UserTd ut) {
        mybatis.insert("UserTdDAO.joinUserToTask", ut);
    }

    public void quitUserFromTask(UserTd ut) {
        mybatis.delete("UserTdDAO.quitUserFromTask", ut);
    }

    public List<UserTd> getTaskMembers(int tdtNo) {
        return mybatis.selectList("UserTdDAO.getTaskMembers", tdtNo);
    }

    public boolean alreadyMember(Map<String, Integer> userTd) {
        return mybatis.selectOne("UserTdDAO.alreadyMember", userTd);
    }

    public List<TodoDetail> getMyTasks(int userNo) {
        return mybatis.selectList("UserTdDAO.getMyTasks", userNo);
    }


}

package com.bloomtoget.todo.service;

import com.bloomtoget.todo.*;
import com.bloomtoget.user.UserTd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("TodoService")
public class TodoServiceImpl implements TodoService {
    @Autowired TdtDAO tdtDAO;
    @Autowired TdrDAO tdrDAO;

    @Override
    public int getUserGroupNo(long userNo) {
        return tdtDAO.getUserGroupNo((int) userNo);
    }

    @Override
    public int insertTodoTask(TodoTaskVO tdt) {
        return tdtDAO.insertTodoTask(tdt);
    }

    @Override
    public TodoDetail getTaskByTdtNo(int tdtNo) {
        return tdtDAO.getTaskByTdtNo(tdtNo);
    }

    @Override
    public void updateTodoTask(TodoTaskVO tdt) {
        tdtDAO.updateTodoTask(tdt);
    }

    @Override
    public List<TodoDetail> getGroupTdtList(int groupNo) {
        return tdtDAO.getGroupTdtList(groupNo);
    }

    @Override
    public List<TodoDetail> getUserTdtList(int userNo) {
        return tdtDAO.getUserTdtList(userNo);
    }

    @Override
    public List<TodoDetail> getOtherTdtList(int userNo) {
        return tdtDAO.getOtherTdtList(userNo);
    }

    @Override
    public void joinWriterToTask(TodoTaskVO tdt) {
        tdtDAO.joinWriterToTask(tdt);
    }

    @Override
    public void joinUserToTask(UserTd ut) {
        tdtDAO.joinUserToTask(ut);
    }

    @Override
    public void quitUserFromTask(UserTd ut) {
        tdtDAO.quitUserFromTask(ut);
    }

    @Override
    public List<UserTd> getTaskMembers(int tdtNo) {
        return tdtDAO.getTaskMembers(tdtNo);
    }

    @Override
    public boolean alreadyMember(int tdtNo, int userNo) {
        Map<String, Integer> userTd = new HashMap<>();
        userTd.put("userNo", userNo);
        userTd.put("tdtNo", tdtNo);
        return tdtDAO.alreadyMember(userTd);
    }

    @Override
    public List<TodoDetail> getMyTasks(int userNo) {
        return tdtDAO.getMyTasks(userNo);
    }

    @Override
    public int insertTodoRecord(TodoRecordVO tdr) {
        return tdrDAO.insertTodoRecord(tdr);
    }
}

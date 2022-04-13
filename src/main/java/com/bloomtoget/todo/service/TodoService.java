package com.bloomtoget.todo.service;

import com.bloomtoget.todo.TodoDetail;
import com.bloomtoget.todo.TodoRecordVO;
import com.bloomtoget.todo.TodoTaskVO;
import com.bloomtoget.user.UserTd;

import java.util.List;


public interface TodoService {
    public int getUserGroupNo(long userNo);

    public int insertTodoTask(TodoTaskVO tdt);

    public TodoDetail getTaskByTdtNo(int tdtNo);

    public void updateTodoTask(TodoTaskVO tdt);

    public List<TodoDetail> getGroupTdtList(int groupNo);

    public List<TodoDetail> getUserTdtList(int userNo);

    public List<TodoDetail> getOtherTdtList(int userNo);

    public void joinWriterToTask(TodoTaskVO tdt);

    public void joinUserToTask(UserTd ut);

    public void quitUserFromTask(UserTd ut);

    public List<UserTd> getTaskMembers(int tdtNo);

    public boolean alreadyMember(int tdtNo, int userNo);

    public List<TodoDetail> getMyTasks(int userNo);

    public int insertTodoRecord(TodoRecordVO tdr);

}

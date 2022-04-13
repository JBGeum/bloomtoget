package com.bloomtoget.todo;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class TdrDAO {
    @Autowired
    private SqlSessionTemplate mybatis;

    public int insertTodoRecord(TodoRecordVO tdr) {
        return mybatis.insert("TdrDAO.insertTodoRecord",tdr);
    }
}

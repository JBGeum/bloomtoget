package com.bloomtoget.group;

import com.bloomtoget.user.UserVO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class GroupDAO {
    @Autowired
    private SqlSessionTemplate mybatis;

    public int insertGroup(GroupVO group) {
        mybatis.insert("GroupDAO.insertGroup", group);
        return (int) group.getGroupNo();
    }

    public void updateGroup(GroupVO group) {
        mybatis.update("GroupDAO.updateGroup", group);
    }

    public void deleteGroup(int groupNo) {
        mybatis.delete("GroupDAO.deleteGroup", groupNo);
    }

    public GroupVO getGroup(GroupVO group) {
        return mybatis.selectOne("GroupDAO.getGroup", group);
    }

    public GroupVO getGroupByNo(int groupNo) {
        return mybatis.selectOne("GroupDAO.getGroupByNo", groupNo);
    }

    public GroupVO getMyGroup(int userNo) {
        return mybatis.selectOne("GroupDAO.getMyGroup", userNo);
    }

    public GroupVO checkGroupPublic(int groupNo) {
        return mybatis.selectOne("GroupDAO.checkGroupPublic", groupNo);
    }

    public List<GroupVO> getPublicGroupList() {
        return mybatis.selectList("GroupDAO.getPublicGroupList");
    }

    public List<GroupVO> getJoinableGroupList() {
        return mybatis.selectList("GroupDAO.getJoinableGroupList");
    }

    public boolean joinableCheck(int groupNo) {
        return mybatis.selectOne("GroupDAO.joinableCheck", groupNo);
    }

    public void requestJoinGroup(Map<String, Integer> usertoGroup) {
        mybatis.update("GroupDAO.requestJoinGroup", usertoGroup);
    }

    public void allowJoinRequest(int userNo) {
        mybatis.update("GroupDAO.allowJoinRequest", userNo);
    }

    public void gainExp(int gainExp) {
        mybatis.update("GroupDAO.gainExp", gainExp);
    }

    public void lvUp() {
        mybatis.update("GroupDAO.lvUp");
    }

    public int getGroupAdmin(int groupNo) {
        return mybatis.selectOne("GroupDAO.getGroupAdmin", groupNo);
    }

    public List<String> getJoinableGroupMember() {
        return mybatis.selectList("GroupDAO.getJoinableGroupMember");
    }

    public List<UserVO> getGroupMembers(int groupNo) {
        return mybatis.selectList("GroupDAO.getGroupMembers", groupNo);
    }

    public void changeAdmin(GroupVO group) {
        mybatis.update("GroupDAO.changeAdmin", group);
    }

    public void quitGroup(int groupNo) {
        mybatis.update("GroupDAO.quitGroup", groupNo);
    }

    public int getUserGroupNo(int userNo) {
        return mybatis.selectOne("GroupDAO.getUserGroupNo", userNo);
    }

    public List<UserVO> getJoinRequestingUsers(int groupNo) {
        return mybatis.selectList("GroupDAO.getJoinRequestingUsers", groupNo);
    }

    public int getGroupMembersNum(int groupNo) {
        return mybatis.selectOne("GroupDAO.getGroupMembersNum", groupNo);
    }

    public void declineJoinRequest(int userNo) {
        mybatis.update("GroupDAO.declineJoinRequest", userNo);
    }

    public void declineAllJoinRequest(int groupNo) {
        mybatis.update("GroupDAO.declineAllJoinRequest", groupNo);
    }

    public void joinGroup(Map<String, Integer> usertoGroup) {
        mybatis.update("GroupDAO.joinGroup", usertoGroup);
    }
}

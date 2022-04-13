package com.bloomtoget.group.service;

import com.bloomtoget.group.GroupDAO;
import com.bloomtoget.group.GroupVO;
import com.bloomtoget.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service("GroupService")
public class GroupServiceImpl implements GroupService {
    @Autowired GroupDAO groupDAO;

    @Override
    public int insertGroup(GroupVO group) {
        return groupDAO.insertGroup(group);
    }

    @Override
    public void updateGroup(GroupVO group) {
        groupDAO.updateGroup(group);
    }

    @Override
    public void deleteGroup(int groupNo) {
        groupDAO.deleteGroup(groupNo);
    }

    @Override
    public GroupVO getGroup(GroupVO group) {
        return groupDAO.getGroup(group);
    }

    @Override
    public GroupVO getGroupByNo(int groupNo) {
        return groupDAO.getGroupByNo(groupNo);
    }

    @Override
    public GroupVO getMyGroup(int userNo) {
        return groupDAO.getMyGroup(userNo);
    }

    @Override
    public GroupVO checkGroupPublic(int groupNo) {
        return groupDAO.checkGroupPublic(groupNo);
    }

    @Override
    public List<GroupVO>  getPublicGroupList(){
        return groupDAO.getPublicGroupList();
    }
    @Override
    public List<GroupVO> getJoinableGroupList(){
        return groupDAO.getJoinableGroupList();
    }
    @Override
    public boolean joinableCheck(int groupNo){
        return groupDAO.joinableCheck(groupNo);
    }

    @Override
    public int getGroupAdmin(int groupNo) {
        return groupDAO.getGroupAdmin(groupNo);
    }

    @Override
    public List<String> getJoinableGroupMember() {
        return  groupDAO.getJoinableGroupMember();
    }

    @Override
    public void requestJoinGroup(Map<String, Integer> usertoGroup){
        groupDAO.requestJoinGroup(usertoGroup);
    }

    @Override
    public List<UserVO> getJoinRequestingUsers(int groupNo) {
        return groupDAO.getJoinRequestingUsers(groupNo);
    }

    @Override
    public void allowJoinRequest(int userNo){
        groupDAO.allowJoinRequest(userNo);
    }

    @Override
    public void gainExp(int gainExp){
        groupDAO.gainExp(gainExp);
    }

    @Override
    public void lvUp(){
        groupDAO.lvUp();
    }

    @Override
    public List<UserVO> getGroupMembers(int groupNo){
        return groupDAO.getGroupMembers(groupNo);
    }

    @Override
    public void changeAdmin(GroupVO group) {
        groupDAO.changeAdmin(group);
    }

    @Override
    public void quitGroup(int userNo) {
        groupDAO.quitGroup(userNo);
    }

    @Override
    public int getUserGroupNo(int userNo) {
        return groupDAO.getUserGroupNo(userNo);
    }

    @Override
    public int getGroupMembersNum(int groupNo) {
        return groupDAO.getGroupMembersNum(groupNo);
    }

    @Override
    public void declineJoinRequest(int userNo) {
        groupDAO.declineJoinRequest(userNo);
    }

    @Override
    public void declineAllJoinRequest(int groupNo) {
        groupDAO.declineAllJoinRequest(groupNo);
    }

    @Override
    public void joinGroup(Map<String, Integer> usertoGroup) {
        groupDAO.joinGroup(usertoGroup);
    }
}

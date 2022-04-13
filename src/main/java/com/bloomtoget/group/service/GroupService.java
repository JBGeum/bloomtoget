package com.bloomtoget.group.service;

import com.bloomtoget.group.GroupVO;
import com.bloomtoget.user.UserVO;


import java.util.List;
import java.util.Map;

public interface GroupService {
    public int insertGroup(GroupVO group);
    public void updateGroup(GroupVO group);
    public void deleteGroup(int groupNo);
    public GroupVO getGroup(GroupVO group);
    public GroupVO getGroupByNo(int groupNo);
    public GroupVO getMyGroup(int userNo);
    public GroupVO checkGroupPublic(int groupNo);
    public List<GroupVO> getPublicGroupList();
    public List<GroupVO> getJoinableGroupList();
    public void requestJoinGroup(Map<String, Integer> usertoGroup);
    public List<UserVO> getJoinRequestingUsers(int groupNo);
    public void allowJoinRequest(int userNo);
    public void gainExp(int gainExp);
    public void lvUp();
    public boolean joinableCheck(int groupNo);
    public int getGroupAdmin(int groupNo);
    public List<String> getJoinableGroupMember();
    public List<UserVO> getGroupMembers(int groupNo);
    public void changeAdmin(GroupVO group);
    public void quitGroup(int userNo);
    public int getUserGroupNo(int userNo);
    public int getGroupMembersNum(int groupNo);
    public void declineJoinRequest(int userNo);
    public void declineAllJoinRequest(int groupNo);
    public void joinGroup(Map<String, Integer> usertoGroup);
}

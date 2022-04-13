package com.bloomtoget.group;


import com.bloomtoget.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping("")   //세션에서 그룹 가입 상태라면 mygroup으로, 비가입상태라면 알림 페이지로
    public ModelAndView groupPage(HttpSession session){
        ModelAndView view = new ModelAndView();
        int userGroupNo = groupService.getUserGroupNo(getUserNo(session));
        if(groupService.getMyGroup(getUserNo(session))!=null){    //소속 그룹이 존재한다면
            view.setViewName("redirect:/group/mygroup");
            return view;
        } else if(userGroupNo<0){
            view.addObject("joinRequesting",-userGroupNo);
        }
        view.setViewName("/group/nogroup");
        return view ;
    }

    @RequestMapping("/admin")
    public ModelAndView groupAdmin(HttpSession session){
        ModelAndView view = new ModelAndView();
        int userGroupNo = groupService.getUserGroupNo(getUserNo(session));
        GroupVO userGroup = groupService.getMyGroup(getUserNo(session));
        if(userGroupNo == userGroup.getGroupAdmin()) {
            view.addObject("group", userGroup);
            view.setViewName("/group/admin");
        } else {
            view.addObject("reason","notAdmin");
            view.setViewName("/noAuth");
        }
        return view;
    }

    @GetMapping("/create")
    public ModelAndView create(HttpSession session){
        ModelAndView view = new ModelAndView();
        int userGroupNo = groupService.getUserGroupNo(getUserNo(session));
        if(userGroupNo ==0) {
            view.addObject("status","available");
        } else if(userGroupNo<0){
            view.addObject("status","joinRequesting");
        }else {
            view.addObject("status","alreadyMember");
        }
        view.setViewName("/group/create");
        return view;
    }

    @PostMapping("/create")
    public ModelAndView insertGroup(GroupVO group,HttpSession session){
        ModelAndView view = new ModelAndView();
        int groupNo = groupService.insertGroup(group);
        Map<String, Integer> usertoGroup = new HashMap<>();
        usertoGroup.put("userNo",getUserNo(session));
        usertoGroup.put("groupNo", groupNo);
        groupService.joinGroup(usertoGroup);
        view.addObject("group",groupService.getGroupByNo(groupNo));
        view.setViewName("/group/welcome");
        return view;
    }

    @RequestMapping("/joinsearch")
    public ModelAndView getjoinableGroup(){
        ModelAndView view = new ModelAndView();
        List<GroupVO> grouplist = groupService.getJoinableGroupList();
        List<String> memberNumList = groupService.getJoinableGroupMember();
        view.addObject("grouplist",grouplist);
        view.addObject("memberNumList",memberNumList);
        view.setViewName("/group/joinsearch");
        return view;
    }
    @RequestMapping("/join/{groupNo}") //그룹정보에서 요청을 받아 회원을 가입승인모드로 등록
    public ModelAndView joinGroup(@PathVariable int groupNo,HttpSession session){
//        1.해당 그룹 가입 상태인 인원수를 user 테이블에서 찾기(승인 대기 포함x)
//        2. 6명 미만일 경우, 폼 페이지로 전송, 그렇지 않을
        ModelAndView view = new ModelAndView();
        boolean joinable = groupService.joinableCheck(groupNo);
        int userNo = getUserNo(session);
        if(groupService.getUserGroupNo(userNo) != 0) {
            view.addObject("status","alreadyJoined");
            view.setViewName("/falseaccess");
            return view;
        }
        if(joinable){    //가입 가능한 상태인 그룹(true)이라면
            Map<String, Integer> usertoGroup = new HashMap<>();
            usertoGroup.put("userNo",getUserNo(session));
            usertoGroup.put("groupNo", groupNo);
            groupService.requestJoinGroup(usertoGroup);  //user 테이블의 회원정보에 groupNo를 음수(가입대기중)로 업데이트
            view.addObject("joinResult",true);
        } else {
            view.addObject("joinResult",false);
            //가입 불가능하다면 처리할 내용
        }
        view.addObject("joinable",joinable);   //승인가능여부 boolean
        view.setViewName("/group/joinresult");    // false면 alert 표시후 back, true면 가입신청되었다는 메세지
        return view;
    }

    @RequestMapping("/joinrequest/{groupNo}")
    public ModelAndView joinRequestingUsers(@PathVariable int groupNo,HttpSession session){
        ModelAndView view = new ModelAndView();
        if(getUserNo(session) == groupService.getGroupAdmin(groupNo)){//세션의 userNo가 해당 그룹 관리자라면
           view.addObject("users",groupService.getJoinRequestingUsers(groupNo));
           view.addObject("groupNo",groupNo);
            //그룹의 인원수 확인
            //6명이라면, 전체 가입 거절 메세지 활성화->인원수를 int로 보낸 후 view에서 숫자비교로 구분
           view.addObject("currentMemberNum",groupService.getGroupMembersNum(groupNo));
           view.setViewName("/group/joinrequest");
           return view;
        }
        view.setViewName("/falseaccess");
        return view;
    }

    @PostMapping("/allowjoin/user") //관리자가 가입 신청을 승인처리하는 요청
    public ModelAndView joinGroupAllowed(int userNo, int groupNo, HttpSession session){
        ModelAndView view = new ModelAndView();
        // 전달받은 userNo(가입예정자)의 현재 groupNo와 동일한 그룹의 관리자가 session의 userNo인지 확인하는 로직
        // 가능하면 추가 getGroupAdmin
        if(groupService.getGroupAdmin(groupNo) == getUserNo(session)){
            groupService.allowJoinRequest(userNo);
            view.setViewName("redirect:/group/joinrequest/" + groupNo);
            return view;
        }
        view.setViewName("/falseaccess");
        return view;
    }

    @PostMapping("/declinejoin/user")
    public ModelAndView declineJoinRequest(int userNo, int groupNo, HttpSession session){
        ModelAndView view = new ModelAndView();
        if(groupService.getGroupAdmin(groupNo) == getUserNo(session)){
            groupService.declineJoinRequest(userNo);
            // 실행후에도 멤버수 변화는 없음
            view.setViewName("redirect:/group/joinrequest/" + groupNo);
            return view;
        }
        view.setViewName("/falseaccess");
        return view;
    }

    @PostMapping("/stopjoinrequest")
    public ModelAndView stopJoinRequest(int groupNo, HttpSession session){
        ModelAndView view = new ModelAndView();
        int userNo = getUserNo(session);
        if(groupService.getUserGroupNo(userNo) <0) {
            groupService.quitGroup(userNo);
            view.setViewName("redirect:/group");
            return view;
        }
        view.addObject("status","StopJoinRequestFalse");
        view.setViewName("/falseaccess");
        return view;
    }

    @PostMapping("/declinejoin/all")
    public ModelAndView declineAllJoinRequest(int userNo, int groupNo, HttpSession session){
        ModelAndView view = new ModelAndView();
        if(groupService.getGroupAdmin(groupNo) == getUserNo(session)){
            groupService.declineAllJoinRequest(groupNo);
            // 실행후에도 멤버수 변화는 없음
            view.setViewName("redirect:/group/joinrequest/" + groupNo);
            return view;
        }
        view.setViewName("/falseaccess");
        return view;
    }

    @RequestMapping("/mygroup")    //내가 등록된 그룹의 상세정보페이지(회원 정보의 번호를 받아서 리다이렉트, 또는 등록되지 않은 경우 뷰로)
    public ModelAndView myGroup(HttpSession session){    //그룹메뉴 존재함
        ModelAndView view = new ModelAndView();
        int userNo = getUserNo(session);
        GroupVO group = groupService.getMyGroup(userNo);
        if(group != null) {
            view.addObject("group", group);
            view.setViewName("/group/mygroup");
        } else if(groupService.getUserGroupNo(userNo)<0){
            view.addObject("joinRequesting",-groupService.getUserGroupNo(userNo));
            view.setViewName("/group/nogroup");
        }else{
            view.setViewName("/group/nogroup");
        }
        return view;
    }

    @RequestMapping("/{groupNo}") //groupNo로 조회해서 해당 그룹의 정보 표시(자신이 속한 그룹이거나, 공개상태인 그룹)
    public ModelAndView groupDetails(@PathVariable int groupNo, HttpSession session){
        ModelAndView view = new ModelAndView();
        GroupVO group = groupService.getGroupByNo(groupNo);
        int userGroupNo = 0;
        if(groupService.getMyGroup(getUserNo(session)) != null) {
            userGroupNo = (int) groupService.getMyGroup(getUserNo(session)).getGroupNo();
        }
        //TODO 그룹의 회원(정보 or 수)도 함께 전송하기, 관리자의 id와 닉네임도
        if(groupNo == userGroupNo){
            view.addObject("status","join");
            view.addObject("group",group);
        } else if(group.getGroupPublic() == 1){
            view.addObject("status","opened");
            view.addObject("group",group);
        } else{
            view.addObject("status","false");
        }
        view.setViewName("/group/groupdetail");
        return view;
    }

    @GetMapping("/edit/{groupNo}") //groupNo로 조회해서 해당 그룹의 정보 표시(자신이 속한 그룹이거나, 공개상태인 그룹)
    public ModelAndView adminCheck(@PathVariable int groupNo,HttpSession session){
        ModelAndView view = new ModelAndView();
//        userNo와 해당 그룹의 관리자No가 동일한지 체크
        if(getUserNo(session) == groupService.getGroupAdmin(groupNo)) {
            // 관리자라면, 수정 가능한 폼 화면으로 전송
            GroupVO group = groupService.getGroupByNo(groupNo);
            view.addObject("group", group);
            view.setViewName("group/editform");
        } else {
            //아니라면, 권한 없음 페이지
            view.addObject("reason","notAdmin");
            view.setViewName("/noAuth");
        }
        return view;
    }

    @PostMapping("/edit/{groupNo}") //폼에서 받은 자료를 DB에 업데이트
    public String updateGroup(GroupVO group){
        groupService.updateGroup(group);
        return "redirect:/group/mygroup";     //수정된 그룹=내 그룹 페이지로
    }

    @GetMapping("/changeadmin/{groupNo}")
    public ModelAndView changeAdminForm(@PathVariable int groupNo,HttpSession session){
        ModelAndView view = new ModelAndView();
        int groupAdmin = groupService.getGroupAdmin(groupNo);
            if(getUserNo(session) == groupAdmin) {
                view.addObject("groupMembers",groupService.getGroupMembers(groupNo));
                view.addObject("groupNo",groupNo);
                view.addObject("groupAdmin",groupAdmin);
                view.setViewName("/group/changeadmin");
          return view;
        }
        view.addObject("reason","notAdmin");
        view.setViewName("/noAuth");
        return view;
        }

        @PostMapping("/changeadmin/{groupNo}")
        public String changeAdmin(@PathVariable int groupNo,GroupVO group){
            groupService.changeAdmin(group);
        return "redirect:/group/" + groupNo;
        }

        @PostMapping("/quit/{userNo}")
        public String quitGroup(@PathVariable int userNo){
        groupService.quitGroup(userNo);
        return "/group/quit";
        }

    public int getUserNo(HttpSession session){
        return Integer.parseInt(String.valueOf(session.getAttribute("userNo")));
    }

}

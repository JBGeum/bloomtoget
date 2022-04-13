package com.bloomtoget.todo;

import com.bloomtoget.todo.service.TodoService;
import com.bloomtoget.user.UserTd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    TodoService todoService;

    @RequestMapping("")
    public ModelAndView todoMain(){
        //todotask와 todorecord 메뉴로 각각 이동가능
        // 달성률 체크할 수 있도록 되어있는 화면?
        ModelAndView view = new ModelAndView();
        view.setViewName("/todotask/todomain");
        return view;
    }

    @GetMapping("/addtask")
    public String writeTodoTask(){
        return "/todotask/writetask";
    }

    @PostMapping("/addtask")
    public ModelAndView insertTodoTask(TodoTaskVO tdt){
        int groupNo = todoService.getUserGroupNo(tdt.getTdt_writer()); // =userNo
        tdt.setTdt_groupNo(groupNo);
        int tdtNo = todoService.insertTodoTask(tdt);    //auto increment 된 task의 key값 받아옴
        // task를 생성할때 생성한 본인은 참여하도록 u-t 테이블에 insert하는 라인 추가
        tdt.setTdt_No(tdtNo);
        todoService.joinWriterToTask(tdt);
        ModelAndView view = new ModelAndView();
        view.addObject("tdtNo",tdtNo);
        view.addObject("status","inserted");
        view.setViewName("/todotask/taskredirect");
        return view;
    }

    @GetMapping("/edittask/{tdtNo}")
    public ModelAndView editTodoTask(@PathVariable int tdtNo,HttpSession session){
        ModelAndView view = new ModelAndView();
        if(todoService.getTaskByTdtNo(tdtNo).getTdt_writer() == getUserNo(session)){    //user = 목표의 작성자인지 확인
            view.addObject("tdt",todoService.getTaskByTdtNo(tdtNo));
            view.setViewName("/todotask/edittask");
        } else {
            view.addObject("status","editfalse");
            view.setViewName("/todotask/taskredirect");
        }
        return view;
    }

    @PostMapping("/edittask/{tdtNo}")
    public ModelAndView updateTodoTask(@PathVariable int tdtNo,TodoTaskVO tdt){
        //폼 화면에서 전송받은 수정된 todotask를 DB에 저장
        //todotask를 비활성화하는 기능을 포함(따로 delete 없음 - 기록을 남기기 위해 삭제 대신 비활성화 상태로 update함)
        //CONSD : 현재 목표 수정은 작성자만 가능>작성자가 그만둘 경우 고려x
        ModelAndView view = new ModelAndView();
        todoService.updateTodoTask(tdt);
        view.addObject("status","updated");
        view.setViewName("redirect:/todo/task/" + tdtNo);
        return view;
    }

    @RequestMapping("/grouplist")
    public ModelAndView selectGroupTodoTaskList(HttpSession session){
//      그룹 내 전체 task
        ModelAndView view = new ModelAndView();
        int groupNo = todoService.getUserGroupNo(getUserNo(session));
        view.addObject("groupTasks",todoService.getGroupTdtList(groupNo)); //writerName, task별 인원수도 포함
        view.addObject("status","group");
        view.setViewName("/todotask/grouplist");
        return view;
    }

    @RequestMapping("/grouplist/user")
    public ModelAndView selectUserTodoTaskList(HttpSession session){
        //grouplist에서, user가 참가중인 task만 표시하도록
        ModelAndView view = new ModelAndView();
        view.addObject("groupTasks",todoService.getUserTdtList(getUserNo(session))); //writerName, task별 인원수도 포함
        view.addObject("status","user");
        view.setViewName("/todotask/grouplist");    //grouplist와 동일한 view로
        return view;
    }

    @RequestMapping("/grouplist/other")
    public ModelAndView selectOtherTodoTaskList(HttpSession session){
        //grouplist에서, user가 참가중이 아닌 task만 표시하도록
        ModelAndView view = new ModelAndView();
        view.addObject("groupTasks",todoService.getOtherTdtList(getUserNo(session))); //writerName, task별 인원수도 포함
        view.addObject("status","other");
        view.setViewName("/todotask/grouplist");    //grouplist와 동일한 view로
        return view;
    }

    @RequestMapping("/dailylist")
    public ModelAndView selectDailyTodoTaskList(HttpSession session){
        //TODO : 자신이 참여중인 일일 todotask의 화면(레코드 등록쪽으로 연결)
        //       오늘 날짜로 date 찍힌 새로운 record 생성 or 이미 생성된 record 목록 get->task 클릭시
        ModelAndView view = new ModelAndView();
        view.addObject("myTasks",todoService.getMyTasks(getUserNo(session)));
        view.addObject("groupNo",todoService.getUserGroupNo(getUserNo(session)));
        view.setViewName("/todotask/mytask");
        return view;
    }

    @PostMapping("/dailycomplete")
    public ModelAndView dailyTaskComplete(TodoRecordVO tdr) {
        ModelAndView view = new ModelAndView();
        // tdtNo+hidden form으로 tdr형태로 도착
        todoService.insertTodoRecord(tdr);
        view.setViewName("redirect:/todo/dailylist");
        return view;
    }

    @RequestMapping("/task/{tdtNo}")
    public ModelAndView taskDetail(@PathVariable int tdtNo,HttpSession session){
        //tdt_No에 따른 todo의 상세 페이지

        ModelAndView view = new ModelAndView();
        TodoDetail tdt = todoService.getTaskByTdtNo(tdtNo);
        if(tdt.getTdt_public() == 1 || tdt.getTdt_groupNo() == todoService.getUserGroupNo(getUserNo(session))){
            view.addObject("tdt",tdt);
            //tdt에서 groupNo를 추출해 groupName을 불러들이기
            //참가중인 멤버 리스트를 user_todo에서 불러오기
            view.addObject("members",todoService.getTaskMembers(tdtNo));
            //현재 회원이 참가중인지?
            view.addObject("alreadyMember",todoService.alreadyMember(tdtNo,getUserNo(session)));
            view.addObject("status","readable");
            view.setViewName("/todotask/taskdetail");
            }
        else{
            view.addObject("status","private");
            view.setViewName("/todotask/taskdetail");
        }
        return view;
    }

    @PostMapping("/task/join")
    public ModelAndView joinTask(UserTd ut, HttpSession session){
        //  task/{tdtNo} 등에서 목표에참여신청 할 경우 처리
        // 넘어올때 hidden form에서 userNo, groupNo, tdtNo, 받기
        ModelAndView view = new ModelAndView();
        todoService.joinUserToTask(ut); //관계성 테이블에 넣기
        view.addObject("tdtNo",ut.getTdtNo());
        view.addObject("status","joinCompleted");
        view.setViewName("/todotask/taskredirect");
        return view;
    }

    @PostMapping("/task/quit")
    public ModelAndView quitTask(UserTd ut, HttpSession session){
        //join과 유사한 처리, delete를 실행
        ModelAndView view = new ModelAndView();
        todoService.quitUserFromTask(ut);
        view.addObject("tdtNo",ut.getTdtNo());
        view.addObject("status","quitCompleted");
        view.setViewName("/todotask/taskredirect");
        return view;
    }

    public int getUserNo(HttpSession session){
        return Integer.parseInt(String.valueOf(session.getAttribute("userNo")));
    }

}


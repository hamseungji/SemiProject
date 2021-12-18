package com.help.project.controller;

import java.io.IOException;
import java.sql.Array;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.help.member.model.vo.Member;
import com.help.project.model.service.ProjectService;
import com.help.project.model.vo.Project;

/**
 * Servlet implementation class SelectProjectMain
 */
@WebServlet("/project/select.do")
public class SelectProjectMainServlet extends HttpServlet {
	//My Project List버튼 누르면 들어오는 첫번째 화면 
	//로그인 후 들어오는 첫번째 화면 
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectProjectMainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//화면 전환용 
		
		//로그인한 사원이 참여중인 모든 프로젝트 
		HttpSession session=request.getSession();
		Member loginMember=(Member)session.getAttribute("loginMember");
		
		String memId=loginMember.getMemberId();//로그인한 아이디
		List<Project> join= new ProjectService().selectJoin(memId);
		request.setAttribute("joinPro", join);

		//-----------------------------------------------참여자수 아직안함
//		int[] people=new int [join.size()];
//		
//		for(int i=0;i<join.size();i++) {
//			people[i]=join.get(i).getProjectNo();//로그인한 사람이 참가한 프로젝트번호들 구하기
//		}
		//로그인한 사원이 참가한 프로젝트의..참여자 숫자 구하기
	//	int joinNum=new ProjectService().joinProjectNumber(people,memId);
		
		HashMap<Integer, Integer> peopleNum= new HashMap<>(join.size());//
		for(int i=0;i<join.size();i++) {
			peopleNum.put(join.get(i).getProjectNo(),i);//프로젝트번호,임의의값들 추가
		}
		HashMap<Integer,Integer> numResult=new ProjectService().selectJoinNumber(peopleNum);
		request.setAttribute("joinNum",numResult);//키값:플젝번호 밸류:참여자수
		
		
		
		request.getRequestDispatcher("/views/project/myProjectView.jsp").forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

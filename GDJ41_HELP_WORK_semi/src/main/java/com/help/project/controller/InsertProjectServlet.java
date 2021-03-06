package com.help.project.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.help.member.model.vo.Member;
import com.help.project.model.service.ProjectService;
import com.help.project.model.vo.ProMemberJoinMember;
import com.help.project.model.vo.Project;
import com.help.project.model.vo.ProjectAddMember;

/**
 * Servlet implementation class InsertProjectServlet
 */
@WebServlet(name="insertProject" , urlPatterns = "/project/insertProject.do")
public class InsertProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertProjectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Project p = Project.builder()
					.proName(request.getParameter("proName"))
					.proExplain(request.getParameter("proExplain"))
					.proCommonYn(request.getParameter("proCommonYn")=="on"?"Y":"N")
					.memberId(request.getParameter("memberId")).
					build();
		
		int result = new ProjectService().insertProject(p);
		String memberId = request.getParameter("memberId");
		
		if(result>0) {
			
			//프로젝트 생성이 완료되면 해당 프로젝트 정보를 받아온당
			Project pinfo = new ProjectService().selectProjectNewinsert(memberId);
			
			//생성 완료시 프로젝트 생성자는 프로젝트 참여자 테이블로 들어가야함
			new ProjectService().insertProMemberCreator(pinfo);
			
			//생성 완료시 프로젝트 번호 가져옴
			int projectNo = new ProjectService().selectProjectNo(pinfo);
			List<ProMemberJoinMember> mList = new ProjectService().selectProjectJoinMemberList(projectNo);
				request.setAttribute("ProMemberJoinMember", mList);
				request.setAttribute("projectInfo", pinfo);
				request.getRequestDispatcher("/views/project/projectDetailView.jsp?").forward(request, response);
			
			
		}else {
			System.out.println("프로젝트 생성 실패");
			request.getRequestDispatcher("/project/selectProjectMain.do").forward(request, response);
		}
	
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

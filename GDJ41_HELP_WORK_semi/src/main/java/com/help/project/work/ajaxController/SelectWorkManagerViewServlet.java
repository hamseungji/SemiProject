package com.help.project.work.ajaxController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.help.project.model.service.ProjectService;
import com.help.project.model.vo.Project;
import com.help.project.work.model.service.WorkService;
import com.help.project.work.model.vo.Work;
import com.help.project.work.model.vo.WorkSelectManagerJoin;

/**
 * Servlet implementation class SelectWorkManagerViewServlet
 */
@WebServlet("/work/SelectWorkManagerViewServlet.do")
public class SelectWorkManagerViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectWorkManagerViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //본인 업무 전체조회
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String logId=request.getParameter("logId");//로그인한아이디
		//System.out.println(logId);
		List<Project> pro=new ProjectService().selectJoin(logId);//로그인한사람이 속한 플젝정보들
		
		int cPage;
		try {
			cPage=Integer.parseInt(request.getParameter("cPage"));
		}catch(NumberFormatException e) {
			cPage=1;
		}
		int numPerPage=20;
		
		
		
		
	//	HashMap<Integer, List<Work>> myworks=new WorkService().selectWorkMine(pro,logId);//플젝번호-해당업무글들
		List<WorkSelectManagerJoin> myworks=new WorkService().selectWorkMine(pro,logId);//플젝번호-해당업무글들
		int totalData=myworks.size();//전체 data개수
		
		
		List<WorkSelectManagerJoin> result=new WorkService().selectWorkMine(pro,logId,cPage,numPerPage);//페이징처리한 data
		int totalPage=(int)Math.ceil((double)totalData/numPerPage);
		int pageBarSize=5;
		int pageNo=((cPage-1)/pageBarSize)*pageBarSize+1;//시작
		int pageEnd=pageNo+pageBarSize-1;//끝
		//javascript:myWorkPaging
		
		
		String pageBar="<nav aria-label=\"Page navigation example\"><ul class=\"pagination\">";
		if(pageNo==1) {//이전
			pageBar+="<li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></li>";//이전
		}else {
			pageBar+="<li class=\"page-item\"><a class=\"page-link\"  href='javascript:myWorkPaging("+(pageNo-1)+");' aria-label=\"Previous\"><span aria-hidden=\"true\">&laquo;</span></a></li>";
		}
		while(!(pageNo>pageEnd||pageNo>totalPage)) {
			if(cPage==pageNo) {
				pageBar+="<li class=\"page-item\"><a class=\"page-link\" href=\"#\"><span>"+pageNo+"</span></a></li>";	
			}else {
				pageBar+="<li class=\"page-item\"><a class=\"page-link\"  href='javascript:myWorkPaging("+(pageNo)+");'>"+pageNo+"</a></li>";
			}
			pageNo++;
		}
		if(pageNo>totalPage) {//다음
			pageBar+="<li class=\"page-item\"><a class=\"page-link\" href=\"#\" aria-label=\"Next\"><span aria-hidden=\"true\">&raquo;</span></a></li>";	
		}else {
			pageBar+="<li class=\"page-item\"><a class=\"page-link\"  href='javascript:myWorkPaging("+(pageNo)+");' aria-label=\"Next\">"+"<span aria-hidden=\"true\">&raquo;</span>"+"</a></li>";
		}
		pageBar+="</ul></nav>";
		
		
		Map<String, Object> param=Map.of("pageBar",pageBar,"list",result);
		response.setContentType("application/json;charset=utf-8");
		new Gson().toJson(param,response.getWriter());
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

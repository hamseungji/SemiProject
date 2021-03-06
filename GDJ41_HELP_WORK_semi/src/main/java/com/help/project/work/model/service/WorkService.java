package com.help.project.work.model.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.help.common.JDBCTemplate.*;
import com.help.project.model.vo.Project;
import com.help.project.normal.model.vo.NormalComment;
import com.help.project.work.model.dao.WorkDao;
import com.help.project.work.model.vo.Work;
import com.help.project.work.model.vo.WorkComment;
import com.help.project.work.model.vo.WorkDetailJoin;
import com.help.project.work.model.vo.WorkSelectManagerJoin;

public class WorkService {
	private WorkDao dao=new WorkDao();
	
	public HashMap<Integer, List<Work>> selectWorkFive(List<Project> pro){
		//해당 프로젝트의 - 업무 게시글들 반환 (키:해당프로젝트번호/밸류:해당프로젝트의 업무게시글list)
		Connection conn = getConnection();
		HashMap<Integer, List<Work>> works=dao.selectWorkFive(conn,pro);
		close(conn);
		return works;
	}
	public List<WorkSelectManagerJoin> selectWorkMine(List<Project> pro,String logId){
		//내가 담당자인 업무들 조회
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> works=dao.selectWorkMine(conn,pro,logId);
		close(conn);
		return works;
		
	}
	public List<WorkSelectManagerJoin> selectWorkMine(List<Project> pro,String logId,int cPage,int numPerPage){
		//내가 담당자인 업무들 조회----페이징 
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> works=dao.selectWorkMine(conn,pro,logId,cPage,numPerPage);
		close(conn);
		return works;
		
	}
	
	public List<WorkSelectManagerJoin> searchMine(String ing,String prior,String h4,String logId,int cPage,int numPerPage){
		//다중 검색--내가 담당자인 업무들//페이징
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> result=new ArrayList<WorkSelectManagerJoin>();
		
		if(h4.equals("나의업무")) {
			 result=dao.searchMine(conn,ing,prior,logId);
			 System.out.println("서비스"+result);
		}else if(h4.equals("전체업무")) {
			//해야함
			 result=dao.searchAll(conn,ing,prior,logId,cPage,numPerPage);
			 System.out.println("서비스"+result);
			
		}
		return result;
		
	}
	
	public List<WorkSelectManagerJoin> searchMine(String ing,String prior,String h4,String logId){
		//다중 검색--내가 담당자인 업무들//
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> result=new ArrayList<WorkSelectManagerJoin>();
		
		if(h4.equals("나의업무")) {
			// result=dao.searchMine(conn,ing,prior);
			// System.out.println("서비스"+result);
		}else if(h4.equals("전체업무")) {
			//해야함
			List<Integer> proNum=dao.selectProjectNo(conn, logId);
			 result=dao.searchAll(conn,ing,prior,proNum);
			 System.out.println("서비스"+result);
			
		}
		return result;
		
	}
	public int insertWorkContent(Work w) {
		//업무 게시글 작성
		Connection conn = getConnection();
		int result = dao.insertWorkContent(conn,w);
		close(conn);

		return result;
	}

	public int selectWorkNo(Work w) {
		//등록된 업무 게시글 번호 가져오기
		Connection conn = getConnection();
		int workNo = dao.selectWorkNo(conn,w);
		close(conn);

		return workNo;
		
	}

	public int insertWorkFile(List<Map<String, Object>> fileList, int workNo) {
		//업무 파일 추가
		Connection conn = getConnection();

		int result = dao.insertWorkFile(conn, fileList,workNo);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return result;
		
	}

	public int insertWorkManager(List<Map<String, Object>> wmList) {
		//업무 담당자 추가
		Connection conn = getConnection();
		int result = dao.insertWorkManager(conn,wmList);
		
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);

		return result;
	}
	
	public List<Integer> selectProjectNo(String logId){
		//내가 참여한 프로젝트 번호들 
		Connection conn=getConnection();
		List<Integer> result= dao.selectProjectNo(conn,logId);
		close(conn);
		return result;
	}
	
	public List<WorkSelectManagerJoin> selectWorkAll(List<Integer> proNum){
		//내가 참여한 프로젝트의 모든 업무들 ---개수구하는데 사용 
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> result=dao.selectWorkAll(conn,proNum);
		close(conn);
		return result;
	}
	public List<WorkSelectManagerJoin> selectWorkAll(String id,int cPage,int numPerPage){
		//내가 참여한 프로젝트의 모든 업무들 ---페이징처리 
		Connection conn=getConnection();
		List<WorkSelectManagerJoin> result=dao.selectWorkAll(conn,id,cPage,numPerPage);
		close(conn);
		return result;
	}
	
	//Work All < 상세화면 >=======================================
	public WorkDetailJoin workDetailProject(int proNo,WorkDetailJoin temp) {
		//1) 프로젝트 번호 : 해당 플젝 제목 
		Connection conn=getConnection();
		WorkDetailJoin result=dao.workDetailProject(conn,proNo,temp);
		close(conn);
		return result;
	}
	public WorkDetailJoin workDetailWork( int workNo, WorkDetailJoin temp) {
		//2) 업무번호 : 업무 제목/내용/시작일/마감일/진행상태/우선순위
		Connection conn=getConnection();
		WorkDetailJoin result=dao.workDetailWork(conn,workNo,temp);
		close(conn);
		return result;
	}
	public WorkDetailJoin workDetailWriter(int workNo, WorkDetailJoin temp) {
		//3) 업무 작성자: id -->이름 변경
		Connection conn=getConnection();
		WorkDetailJoin result=dao.workDetailWriter(conn,workNo,temp);
		close(conn);
		return result;
	}
	public WorkDetailJoin workDetailManager(int workNo, WorkDetailJoin temp) {
		//4) 업무 담당자 : 여러명 O 
		Connection conn=getConnection();
		WorkDetailJoin result=dao.workDetailManager(conn,workNo,temp);
		close(conn);
		return result;
	}
	//======================================================================
	public int updateWorkContent(Work w,int contentNo) {
		// 업무게시글 수정
		Connection conn=getConnection();
		int result = dao.updateWorkContent(conn,w,contentNo);
		close(conn);

		return result ;
	}
	
	public void insertWorkComment(WorkComment wc) {
		
		//업무 댓글 추가
		Connection conn = getConnection();

		int result = dao.insertWorkComment(conn,wc);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
	}
	public List<WorkComment> selectWorkComment(int contentNo) {
		Connection conn = getConnection();
		
		List<WorkComment> wcList = dao.selectWorkComment(conn,contentNo);
		
		close(conn);
		
		return wcList;
	}
	public void deleteWorkComment(int contentNo) {
		Connection conn = getConnection();
		int result = dao.deleteWorkComment(conn,contentNo);
		
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
	}
	
	
	
	
}

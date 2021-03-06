package com.help.attendance.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.help.attendance.model.service.AttendanceService;
import com.help.attendance.model.vo.Attendance;
import com.help.member.model.vo.Member;

@WebServlet(name="attendanceList", urlPatterns = "/attendance/attendanceList.do")
public class AttendanceListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AttendanceListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		Member loginMember=(Member)session.getAttribute("loginMember");
		String memberId=loginMember.getMemberId();
		//최초 현재 월을 보여주기 위함
		String month=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));


		List<Attendance> list = new AttendanceService().selectAttendanceMonthly(memberId,month);

		request.setAttribute("attendanceMonthly", list);
		request.getRequestDispatcher("/views/attendance/attendanceList.jsp").forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

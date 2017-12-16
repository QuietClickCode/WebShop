package com.itheima.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.Order;
import com.itheima.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	
	public  void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid=request.getParameter("oid");
		AdminService service=new AdminService();
		List<Map<String,Object>> mapList=service.findOrderInfoByOid(oid);
		Gson gson=new Gson();
		String json=gson.toJson(mapList);
		System.out.println(json);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
		
		
	}
	public  void findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminService service=new AdminService();
		List<Order> orderList=service.findAllOrders();
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
		
		
	}
	public  void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//提供一个集合List<Category> 转成json字符串
		AdminService service=new AdminService();
		List<Category> categoryList=service.findAllCategory();
		Gson gson=new Gson();
		String json=gson.toJson(categoryList);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(json);
		
	}

	
}

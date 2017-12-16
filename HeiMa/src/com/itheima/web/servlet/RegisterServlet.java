package com.itheima.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.MailUtils;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//获得表单数据
		Map<String,String[]> properties=request.getParameterMap();
		User user=new User();
		try {
			//自己指定一个类型转换器
			ConvertUtils.register(new Converter(){

				@Override
				public Object convert(Class clazz, Object value) {
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
					Date parse=null;
					try {
						parse=format.parse(value.toString());
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
					return parse;
				}
				
			}, Date.class);
			
			
			
			BeanUtils.populate(user, properties);
		} catch (IllegalAccessException | InvocationTargetException e) {
			
			e.printStackTrace();
		}
		
		//private String uid;
		user.setUid(CommonsUtils.getUUID());
		//private String telephone;
		user.setTelephone(null);
		//private int state;
		user.setState(0);
		//private String code;
		String activeCode=CommonsUtils.getUUID();
		user.setCode(activeCode);

		//将user传递给service层
		UserService service=new UserService();
		boolean isRegistSuccess=service.regist(user);
		
		//是否注册成功
		if(isRegistSuccess){
			//发送邮件
			String emailMsg="恭喜您注册成功，请点击下面的链接进行激活账户<a href='http://mysite.s1.natapp.cc/HeiMa/active?activeCode="+activeCode+"'>"
					+"http://mysite.s1.natapp.cc/HeiMa/active?activeCode="+activeCode+"</a>";
			try {
				MailUtils.sendMail(user.getEmail(), emailMsg);
			} catch (MessagingException e) {
				
				e.printStackTrace();
			}
			//跳转到注册成功页面
			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		}else{
			//跳转到失败的页面
			response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
		}
		
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	

}

package com.itheima.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.itheima.domain.Category;
import com.itheima.domain.Product;
import com.itheima.service.AdminService;
import com.itheima.utils.CommonsUtils;

public class AdminAddProductServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//收集表单数据,封装一个Product实体,将上传图片存到服务器磁盘上
		Product product=new Product();
		Map<String,Object> map=new HashMap<String,Object>();
		
		DiskFileItemFactory factory=new DiskFileItemFactory();
		ServletFileUpload upload=new ServletFileUpload(factory);
		try {
			List<FileItem> parseRequest=upload.parseRequest(request);
			for(FileItem item:parseRequest){
				boolean formField = item.isFormField();
				if(formField){
					String fieldName=item.getFieldName();
					String fieldValue=item.getString("UTF-8");
					map.put(fieldName,fieldValue);
				}else{
					String fileName=item.getName();
					String path=this.getServletContext().getRealPath("upload");
					InputStream in=item.getInputStream();
					OutputStream out=new FileOutputStream(path+"/"+fileName);
					IOUtils.copy(in, out);
					in.close();
					out.close();
					item.delete();
					map.put("pimage", "upload/"+fileName);
					
				}
			}
			try {
				BeanUtils.populate(product, map);
				product.setPid(CommonsUtils.getUUID());
				product.setPdate(new Date());
				product.setPflag(0);
				Category category=new Category();
				category.setCid(map.get("cid").toString());
				product.setCategory(category);
				AdminService service=new AdminService();
				service.saveProduct(product);
				
			} catch (IllegalAccessException | InvocationTargetException e) {
				
				e.printStackTrace();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		} catch (FileUploadException e) {
			
			e.printStackTrace();
		}
		
				
		
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request,response);
	}
	
}


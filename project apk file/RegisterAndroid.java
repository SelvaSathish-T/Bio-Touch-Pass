package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.simple.JSONObject;


public class RegisterAndroid extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String phone = request.getParameter("phone");
		
		String[] detail;
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		
		OwnerPojo op=new OwnerPojo();
		op.setUsername(username);
		op.setEmail(email);
		op.setPassword(password);
		op.setPhone(phone);
		
		try {
			
			boolean emailexists=FileDao.ownerCheck(op);
			if(emailexists==true)
			{
				jsonObject.put("error", "false");
				jsonObject.put("message", "email already exist");
			}
			else
			{
				int status=FileDao.ownerSave(op);
				if(status>0)
				{
					String userdetails=FileDao.LoginDetails(email);
					detail = userdetails.split(",");
					jsonObject2.put("username", detail[0]);
					jsonObject2.put("email", detail[1]);
					jsonObject2.put("password", detail[2]);
					jsonObject2.put("phone", detail[3]);
					
					jsonObject.put("error", "false");
					jsonObject.put("message", "Registration Successfull");
					jsonObject.put("user", jsonObject2);
				}
				else {
					jsonObject.put("error", "true");
					jsonObject.put("message", "Registration faild");
				}
				
			}
			out.print(jsonObject);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
		
	
	
	}


}

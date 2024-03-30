package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class LoginAndroid extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email=request.getParameter("username");
		String password=request.getParameter("password");
		String[] detail;
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		
		OwnerPojo olog=new OwnerPojo();
		olog.setEmail(email);
		olog.setPassword(password);
		try {
			
			String status=FileDao.ownerLogin(olog);
			if(!status.equals("no"))
			{
				
				String userdetails=FileDao.LoginDetails(email);
				detail = userdetails.split(",");
				
				jsonObject2.put("username", detail[0]);
				jsonObject2.put("email", detail[1]);
				jsonObject2.put("password", detail[2]);
				jsonObject2.put("phone", detail[3]);
				
				jsonObject.put("error", "false");
				jsonObject.put("message", "Login Successfull");
				jsonObject.put("user", jsonObject2);
				
				
			}
			else
			{
				jsonObject.put("error", "true");
				jsonObject.put("message", "Login failed");
			}
			out.print(jsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}


}

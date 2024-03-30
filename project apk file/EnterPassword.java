package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class EnterPassword extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String uid= request.getParameter("uid");
		String[]pass;
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		try {
			String passstr=FileDao.password(uid);
			if(!passstr.equalsIgnoreCase("")){
			System.out.println("------"+passstr);
			pass = passstr.split(",");
			jsonObject2.put("password", pass[0]);
			jsonObject2.put("stroke", pass[1]);
			jsonObject.put("error", "false");
			jsonObject.put("message", "password");
			jsonObject.put("user",jsonObject2);
			
			}
			else {
				jsonObject.put("error", "true");
				jsonObject.put("message", "password not registered");
			}
			out.print(jsonObject);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	
	}


}

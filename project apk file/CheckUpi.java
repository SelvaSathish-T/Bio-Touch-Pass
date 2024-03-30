package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CheckUpi extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String phone = request.getParameter("phone");
		OwnerPojo ownerPojo = new OwnerPojo();
		ownerPojo.setPhone(phone);
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject users = new JSONObject();
		
		
		try {
			boolean upiexists=FileDao.upiCheck(ownerPojo);
			if(upiexists==true)
			{
				users.put("upiid",phone+"@okbank");
				jsonObject.put("error", "false");
				jsonObject.put("message", "upiid");
				jsonObject.put("users",users);
				//out.print("");
				
			}
			else {
				jsonObject.put("error", "true");
				jsonObject.put("message", "noupi");
			}
			out.print(jsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	
	}


}

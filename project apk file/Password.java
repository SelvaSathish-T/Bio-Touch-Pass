package logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;


public class Password extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String stroke = request.getParameter("stroke");
		String upiid = phone+"@okbank";
		
		JSONObject obj = new JSONObject();
		
		OwnerPojo op=new OwnerPojo();
		op.setPhone(phone);
		op.setPassword(password);
		op.setStroke(stroke);
		op.setUpiid(upiid);
		
		try {
			boolean emailexists=FileDao.ownerCheck(op);
			if(emailexists==true)
			{
				obj.put("error", "true");
				obj.put("message", "upi id generated");
				//out.print("");
				
			}
			else
			{
				int status=FileDao.ownerSave(op);
				if(status>0)
				{
					obj.put("error", "false");
					obj.put("message", "Register Success");
				}
				else {
					obj.put("error", "true");
					obj.put("message", "Error");
				}
				
			}
			out.print(obj);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		out.close();
	}

	}



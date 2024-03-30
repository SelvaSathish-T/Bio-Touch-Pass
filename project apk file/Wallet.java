package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import sun.awt.SunHints.Value;

public class Wallet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		System.out.println("====="+email);
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		String[] values;
		try {
			String data = FileDao.wallet(email);
			values = data.split(",");
			System.out.println("-----"+values[0]);
			if(!values[0].equalsIgnoreCase("")){
			jsonObject.put("acccountno", values[0]);
			jsonObject.put("mailid", values[1]);
			jsonObject.put("name", values[2]);
			jsonObject.put("amount", values[3]);
			jsonObject2.put("user", jsonObject);
			jsonObject2.put("error", "false");
			jsonObject2.put("message", "wallet");
			}
			else {
				jsonObject2.put("error", "false");
				jsonObject2.put("message", "error");
			}
			
			out.print(jsonObject2);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}

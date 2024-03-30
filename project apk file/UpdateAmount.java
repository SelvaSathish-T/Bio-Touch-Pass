package logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class UpdateAmount extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String accountno = request.getParameter("accountno");
		String amount = request.getParameter("rem");
		JSONObject jsonObject = new JSONObject();
		
		FilePojo ftype = new FilePojo();
		ftype.setAmount(amount);
		ftype.setAccountno(accountno);
	
		try {
			int uamount = FileDao.updateamount(ftype);
			if(uamount>0){
				jsonObject.put("error", "false");
				jsonObject.put("message", "amount updated");
			}
			else {
				jsonObject.put("error", "true");
				jsonObject.put("message", "error");
			}
			out.print(jsonObject);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}

package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.FileDao;



public class FileDao
{
	public static Connection getConnection(){  
        Connection con=null;  
        try{  
            Class.forName("com.mysql.jdbc.Driver");  
            con=DriverManager.getConnection("jdbc:mysql://localhost/bankadmindb", "root", "root");  
            //System.out.println("------------CLOUD USER CREATED------------");
        }catch(Exception e){System.out.println(e);}  
        return con;  
    } 
	
	
	
	 public static boolean ownerCheck(OwnerPojo op) throws SQLException
	 {
		 boolean emailexists=false;
		 Connection con=FileDao.getConnection();
		 String query="select * from password where phone = ?";
		 PreparedStatement st = con.prepareStatement(query);
		 st.setString(1, op.getPhone());
		 ResultSet rs=st.executeQuery();
		 if(rs.next()) {
		   emailexists = true;
		 }
		 
		return emailexists;
		 
	 }
	 public static boolean upiCheck(OwnerPojo op) throws SQLException
	 {
		 boolean emailexists=false;
		 Connection con=FileDao.getConnection();
		 String query="select * from password where phone = ?";
		 PreparedStatement st = con.prepareStatement(query);
		 st.setString(1, op.getPhone());
		 ResultSet rs=st.executeQuery();
		 if(rs.next()) {
		   emailexists = true;
		 }
		 
		return emailexists;
		 
	 }
	 
	  public static int ownerSave(OwnerPojo op){  
	        int status=0;  
	        try{  
	            Connection con=FileDao.getConnection();  
	            String query="insert into password(phone,password,stroke,upiid) values (?,?,?,?)";
	            PreparedStatement ps=con.prepareStatement(query);  
	            ps.setString(1, op.getPhone());  
	            ps.setString(2, op.getPassword());  
	            ps.setString(3, op.getStroke());
	            ps.setString(4, op.getUpiid());
	            status=ps.executeUpdate();  
	              
	            con.close();  
	        }catch(Exception ex){ex.printStackTrace();}  
	          
	        return status;  
	    }  
	  
	  public static int updateamount(FilePojo filePojo){  
	        int status=0;  
	        try{  
	            Connection con=FileDao.getConnection();  
	            String query="update useraccountinfo set amount=? where accountno=?";
	            PreparedStatement ps=con.prepareStatement(query);
	            ps.setString(1, filePojo.getAmount());
	            ps.setString(2, filePojo.getAccountno());
	            status=ps.executeUpdate();  
	            con.close();  
	        }catch(Exception ex){ex.printStackTrace();}  
	          
	        return status;  
	    }
	  
	  public static String password(String uid) throws SQLException
		 {
			 String upassword="";
			 String ustroke="";
			 Connection con=FileDao.getConnection();
			 String query="select * from password where phone=?";
			 PreparedStatement st = con.prepareStatement(query);
			 st.setString(1, uid);
			 ResultSet rs=st.executeQuery();
			 if(rs.next()) {
			   upassword=rs.getString("password");
			   ustroke=rs.getString("stroke");
			 }
			 
			return upassword+","+ustroke;
		 }
	  
	  public static String wallet(String email) throws SQLException
		 {
		  System.err.println("fdfdfsdf");
			 String acccountno="";
			 String mailid="";
			 String name="";
			 String amount="";
			 Connection con=FileDao.getConnection();
			 String query="SELECT userinfo.accountno,userinfo.mailid,userinfo.accountholdername,useraccountinfo.amount FROM userinfo INNER JOIN useraccountinfo ON userinfo.accountno = useraccountinfo.accountno WHERE userinfo.mailid = ?";
			 PreparedStatement st = con.prepareStatement(query);
			 st.setString(1, email);
			 ResultSet rs=st.executeQuery();
			 if(rs.next()) {
				 acccountno=rs.getString("accountno");
				 mailid=rs.getString("mailid");
				 name=rs.getString("accountholdername");
				 amount=rs.getString("amount");
			 }
			 
			return acccountno+","+mailid+","+name+","+amount;
		 }




	
	  
	 
	
	
}


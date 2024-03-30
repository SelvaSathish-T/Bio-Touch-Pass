package com.example.handwrittenpassword;

public class URLs {

    private static final String ROOT_URL = "http://"+IpAddress.Ip_Address+"/biotouchpass/Api.php?apicall=";
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";

    public static final String WALLET = "http://"+IpAddress.Ip_Address+":8888/BiotouchPass/Wallet";
    public static final String PASSWORD_INSERT = "http://"+IpAddress.Ip_Address+":8888/BiotouchPass/Password";
    public static final String CHECK_UPI = "http://"+IpAddress.Ip_Address+":8888/BiotouchPass/CheckUpi";
    public static final String UPDATE_AMOUNT = "http://"+IpAddress.Ip_Address+":8888/BiotouchPass/UpdateAmount";
    public static final String VERIFY_PASSWORD = "http://"+IpAddress.Ip_Address+":8888/BiotouchPass/EnterPassword";
   // http://localhost:8888/BiotouchPass/UpdateAmount
   // http://localhost:8888/BiotouchPass/EnterPassword
   // public static final String VERIFY_PASSWORD = "http://"+IpAddress.Ip_Address+"/BioTouchPass/RetrievePassword.php";

}

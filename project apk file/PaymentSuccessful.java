package com.example.handwrittenpassword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.handwrittenpassword.startup.MainActivity;
import com.example.handwrittenpassword.utility.ImageUrlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentSuccessful extends AppCompatActivity {
    TextView message;
    Button shopping;

    String total;
    String uemail;
    int aamount;
    int uamount;
    int remaining;
    String accountno;
    int amount;
   // String[] ammacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        SharedPreferences sharedPreferences1 = getSharedPreferences("mypref",MODE_PRIVATE);
        uemail = sharedPreferences1.getString("email",null);
        accountno = sharedPreferences1.getString("accountno",null);
        amount = Integer.parseInt(sharedPreferences1.getString("ammount",null));
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();

        imageUrlUtils.empty();
        MainActivity.notificationCountCart=0;

        SharedPreferences sharedPreferences = getSharedPreferences("pre",MODE_PRIVATE);
        total = sharedPreferences.getString("total",null);

       // uamount = Integer.parseInt(insert());
       // ammacc = insert().split("@");

        remaining = amount-Integer.parseInt(total);
        updateamount(accountno);
        /*for(int i=0;i<=MainActivity.notificationCountCart;i++){
            imageUrlUtils.removeCartListImageUri(i);
            imageUrlUtils.removeCartListCost(i);
            imageUrlUtils.removeCartListDesc(i);

        }*/

        message = findViewById(R.id.textView12);
        shopping = findViewById(R.id.button8);
       // amount = "100";

        message.setText("Your payment of ₹"+total+" was successfully completed");

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

/*    public  String insert(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.WALLET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.e("test",response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = obj.getJSONObject("user");
                                accountno = userJson.getString("acccountno");
                                aamount = userJson.getString("amount");

                                SharedPreferences sharedPreferences = getSharedPreferences("mpref",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("acc",accountno);
                                editor.apply();

                                String mailid = userJson.getString("mailid");
                                String aname = userJson.getString("name");



                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", uemail);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        return aamount+"@"+accountno;
    }*/
    public  void updateamount(final String account){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.UPDATE_AMOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.e("test",response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accountno", account);
                params.put("rem", String.valueOf(remaining));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}

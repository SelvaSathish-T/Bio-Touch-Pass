package com.example.handwrittenpassword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handwrittenpassword.startup.MainActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    public EditText email,password;
    public Button login;
    public TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etLogGmail);
        password = findViewById(R.id.etLoginPassword);

        login = findViewById(R.id.btnLogin);

        register = findViewById(R.id.tvRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginandroid();

            }
        });
    }

    public void loginandroid(){
        final String username = email.getText().toString();
        final String spassword = password.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(spassword)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
                                SharedPreferences.Editor editor  = sharedPreferences.edit();
                                editor.putString("phone",userJson.getString("phone"));
                                editor.putString("email",userJson.getString("email"));
                                Log.e("phone",userJson.getString("phone"));
                                editor.apply();
                                //creating a new user object
                               /* User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("gender")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity*/
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                params.put("username", username);
                params.put("password", spassword);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}

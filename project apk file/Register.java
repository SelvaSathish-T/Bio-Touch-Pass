package com.example.handwrittenpassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public EditText name,phone,email,password;
    public Button register,login;
   // public TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.etRegName);
        phone = findViewById(R.id.etRegPhone);
        email = findViewById(R.id.etRegGmail);
        password = findViewById(R.id.etRegPassword);

        register = findViewById(R.id.btnRegLogin);
        login = findViewById(R.id.btnGotoLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void register(){
        final String susername = name.getText().toString().trim();
        final String semail = email.getText().toString().trim();
        final String spassword = password.getText().toString().trim();
        final String sphone = phone.getText().toString().trim();

        //final String phone = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations

        if (TextUtils.isEmpty(susername)) {
            name.setError("Please enter username");
            name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(semail)) {
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sphone)) {
            phone.setError("Please enter your email");
            phone.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(sphone).matches()) {
            phone.setError("Enter a valid email");
            phone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(spassword)) {
            password.setError("Enter a password");
            password.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
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



                               /* //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email"),
                                        userJson.getString("gender")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
*/
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), Login.class));
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
                params.put("username", susername);
                params.put("email", semail);
                params.put("password", spassword);
                params.put("phone", sphone);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    }


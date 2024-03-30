package com.example.handwrittenpassword;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.williamww.silkysignature.views.SignaturePad;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterPassword extends AppCompatActivity {

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    Bitmap signatureBitmap;
    int strokeCount =0;
    int count = 0;
    String ss;
    String ps;
    String ts;
    public int counter;
    // int count = 0;
    //int time;
    int timer;
    //ArrayList<String> isnum;
    ArrayList<String>isnum;

    /*HashMap<Integer,String> password;
    HashMap<Integer,String> stroke;*/

    public ArrayList<Integer> stroke;
    public ArrayList<Integer> time;
    public ArrayList<String> password;
    public TextView clock;
    public String uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        uemail = sharedPreferences.getString("phone",null);
        Log.e("test",uemail);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        clock = (TextView)findViewById(R.id.counter);
        stroke = new ArrayList<>();
        password = new ArrayList<>();
        time = new ArrayList<>();
        isnum = new ArrayList<>();
        isnum.add("1");
        isnum.add("2");
        isnum.add("3");
        isnum.add("4");
        isnum.add("5");
        isnum.add("6");
        isnum.add("7");
        isnum.add("8");
        isnum.add("9");
        isnum.add("0");
        //  startClock();



        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                strokeCount++;
                // Toast.makeText(MainActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);

            }
            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                strokeCount=0;


            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (signatureBitmap != null) {
                    runTextRecognition();
                    mSignaturePad.clear();


                } else {
                    showToast("Choose a proper image");
                }

            }
        });


    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(signatureBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                .getVisionTextDetector();
        detector.detectInImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {

                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception

                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText texts) {

        StringBuilder t = new StringBuilder();

        List<FirebaseVisionText.Block> blocks = texts.getBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            strokeCount=0;
            counter=0;
            new CountDownTimer(500000,2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    counter =0;
                    Log.e("test",String.valueOf(counter));
                    timer = counter;
                    counter++;
                }
                @Override
                public void onFinish() {
                    //counttime.setText("Finished");
                }
            }.start();
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            t.append(" ").append(blocks.get(i).getText());
        }
        showToast(t.toString());
        showToast(String.valueOf(strokeCount));

        if(!isnum.contains(t.toString().trim())){
            Toast.makeText(getApplicationContext(),"Please enter number from 0-9",Toast.LENGTH_SHORT).show();


            return;

        }
        password.add(t.toString());
        clock.setText(t.toString().trim());
        stroke.add(strokeCount);
        //  time.add(counter);


        if(count == 3){
            ss ="";

            ps ="";



            for(Integer integer:stroke){
                ss = ss+integer+"@";
            }

            for(String pass:password){
                ps = ps+pass+"@";
            }
            /*for(Integer tim:time){
                ts = ts+tim+"@";
            }*/

            ss= ss.substring(0, ss.length() - 1);
            /*ts= ts.substring(0, ts.length() - 1);*/
            ps = ps.substring(0, ps.length() - 1);
            ps = ps.replaceAll("\\s", "");

            insert(ps,ss);
           // Log.e("test",ts);
            finish();
        }
        count++;
        strokeCount = 0;
        counter=0;

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public  void insert(final String password, final String stroke){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.PASSWORD_INSERT,
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
                params.put("phone", uemail);
                params.put("password", password);
                params.put("stroke", stroke);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


}

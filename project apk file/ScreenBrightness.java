package com.example.handwrittenpassword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class ScreenBrightness extends AppCompatActivity {
    ArrayList<Integer>numbers;
    Button button;
    public int rand_int1;
    Boolean ans;
    Random random;
    String[]rannumbers;
    int bright = 0;

    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    Bitmap signatureBitmap;

    int count = 0;
    //public ArrayList<Integer> stroke;
    //public ArrayList<String> password;
    String[] upassword;
    String[] ustroke;

    HashMap<Integer,String> password;
    HashMap<Integer,Integer> stroke;

    HashMap<Integer,Integer> hstroke;
    HashMap<Integer,String> hpassword;
    int brightcount = 0;
    int scount = 0;
    ArrayList<String>isnum;
    int strokeCount =0;
    public String uemail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        FirebaseApp.initializeApp(this);

        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        uemail = sharedPreferences.getString("phone",null);
        insert();

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);


        password = new HashMap<>();
        stroke = new HashMap<>();

        hstroke = new HashMap<>();
        hpassword = new HashMap<>();

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

        //--------------------- for random numbers-----------------------------
        numbers = new ArrayList<>();
        random = new Random();
        rannumbers = test().split("@");
        Log.e("tets",rannumbers[bright]);
        //-------------------------------------------------------------------
        settingPermission();

        if(rannumbers[bright].equalsIgnoreCase("2")){
            changeScreenBrightness(getApplicationContext(),10);
        }

        else if(rannumbers[bright].equalsIgnoreCase("4")){
            changeScreenBrightness(getApplicationContext(),10);
        }
        else {
            changeScreenBrightness(getApplicationContext(),120);
        }

/*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test",rannumbers[bright]);

                if(rannumbers[bright].equalsIgnoreCase("2")){
                    changeScreenBrightness(getApplicationContext(),10);
                }

                else if(rannumbers[bright].equalsIgnoreCase("4")){
                    changeScreenBrightness(getApplicationContext(),10);
                }
                else {
                    changeScreenBrightness(getApplicationContext(),120);
                }
                bright++;
            }
        });
*/



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
                    for (Map.Entry<Integer, String> entry : hpassword.entrySet()) {
                        Log.e("test2", "Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());
                    }

                } else {
                    showToast("Choose a proper image");
                }


            }
        });
    }

    private void changeScreenBrightness(Context context, int screenBrightnessValue)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);// Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue);
    }
    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
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

            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            t.append(" ").append(blocks.get(i).getText());
        }
        showToast(t.toString());
        showToast(String.valueOf(strokeCount));
        Log.e("brightcount", String.valueOf(brightcount));
        Log.e("err1",rannumbers[bright]);
        Log.e("err2",t.toString().trim());
        Log.e("err2",hpassword.get(brightcount));

        if(!isnum.contains(t.toString().trim())){
            Toast.makeText(getApplicationContext(),"Please enter number from 0-9",Toast.LENGTH_SHORT).show();

            return;

        }

        if(rannumbers[bright].equalsIgnoreCase("2")){
           // password.put(count,t.toString());
            Log.e("err",hpassword.get(brightcount));

            if(t.toString().trim().equalsIgnoreCase(hpassword.get(brightcount))) {
                Log.e("err1", hpassword.get(brightcount));
                password.put(count, t.toString().trim());
                stroke.put(count, strokeCount);
                //strokeCount=0;
                count++;
            }
        }
        else if(rannumbers[bright].equalsIgnoreCase("4")){
            if(t.toString().trim().equalsIgnoreCase(hpassword.get(brightcount))) {
                //password.put(count,t.toString());
                Log.e("err1", hpassword.get(brightcount));
                Log.e("err1", hpassword.get(brightcount));
                password.put(count, t.toString().trim());
                stroke.put(count, strokeCount);
                //strokeCount=0;
                count++;
            }
        }
        else {
            password.put(count,t.toString().trim());
            stroke.put(count,strokeCount);
            if(brightcount<3) {
                brightcount++;
            }
            count++;
            Log.e("err","hi");
        }
        scount++;
        strokeCount=0;

        if(bright<=4) {
            bright++;
            if (rannumbers[bright].equalsIgnoreCase("2")) {
                changeScreenBrightness(getApplicationContext(), 10);
            } else if (rannumbers[bright].equalsIgnoreCase("4")) {
                changeScreenBrightness(getApplicationContext(), 10);
            } else {
                changeScreenBrightness(getApplicationContext(), 120);
            }
        }
        if(scount == 6) {
            for (Map.Entry<Integer, String> entry : password.entrySet()) {
                Log.e("test1", "Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }
            for (Map.Entry<Integer, String> entry : hpassword.entrySet()) {
                Log.e("test2", "Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }

            for (Map.Entry<Integer, Integer> entry : hstroke.entrySet()) {
                Log.e("test3", "Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }
            for (Map.Entry<Integer, Integer> entry : stroke.entrySet()) {
                Log.e("test4", "Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }
            //Toast.makeText(getApplicationContext(),"finish",Toast.LENGTH_SHORT).show();
            Boolean test1 = hpassword.equals(password);
            Boolean test2 = hstroke.equals(stroke);
            if(test1&&test2){
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                changeScreenBrightness(getApplicationContext(),120);
                Intent intent = new Intent(getApplicationContext(),Success.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    public String test(){
        int[] randomSequence = new int[6];
        Random randomNumbers = new Random();

        for (int i = 0; i < randomSequence.length; i++ ) {
            if (i == 0) { // seed first entry in array with item 0
                randomSequence[i] = 0;
            } else { // for all other items...
                // choose a random pointer to the segment of the
                // array already containing items
                int pointer = randomNumbers.nextInt(i + 1);
                randomSequence[i] = randomSequence[pointer];
                randomSequence[pointer] = i;
                // note that if pointer & i are equal
                // the new value will just go into location i and possibly stay there
                // this is VERY IMPORTANT to ensure the sequence is really random
                // and not biased
            } // end if...else
        } // end for
        String ss ="";

        for (int number: randomSequence) {
            Log.e("test", String.valueOf(number));
            ss=ss+number+"@";
        } // end for
        ss = ss.substring(0, ss.length() - 1);
        return ss;

    }
    public  void insert(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.VERIFY_PASSWORD,
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

                                upassword = userJson.getString("password").split("@");
                                ustroke = userJson.getString("stroke").split("@");

                                int i=0;
                                int j=0;

                                for (String te:upassword){
                                    hpassword.put(i,te);
                                    i++;
                                }

                                for (String in:ustroke){
                                    hstroke.put(j, Integer.valueOf(in));
                                    Log.e("ted", String.valueOf(hstroke));
                                    j++;
                                }


                                /*upassword= new ArrayList(Arrays.asList(userJson.getString("password").split("@")));
                                ustroke= new ArrayList(Arrays.asList(userJson.getString("stroke").split("@")));*/


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
                params.put("uid", uemail);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
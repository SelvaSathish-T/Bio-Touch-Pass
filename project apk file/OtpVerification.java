package com.example.handwrittenpassword;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.williamww.silkysignature.views.SignaturePad;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtpVerification extends AppCompatActivity {
    String[]Otp;
    HashMap<Integer,String>Omap;
    int Count = 0;
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    Bitmap signatureBitmap;
    int strokeCount =0;
    int count = 0;
    //public ArrayList<Integer> stroke;
    //public ArrayList<String> password;
    String[] upassword;
    String[] ustroke;

    HashMap<Integer,Integer> stroke;
    HashMap<Integer,String> password;

    HashMap<Integer,Integer> hstroke;
    HashMap<Integer,String> hpassword;
    ArrayList<String>isnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        Omap = new HashMap<>();
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        stroke = new HashMap<>();
        password = new HashMap<>();
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



        Bundle bundle = getIntent().getExtras();
        String otp = bundle .getString("otp");
        Otp = otp.split("@");

        for (String test:Otp){
            Omap.put(Count,test);
            Count++;
            Log.e("12",test);
        }



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
        password.put(count,t.toString().trim());
        stroke.put(count,strokeCount);
        Log.e("test", String.valueOf(count));
        count++;
        strokeCount=0;

        if(count == 6) {
            Toast.makeText(getApplicationContext(),"tets",Toast.LENGTH_SHORT).show();

            for (Map.Entry<Integer,String> entry : password.entrySet()){
                Log.e("test1","Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }
            for (Map.Entry<Integer,String> entry : Omap.entrySet()){
                Log.e("test2","Key = " + entry.getKey() +
                        ", Value = " + entry.getValue());
            }


            Boolean check = password.equals(Omap);
            //Boolean check2 = stroke.equals(hstroke);

            if(check){
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),PaymentSuccessful.class);
                startActivity(intent);
                finish();
            }
           // Toast.makeText(getApplicationContext(),""+check2,Toast.LENGTH_SHORT).show();
        }
        // count = 0;
        /*password.clear();
        stroke.clear();*/
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

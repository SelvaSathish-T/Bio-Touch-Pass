package com.example.handwrittenpassword;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.libra.sinvoice.SinVoice;
import java.util.Random;


public class Success extends AppCompatActivity implements SinVoice.Listener {
    TextView textView;
    private int mSystemVolume;
    private int mSetVolume;
    private SinVoice mSinVoice;
    String otp;
    private boolean mIsReadFromFile;
    Button enter,resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        enter = findViewById(R.id.button5);
        resend = findViewById(R.id.button6);



        //textView = findViewById(R.id.textView);
        mIsReadFromFile = false;

//        textView.setText("Success");

        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(Success.this).setTitle(R.string.permission_lack_record)
                    .setMessage(R.string.permission_lack_record_info)
                    .setPositiveButton(R.string.command_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Success.this.finish();
                        }
                    }).setCancelable(false).show();

            return;
        } else {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mSystemVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSetVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2 + 1;
            am.setStreamVolume(AudioManager.STREAM_MUSIC, mSetVolume, 0);
        }
       // initEffect();


        mSinVoice = new SinVoice(this, this);
        otp = test();
        mSinVoice.send(otp);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),OtpVerification.class);
                intent.putExtra("otp",otp);
                startActivity(intent);
                finish();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = test();
                mSinVoice.send(otp);
            }
        });

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

    @Override
    public void onResume() {
        super.onResume();

        setVolume();
        if (null != mSinVoice) {
            mSinVoice.startListen(mIsReadFromFile);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (null != mSinVoice) {
            mSinVoice.stop();
        }

        restoreVolume();
    }

    private void setVolume() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int systemVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mSetVolume != systemVolume) {
            if (systemVolume != mSystemVolume) {
                mSystemVolume = systemVolume;
                mSetVolume = systemVolume;
            }
            am.setStreamVolume(AudioManager.STREAM_MUSIC, mSetVolume, 0);
        }
    }

    private void restoreVolume() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, mSystemVolume, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mSinVoice) {
            mSinVoice.destroy();
        }
    }

    @Override
    public void onSinVoiceSendStart() {
    }

    @Override
    public void onSinVoiceSendFinish() {

    }

    private void setState(String str1) {
        setState(str1, "");
    }

    private void setState(String str1, String str2) {

    }

    @Override
    public void onSinVoiceStartListen() {
        setState(getString(R.string.state_listening_sonic));

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSinVoiceStopListen() {
        setState(getString(R.string.state_stop_listening_sonic));
    }

    @Override
    public void onSinVoiceReceiveStart() {
        setState(getString(R.string.state_receiving_data));
    }
    @Override

    public void onSinVoiceReceiveSuccess(String text) {
        setState(getString(R.string.state_listening_sonic), getString(R.string.state_receiving_successful));
        // mTvRecognised.setText(text);
        Log.e("Information", "Received " + text);
    }

    @Override
    public void onSinVoiceReceiveFailed() {
        setState(getString(R.string.state_listening_sonic), getString(R.string.state_receiving_failed));

    }
}

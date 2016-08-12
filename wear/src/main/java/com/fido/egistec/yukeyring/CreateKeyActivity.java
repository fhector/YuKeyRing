package com.fido.egistec.yukeyring;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fido.egistec.fpservice.Command;
import com.fido.egistec.fpservice.FingerEnrollListener;
import com.fido.egistec.fpservice.YouKeyUWrapper;

import java.util.ArrayList;
import java.util.Arrays;

import egistec.fingerauth.api.FPAuthListeners;


/**
 * Created by delly on 6/15/15.
 */
public class CreateKeyActivity extends Activity implements FingerEnrollListener, FPAuthListeners.StatusListener {

    private static final String TAG = "CreateKeyActivity";

    public final static int CREATE_KEY_REQ = 0x1104;
    public static ArrayList<Integer> pListfp =
            new ArrayList<>(Arrays.asList(R.drawable.key_1, R.drawable.key_2,
                    R.drawable.key_3, R.drawable.key_4,
                    R.drawable.key_5, R.drawable.key_6,
                    R.drawable.key_7, R.drawable.key_8, R.drawable.key_8_p));
    public static ArrayList<Integer> pListfpp = new ArrayList<>(Arrays.asList(R.drawable.key_0_p, R.drawable.key_1_p, R.drawable.key_2_p, R.drawable.key_3_p,
            R.drawable.key_4_p, R.drawable.key_5_p, R.drawable.key_6_p, R.drawable.key_7_p));
    public static ArrayList<Integer> pListfpc = new ArrayList<>(Arrays.asList(R.drawable.key_1_p, R.drawable.key_2_p, R.drawable.key_3_p, R.drawable.key_4_p,
            R.drawable.key_5_p, R.drawable.key_6_p, R.drawable.key_7_p, R.drawable.key_8_p));

    private final String LOG_TAG = CreateKeyActivity.class.getName();
    private final int ENROLL_MAX_SIZE = 8;


    private final int PRESS_DOWN = 0;
    private final int PRESS_UP = 1;
    public static ImageButton mEnrollbtn;
    private int nCounter = 0;
    private boolean mCreated = false;
    private YouKeyUWrapper mUKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createkey_activity);

        mEnrollbtn = (ImageButton) findViewById(R.id.enrollmentbtn);
        /*
        mEnrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEnrollbtn.setImageDrawable(getResources().getDrawable(pListfp.get(nCounter % ENROLL_MAX_SIZE)));
                mEnrollbtn.invalidate();
            }
        });
        mEnrollbtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        mEnrollbtn.setImageDrawable(getResources().getDrawable(pListfp.get(nCounter % ENROLL_MAX_SIZE)));
                        mEnrollbtn.invalidate();
                        nCounter++;
                        if(nCounter==8) {
                            final Toast toast = Toast.makeText(getApplicationContext(), "success create...", Toast.LENGTH_SHORT);
                            toast.show();
                            new CountDownTimer(3000, 500) {
                                public void onTick(long m) {
                                }
                                public void onFinish() {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }.start();
                        }
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {

                        mEnrollbtn.setImageDrawable(getResources().getDrawable(pListfpp.get(nCounter % ENROLL_MAX_SIZE)));
                        mEnrollbtn.invalidate();
                        new CountDownTimer(500, 500) {
                            public void onTick(long m) {

                            }
                            public void onFinish() {
                                Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                                vib.vibrate(500);
                                mEnrollbtn.setImageDrawable(getResources().getDrawable(pListfpc.get(nCounter%ENROLL_MAX_SIZE)));
                                mEnrollbtn.invalidate();
                            }
                        }.start();
                        break;
                    }
                    default:
                        break;
                }
                return false;
            }
        });
        */

        nCounter = 0;
        mCreated = false;
        mUKey = YouKeyUWrapper.getInstance();
        mUKey.registerOnFingerFetch(this, this);
        mUKey.sendCommand(Command.REQUEST_ENROLL);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        nCounter = 0;
        mCreated = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        nCounter = 0;
        mCreated = false;
        super.onResume();
    }

    @Override
    public void onSuccess() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        mCreated = true;
        updateFPView();
    }

    @Override
    public void onFail() {
        Log.e("onFail", "录入指纹失败，重新尝试");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "录入指纹失败，重新尝试"));
//        Toast.makeText(CreateKeyActivity.this, "录入指纹失败，重新尝试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void waitForFingerOn() {
        Log.e("waitForFingerOn", "请按下手指");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "请按下手指"));
//        Toast.makeText(CreateKeyActivity.this, "请按下手指", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fingerCoveredPatialSensor() {
        Log.e("fingerCoveredPatial", "请将手指覆盖在传感器上");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "请将手指覆盖在传感器上"));
//        Toast.makeText(CreateKeyActivity.this, "请将手指覆盖在传感器上", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fingerUp() {
        Log.e("fingerUp", "请松开手指");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "请松开手指"));
//        Toast.makeText(CreateKeyActivity.this, "请松开手指", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeFinger() {
        Log.e("changeFinger", "换个手指试一下");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "换个手指试一下"));
    }

    @Override
    public void fingerLeftOrRight() {
        Log.e("fingerLeftOrRight", "请移动手指");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "请移动手指"));
    }

    @Override
    public void onProgress() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        updateFPView();
    }

    @Override
    public void onBadImage(int i) {

    }

    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onServiceDisConnected() {

    }

    @Override
    public void onFingerFetch() {

    }

    @Override
    public void onFingerImageGetted() {

    }

    @Override
    public void onUserAbort() {
        final Toast toast = Toast.makeText(getApplicationContext(), "canceled...", Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(3000, 500) {
            public void onTick(long m) {
            }

            public void onFinish() {
                setResult(RESULT_CANCELED);
                finish();
            }
        }.start();
    }

    @Override
    public void onStatus(int i) {

    }

    private void updateFPView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCreated == false) {
                    Log.e("process", nCounter + "");
                    mEnrollbtn.setImageDrawable(getResources().getDrawable(pListfp.get((nCounter++) % ENROLL_MAX_SIZE)));
                    mEnrollbtn.invalidate();
                } else {
                    nCounter = 0;
                    final Toast toast = Toast.makeText(getApplicationContext(), "success create...", Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(3000, 500) {
                        public void onTick(long m) {
                        }

                        public void onFinish() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }.start();
                }
            }
        });
    }

}

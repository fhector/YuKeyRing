package com.fido.egistec.yukeyring;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fido.egistec.fpservice.Command;
import com.fido.egistec.fpservice.SendingThread;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 创建
 */
public class CreateKeyActivity extends SerialPortActivity {

    private static final String TAG = "CreateKeyActivity";
    private SendingThread mSendingThread;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createkey_activity);
        mEnrollbtn = (ImageButton) findViewById(R.id.enrollmentbtn);
        nCounter = 0;
        mCreated = false;
        if (mSerialPort != null) {
            mSendingThread = new SendingThread(mOutputStream, Command.REQUEST_ENROLL);
            mSendingThread.start();
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        if (buffer[0] == (byte) 0x55 && buffer[1] == (byte) 0xaa) {
            if (buffer[2] == (byte) 0x81) {
                if (buffer[3] == (byte) 0x21) {
                    onProgress();
                } else if (buffer[3] == (byte) 0xff) {
                    onFail();
                } else if (buffer[3] == (byte) 0x24 && buffer[4] == (byte) 0x30) {
                    onSuccess(); // action result continue
                } else if (buffer[3] == (byte) 0x25) {
//                    mFingerEnrollListener.waitForFingerOn();
                } else if (buffer[3] == (byte) 0x28) {
//                    mFingerEnrollListener.fingerCoveredPatialSensor();
                } else if (buffer[3] == (byte) 0x23) {
//                    mFingerEnrollListener.fingerUp();
                } else if (buffer[3] == (byte) 0x20) {
//                    mFingerEnrollListener.changeFinger();
                } else if (buffer[3] == (byte) 0x19) {
//                    mFingerEnrollListener.fingerLeftOrRight();
                }
            }
            Log.e("buffer", byte2hex(buffer));
        }
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

    /*@Override
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
    }*/

    private void onSuccess() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        mCreated = true;
        updateFPView();
    }

    private void onFail() {
        Log.e("onFail", "录入指纹失败，重新尝试");
        runOnUiThread(new ToastRunnable(CreateKeyActivity.this, "录入指纹失败，重新尝试"));
    }

    private void onProgress() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        updateFPView();
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

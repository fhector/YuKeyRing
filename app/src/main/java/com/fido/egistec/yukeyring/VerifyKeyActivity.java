package com.fido.egistec.yukeyring;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fido.egistec.fpservice.Command;
import com.fido.egistec.fpservice.SendingThread;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by delly on 6/15/15.
 */
public class VerifyKeyActivity extends SerialPortActivity {

    public final static int VERIFY_KEY_REQ = 0x1103;

    private SendingThread mSendingThread;

    private final String LOG_TAG = CreateKeyActivity.class.getName();
    private final ArrayList<Integer> pListfp = new ArrayList<>(Arrays.asList(R.drawable.key_1, R.drawable.key_2, R.drawable.key_3, R.drawable.key_4,
            R.drawable.key_5, R.drawable.key_6, R.drawable.key_7, R.drawable.key_8_p));
    private final ArrayList<Integer> pListfpp = new ArrayList<>(Arrays.asList(R.drawable.key_0_p, R.drawable.key_1_p, R.drawable.key_2_p, R.drawable.key_3_p,
            R.drawable.key_4_p, R.drawable.key_5_p, R.drawable.key_6_p, R.drawable.key_7_p));
    private final ArrayList<Integer> pListfpc = new ArrayList<>(Arrays.asList(R.drawable.key_1_p, R.drawable.key_2_p, R.drawable.key_3_p, R.drawable.key_4_p,
            R.drawable.key_5_p, R.drawable.key_6_p, R.drawable.key_7_p, R.drawable.key_8_p));
    private final int ENROLL_MAX_SIZE = 8;

    private final int PRESS_DOWN = 0;
    private final int PRESS_UP = 1;
    private ImageButton mVerifybtn;
    private int nCounter = 0;

    private boolean mVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifykey_activity);

        mVerifybtn = (ImageButton) findViewById(R.id.verifybtn);
        if (mSerialPort != null) {
            mSendingThread = new SendingThread(mOutputStream, Command.REQUEST_VERIFY);
            mSendingThread.start();
        }
        mVerified = false;
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        Log.e("buffer", byte2hex(buffer));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*@Override
    public void onSuccess() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        mVerified = true;
        updateFPView();
    }

    @Override
    public void noFingerEnrolled() {
        runOnUiThread(new ToastRunnable(VerifyKeyActivity.this, "请先录入一个指纹吧"));
    }

    @Override
    public void waitForFingerOn() {
        runOnUiThread(new ToastRunnable(VerifyKeyActivity.this, "按下手指"));
    }

    @Override
    public void notAFingerPrint() {
        runOnUiThread(new ToastRunnable(VerifyKeyActivity.this, "不是一个指纹"));
    }

    @Override
    public void badFingerPrint() {
        runOnUiThread(new ToastRunnable(VerifyKeyActivity.this, "错误的指纹"));
    }

    @Override
    public void fingerUp() {
        runOnUiThread(new ToastRunnable(VerifyKeyActivity.this, "验证中，抬起手指"));
    }

    @Override
    public void onFail() {
        Vibrator vib = (Vibrator) getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(500);
        mVerified = false;
        updateFPView();
    }
*/
    private void updateFPView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mVerified == true) {
                    mVerifybtn.setImageDrawable(getResources().getDrawable(R.drawable.key_8_p));
                    mVerifybtn.invalidate();
                    final Toast toast = Toast.makeText(getApplicationContext(), "success verify", Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(1000, 500) {
                        public void onTick(long m) {
                        }

                        public void onFinish() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }.start();
                } else {
                    final Toast toast = Toast.makeText(getApplicationContext(), "please input again", Toast.LENGTH_SHORT);
                    toast.show();
                    if (mSerialPort != null) {
                        mSendingThread = new SendingThread(mOutputStream, Command.REQUEST_VERIFY);
                        mSendingThread.start();
                    }
                }
            }
        });
    }
}

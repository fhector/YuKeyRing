package com.fido.egistec.yukeyring;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fido.egistec.fpservice.Command;
import com.fido.egistec.fpservice.SendingThread;


public class MainActivity extends SerialPortActivity {

    private final String LOG_TAG = MainActivity.class.getName();

    private final static int GREEN_KEY = 0;
    private final static int YELLOW_KEY = 1;
    private final static int RED_KEY = 2;

    private Button createKeybtn;
    private Button verifyKeybtn;
    private long mLastClickTime = SystemClock.elapsedRealtime();

    static public boolean bHasKey = false;
    static public boolean bKeyVerified = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createKeybtn = (Button) findViewById(R.id.createkey);
        createKeybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold of 1000 ms

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                doCreateKey();
            }
        });

        verifyKeybtn = (Button) findViewById(R.id.verifykey);
        verifyKeybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold of 1000 ms

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                doVerifyKey();
            }
        });

        SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
        if (sp.getString("HAS_KEY", "false").compareToIgnoreCase("true") == 0) {
            MainActivity.bHasKey = true;
        } else {
            MainActivity.bHasKey = false;
        }
        if (sp.getString("KEY_VERIFIED", "false").compareToIgnoreCase("true") == 0) {
            MainActivity.bKeyVerified = true;
        } else {
            MainActivity.bKeyVerified = false;
        }

        doUpdate();
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        if (buffer[0] == (byte) 0x55 && buffer[1] == (byte) 0xaa) {
            if (buffer[2] == (byte) 0x83) {
                if (buffer[3] == (byte) 0x32 && buffer[4] == (byte) 0xb8) {
                    onSuccess(); // action result continue
                } else {
                    onFail();
                }
            }
        }
        Log.e("buffer", byte2hex(buffer));
    }

    private void onFail() {
        Log.e("onfail", "删除失败");
        runOnUiThread(new ToastRunnable(MainActivity.this, "删除失败"));
    }

    private void onSuccess() {
        Log.e("onsuccess", "删除成功");
        runOnUiThread(new ToastRunnable(MainActivity.this, "删除成功"));
        bHasKey = false;
        bKeyVerified = false;
        SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
        sp.edit().putString("HAS_KEY", "false").commit();
        sp.edit().putString("KEY_VERIFIED", "false").commit();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doUpdate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        doUpdate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_key) {
            new SendingThread(mOutputStream, Command.REQUEST_DELETE).start();
        } else if (id == R.id.action_reset_verify) {
            bHasKey = true;
            bKeyVerified = false;
            SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
            sp.edit().putString("KEY_VERIFIED", "false").commit();
            doUpdate();
        }

        doUpdate();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (CreateKeyActivity.CREATE_KEY_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                this.bHasKey = true;
                this.bKeyVerified = true;

                SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
                sp.edit().putString("HAS_KEY", "true").commit();
                sp.edit().putString("KEY_VERIFIED", "true").commit();
            }
        } else if (VerifyKeyActivity.VERIFY_KEY_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                this.bKeyVerified = true;
                SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
                sp.edit().putString("KEY_VERIFIED", "true").commit();
            }
        } else {
            Log.v(LOG_TAG, "no support request code");
        }
    }

    private void doCreateKey() {
        try {
            startActivityForResult(new Intent(MainActivity.this, CreateKeyActivity.class),
                    CreateKeyActivity.CREATE_KEY_REQ);
        } catch (ActivityNotFoundException activityNotFound) {
            Log.e(LOG_TAG, "CreateKeyActivity is not found");
        }
    }

    private void doVerifyKey() {
        try {
            startActivityForResult(new Intent(MainActivity.this, VerifyKeyActivity.class),
                    VerifyKeyActivity.VERIFY_KEY_REQ);
        } catch (ActivityNotFoundException activityNotFound) {
            Log.e(LOG_TAG, "VerifyKeyActivity is not found");
        }
    }

    private void doUpdate() {
        if (bHasKey) {
            createKeybtn.setVisibility(View.GONE);
            verifyKeybtn.setVisibility(View.VISIBLE);
        } else {
            createKeybtn.setVisibility(View.VISIBLE);
            verifyKeybtn.setVisibility(View.GONE);
        }

    }
}

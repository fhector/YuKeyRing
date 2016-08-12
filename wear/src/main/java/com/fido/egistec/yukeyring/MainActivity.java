package com.fido.egistec.yukeyring;

import android.app.Activity;
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
import com.fido.egistec.fpservice.YouKeyUWrapper;


public class MainActivity extends Activity {

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
            bHasKey = false;
            bKeyVerified = false;
            YouKeyUWrapper.getInstance().sendCommand(Command.REQUEST_DELETE);
            SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
            sp.edit().putString("HAS_KEY", "false").commit();
            sp.edit().putString("KEY_VERIFIED", "false").commit();
        } else if (id == R.id.action_reset_verify) {
            bKeyVerified = false;
            SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
            sp.edit().putString("KEY_VERIFIED", "false").commit();
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
//        if(bHasKey) {
        createKeybtn.setVisibility(View.GONE);
        verifyKeybtn.setVisibility(View.VISIBLE);
//        } else {
//            createKeybtn.setVisibility(View.VISIBLE);
//            verifyKeybtn.setVisibility(View.GONE);
//        }

    }
}

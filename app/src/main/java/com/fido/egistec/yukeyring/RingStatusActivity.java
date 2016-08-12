package com.fido.egistec.yukeyring;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * Created by delly on 6/15/15.
 */
public class RingStatusActivity extends Activity {

    private final String LOG_TAG = MainActivity.class.getName();
    private final static String RED_SECURITY = "RED_SECURITY";
    private final static String YELLOW_SECURITY = "YELLOW_SECURITY";
    private final static String GREEN_SECURITY = "GREEN_SECURITY";
    private final static String ACTION_SECURITY_CHECK = "ACTION_SECURITY_CHECK";

    private BroadcastReceiver bReceiver;

    private ImageView RedRingImg;
    private ImageView YellowRingImg;
    private ImageView GreenRingImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringstatus_activity);

        RedRingImg = (ImageView) findViewById(R.id.RedKeyRing);
        RedRingImg.setVisibility(View.GONE);
        RedRingImg.invalidate();

        YellowRingImg = (ImageView) findViewById(R.id.YelloKeyRing);
        YellowRingImg.setVisibility(View.GONE);
        YellowRingImg.invalidate();

        GreenRingImg = (ImageView) findViewById(R.id.GreenKeyRing);
        GreenRingImg.setVisibility(View.GONE);
        GreenRingImg.invalidate();

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

        if (getIntent().hasExtra("security_level")) {
            String level = getIntent().getStringExtra("security_level");
            Log.v(LOG_TAG, "Security level: " + level
                    + " ,HasKey: " + MainActivity.bHasKey + " ,HasVerified: " + MainActivity.bKeyVerified);
            if (GREEN_SECURITY.equals(level)) {
                toastGreenKey();
            } else if (YELLOW_SECURITY.equals(level)) {
                if (MainActivity.bHasKey && MainActivity.bKeyVerified)
                    toastGreenKey();
                else if (MainActivity.bHasKey && !MainActivity.bKeyVerified) {
                    requestVerifyKey();
                } else {
                    requestCreateKey();
                }
            } else if (RED_SECURITY.equals(level)) {
                if (MainActivity.bHasKey) {
                    requestVerifyKey();
                } else {
                    requestCreateKey();
                }
            } else {
                Log.v(LOG_TAG, "no support security level" + level);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (CreateKeyActivity.CREATE_KEY_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                MainActivity.bHasKey = true;
                MainActivity.bKeyVerified = true;
                SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
                sp.edit().putString("HAS_KEY", "true").commit();
                sp.edit().putString("KEY_VERIFIED", "true").commit();
                toastGreenKey();
            }
        } else if (VerifyKeyActivity.VERIFY_KEY_REQ == requestCode) {
            if (resultCode == RESULT_OK) {
                MainActivity.bKeyVerified = true;
                SharedPreferences sp = getSharedPreferences("key.share_preferences", MODE_PRIVATE);
                sp.edit().putString("KEY_VERIFIED", "true").commit();
                toastGreenKey();
            }
        } else {
            Log.v(LOG_TAG, "no support request code");
        }
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

    private void toastGreenKey() {

        boolean noKeyRingUI = true;
        if (noKeyRingUI) {
            setResult(RESULT_OK);
            finish();
        } else {
            GreenRingImg.setVisibility(View.VISIBLE);
            GreenRingImg.invalidate();
            GreenRingImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });

            final Toast toast = Toast.makeText(this, "successful security check ", Toast.LENGTH_SHORT);
            toast.show();
            new CountDownTimer(2000, 1000) {
                public void onTick(long m) {
                    toast.setText("success key.. " + (m / 1000));
                    toast.show();
                }

                public void onFinish() {
                    setResult(RESULT_OK);
                    finish();
                }
            }.start();
        }
    }

    private void toastYellowKey() {
        YellowRingImg.setVisibility(View.VISIBLE);
        YellowRingImg.invalidate();
        YellowRingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestVerifyKey();
            }
        });

        final Toast toast = Toast.makeText(this, "Press to verify.. 5", Toast.LENGTH_SHORT);
        new CountDownTimer(5000, 1000) {
            public void onTick(long m) {
                toast.cancel();
                toast.setText("Press to verify.. " + (m / 1000));
                toast.show();
            }

            public void onFinish() {
                //finish();
            }
        }.start();
    }

    private void toastRedKey() {
        RedRingImg.setVisibility(View.VISIBLE);
        RedRingImg.invalidate();
        final Toast toast = Toast.makeText(this, "Press to enroll.. 5", Toast.LENGTH_SHORT);
        new CountDownTimer(5000, 1000) {
            public void onTick(long m) {
                toast.cancel();
                toast.setText("Press to verify.. " + +(m / 1000));
                toast.show();
            }

            public void onFinish() {
                //finish();
            }
        }.start();
    }

    private void requestCreateKey() {
        Log.v(LOG_TAG, "requestCreateKey()");
        try {
            Intent myIntent = new Intent(RingStatusActivity.this, CreateKeyActivity.class);
            RingStatusActivity.this.startActivityForResult(myIntent, CreateKeyActivity.CREATE_KEY_REQ);
        } catch (ActivityNotFoundException activityNotFound) {
            Log.e(LOG_TAG, "CreateKeyActivity is not found");
        }
    }

    private void requestVerifyKey() {
        Log.v(LOG_TAG, "requestVerifyKey()");
        try {
            Intent myIntent = new Intent(RingStatusActivity.this, VerifyKeyActivity.class);
            RingStatusActivity.this.startActivityForResult(myIntent, VerifyKeyActivity.VERIFY_KEY_REQ);
        } catch (ActivityNotFoundException activityNotFound) {
            Log.e(LOG_TAG, "VerifyKeyActivity is not found");
        }
    }
}

package com.fido.egistec.fpservice;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import egistec.fingerauth.api.FPAuthListeners.StatusListener;

/**
 * Created by delly on 6/15/15.
 */
public class YouKeyUWrapper {

    private static final String TAG = "YouKeyUWrapper";

    private static YouKeyUWrapper gUWrapper;

    private SerialPort mSerialPort = null;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;

    private StatusListener mCBStatusListener;
    //    private EnrollListener mCBEnrollListener;
    private FingerEnrollListener mFingerEnrollListener;
    //    private VerifyListener mCBVerifyListener;
    private FingerVerifyListener mFingerVerifyListener;


    private ReadThread mReadThread;
    public static boolean mRunThread = false;

    public static YouKeyUWrapper getInstance() {
        if (gUWrapper == null)
            gUWrapper = new YouKeyUWrapper();
        return gUWrapper;
    }

    public YouKeyUWrapper() {
        try {
            mSerialPort = new SerialPort(new File("/dev/ttyMT1"), 115200, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();

        /* Create a receiving thread */
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    public void finalize() {

        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mSerialPort != null) mSerialPort.close();
        mSerialPort = null;

        if (mReadThread != null) mReadThread.interrupt();
        mReadThread = null;
    }

    protected void onDataReceived(byte[] buffer, int size) {
        Log.e("buffer", byte2hex(buffer));
        if (buffer[0] == (byte) 0x55 && buffer[1] == (byte) 0xaa) {
            if (buffer[2] == (byte) 0x81) {
                if (buffer[3] == (byte) 0x21) {
                    mFingerEnrollListener.onProgress();
                } else if (buffer[3] == (byte) 0xff) {
                    mFingerEnrollListener.onFail();
                } else if (buffer[3] == (byte) 0x24 && buffer[4] == (byte) 0x30) {
                    mFingerEnrollListener.onSuccess(); // action result continue
                } else if (buffer[3] == (byte) 0x25) {
                    mFingerEnrollListener.waitForFingerOn();
                } else if (buffer[3] == (byte) 0x28) {
                    mFingerEnrollListener.fingerCoveredPatialSensor();
                } else if (buffer[3] == (byte) 0x23) {
                    mFingerEnrollListener.fingerUp();
                } else if (buffer[3] == (byte) 0x20) {
                    mFingerEnrollListener.changeFinger();
                } else if (buffer[3] == (byte) 0x19) {
                    mFingerEnrollListener.fingerLeftOrRight();
                }
            } else if (buffer[2] == (byte) 0x82) {
                if (buffer[3] == (byte) 0xfe) {
                    mFingerVerifyListener.noFingerEnrolled();
                } else if (buffer[3] == (byte) 0x97) {
                    mFingerVerifyListener.waitForFingerOn();
                } else if (buffer[3] == (byte) 0x95) {
                    mFingerVerifyListener.notAFingerPrint();
                } else if (buffer[3] == (byte) 0x93) {
                    mFingerVerifyListener.badFingerPrint();
                } else if (buffer[3] == (byte) 0x22) {
                    mFingerVerifyListener.fingerUp();
                } else if (buffer[3] == (byte) 0x01) {
                    mFingerVerifyListener.onFail();
                } else if (buffer[3] == (byte) 0xb3 && buffer[4] == (byte) 0xa9) {
                    mFingerVerifyListener.onFail();
                }
            }
        } else {
            mFingerEnrollListener.onFail();
        }

        /*if (buffer[0] == (byte) 0x55 && buffer[1] == (byte) 0xAA) { // escape code
            if (buffer[2] == (byte) 0x00 && buffer[3] == (byte) 0x81) { // action id enrollment
                if (buffer[4] == (byte) 0x00 && buffer[5] == (byte) 0x21) // action progress result success
                    mCBEnrollListener.onProgress();
                else if (buffer[4] == (byte) 0x00 && buffer[5] == (byte) 0xFF) {// action progress result fail
                    mCBEnrollListener.onFail();
                } else if (buffer[4] == (byte) 0x00 && buffer[5] == (byte) 0x00) { // action complete result success
                    mCBEnrollListener.onSuccess(); // action result continue
                } else
                    Log.e(TAG, "Unexpected response");
            } else if (buffer[2] == (byte) 0x00 && buffer[3] == (byte) 0x82) { // action id verify
                if (buffer[4] == (byte) 0x00 && buffer[5] == (byte) 0x00) { // action result success
                    mCBVerifyListener.onSuccess();
                } else if (buffer[4] == (byte) 0x00 && buffer[5] == (byte) 0x01) {// action result fail
                    mCBVerifyListener.onFail();
                } else
                    Log.e(TAG, "Unexpected response");
            } else if (buffer[2] == (byte) 0x00 && buffer[3] == (byte) 0x83) { // action id delete
            } else if (buffer[2] == (byte) 0x00 && buffer[3] == (byte) 0x84) { // action id cancel
            }
        }*/
    }

    /*private String byteArrayToHex(byte[] a, int size) {
        StringBuilder sb = new StringBuilder(size * 2);
        for (byte b : a) {
            if (size-- > 0)
                sb.append(String.format("%02x", b & 0xff));
            else
                break;
        }
        return sb.toString();
    }*/

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        size = (size > 10) ? 10 : size;
                        Log.e(TAG, "size: " + size + " " + byte2hex(buffer));
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Log.e(TAG, "Interrupt thread!! ");
        }
    }

    private static String byte2hex(byte[] buffer) {
        String h = "";

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }

        return h;

    }

    public void sendCommand(byte[] cmd) {
        Log.e("cmd", byte2hex(cmd));
        try {
            mOutputStream.write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerOnFingerFetch(StatusListener CBStatusListener, FingerEnrollListener fingerEnrollListener) {
        mCBStatusListener = CBStatusListener;
//        mCBEnrollListener = CBEnrollListener;
        mFingerEnrollListener = fingerEnrollListener;
    }

    public void registerOnFingerVerify(StatusListener CBStatusListener, FingerVerifyListener fingerVerifyListener) {
        mCBStatusListener = CBStatusListener;
//        mCBVerifyListener = CBVerifyListener;
        mFingerVerifyListener = fingerVerifyListener;
    }
}

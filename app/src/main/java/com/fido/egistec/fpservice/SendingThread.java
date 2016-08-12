package com.fido.egistec.fpservice;

import android.util.Log;

import com.fido.egistec.yukeyring.CRC16;

import java.io.IOException;
import java.io.OutputStream;

public class SendingThread extends Thread {
    private byte[] mBuffer;
    private OutputStream mOutputStream;

    public SendingThread() {

    }

    public SendingThread(OutputStream outputStream, byte[] buffer) {
        this.mBuffer = CRC16.CalcCRC16Kermit(buffer);
        this.mOutputStream = outputStream;
    }

    @Override
    public void run() {
//            while (!isInterrupted()) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
                mOutputStream.flush();
                Log.e("buffer", byte2hex(mBuffer));
            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//            }
    }

    public String byte2hex(byte[] buffer) {
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
}
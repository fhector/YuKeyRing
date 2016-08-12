/*
 * Copyright 2011 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api.sample;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class Sending01010101Activity extends SerialPortActivity {

    SendingThread mSendingThread;
    byte[] mBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sending01010101);
        mBuffer = new byte[]{0x55, (byte) 0xaa, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, (byte) 0xb3, (byte) 0xa9};
        if (mSerialPort != null) {
            mSendingThread = new SendingThread();
            mSendingThread.start();
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        Log.e("data", byte2hex(buffer));
        // ignore incoming data
    }

    private class SendingThread extends Thread {
        @Override
        public void run() {
//            while (!isInterrupted()) {
            try {
                if (mOutputStream != null) {
                    mOutputStream.write(mBuffer);
                    mOutputStream.flush();
                } else {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
//            }
        }
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

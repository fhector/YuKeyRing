package com.fido.egistec.fpservice;

/**
 * Created by Administrator on 2016/7/30.
 */
public interface FingerVerifyListener {
    void noFingerEnrolled();

    void waitForFingerOn();

    void notAFingerPrint();

    void badFingerPrint();

    void fingerUp();

    void onFail();

    void onSuccess();
}

package com.fido.egistec.fpservice;

/**
 * Created by Administrator on 2016/7/29.
 */
public class Command {
    public static final byte[] REQUEST_ENROLL = {0x55, (byte) 0xaa, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00};  // enrollment
    public static final byte[] REQUEST_VERIFY = {0x55, (byte) 0xaa, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00};  // verification
    public static final byte[] REQUEST_DELETE = {0x55, (byte) 0xaa, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00};  // delete
    public static final byte[] REQUEST_CANCEL = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // cancel
    public static final byte[] REQUEST_FORWARD = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // foward
    public static final byte[] REQUEST_SET_DETECT_MODE = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // 设置监测模式
    public static final byte[] REQUEST_CALIBRATION = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // 校准
    public static final byte[] REQUEST_GET_CONFIHURATION = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // 获取配置
    public static final byte[] REQUEST_SET_CONFIGURATION = {0x55, (byte) 0xaa, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00};  // 设定配置
    public static final byte[] REQUEST_GET_COS_VERSION = {0x55, (byte) 0xaa, 0x00, 0x7f, 0x00, 0x00, 0x00, 0x00};  // cos version


    //ENROLL
    public static final byte[] RESPONSE_ENROLL_FINGER_COVERED_PARTIAL_SENSOR = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x28, 0x00, 0x00, (byte) 0xe1, (byte) 0xc9};  // 手指部分覆盖传感器
    public static final byte[] RESPONSE_ENROLL_WAIT_FOR_FINGER_ON = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x25, 0x00, 0x00, (byte) 0x1e, (byte) 0xb6};  // 等待正确的指纹
    public static final byte[] RESPONSE_ENROLL_FINGER_UP = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x23, 0x00, 0x00, (byte) 0xc8, (byte) 0x6f};  // 松开手指
    public static final byte[] RESPONSE_ENROLL_FINGER_PROGRESS = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x21, 0x00, 0x00, (byte) 0x7d, (byte) 0xd7};  // 指纹进度
    public static final byte[] RESPONSE_ENROLL_CHANGE_FINGER = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x20, 0x00, 0x00, (byte) 0x27, (byte) 0x0b};  // 换个手指
    public static final byte[] RESPONSE_ENROLL_FINGER_LEFT_OR_RIGHT = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x19, 0x00, 0x00, (byte) 0x3d, (byte) 0xbb};  // 左移或者右移
    public static final byte[] RESPONSE_ENROLL_FINGER_COMPLETE = {0x55, (byte) 0xaa, 0x00, (byte) 0x81, 0x00, 0x00, 0x00, 0x00, (byte) 0x24, (byte) 0x30};  // 左移或者右移

    //VERIFY
    public static final byte[] RESPONSE_VERIFY_NO_FINGER_ENROLLED = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, 0x00, 0x00, 0x00, (byte) 0xb3, (byte) 0xa9};  // 没有录入指纹，请先录入
    public static final byte[] RESPONSE_VERIFY_WAIT_FOR_FINGER_ON = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, 0x01, 0x00, 0x00, (byte) 0x63, (byte) 0x20};  // 忽略
    public static final byte[] RESPONSE_FINGER_COVERED_PARTIAL_SENSOR1 = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, 0x22, 0x00, 0x00, (byte) 0x8f, (byte) 0x7f};  //部分覆盖1
    public static final byte[] RESPONSE_FINGER_COVERED_PARTIAL_SENSOR2 = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, (byte) 0x93, 0x00, 0x00, (byte) 0x5f, (byte) 0xe1};  // 部分覆盖2
    public static final byte[] RESPONSE_FINGER_MATCHING = {0x55, (byte) 0xaa, 0x00, 0x04, (byte) 0x82, 0x00, (byte) 0x95, 0x00, (byte) 0x89, (byte) 0x38};  // 匹配中
    public static final byte[] RESPONSE_FINGER_MATCHED = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, (byte) 0x97, 0x00, 0x00, (byte) 0x3c, (byte) 0x80};  // 匹配完成
    public static final byte[] RESPONSE_FINGER_NO_MATCHED = {0x55, (byte) 0xaa, 0x00, (byte) 0x82, 0x00, (byte) 0xfe, 0x00, 0x00, (byte) 0xa5, (byte) 0xd3};  // 匹配失败

    //DELETE
    public static final byte[] RESPONSE_DELETE_SUCCESS = {0x55, (byte) 0xaa, 0x00, (byte) 0x83, 0x00, 0x00, 0x00, 0x00, (byte) 0x32, (byte) 0xb8};  // 清除成功

    //CANCEL
    public static final byte[] RESPONSE_CANCEL_SUCCESS = {0x55, (byte) 0xaa, 0x00, (byte) 0x84, 0x00, 0x00, 0x00, 0x00, (byte) 0x02, (byte) 0x64};  // 取消成功

    //FORWARD
    public static final byte[] RESPONSE_FORWARD = {0x55, (byte) 0xaa, 0x00, (byte) 0x85, 0x00, 0x00, 0x00, 0x00, (byte) 0x88, (byte) 0x31};  // forward

    public static final byte[] RESPONSE_SET_DETECT_MODE = {0x55, (byte) 0xaa, 0x00, (byte) 0x86, (byte) 0xf4, 0x00, 0x00, 0x00, (byte) 0x11, (byte) 0x2b};  // 设置监测模式
    public static final byte[] RESPONSE_CALIBRATION = {0x55, (byte) 0xaa, 0x00, (byte) 0x87, 0x00, 0x00, 0x00, 0x00, (byte) 0x1f, (byte) 0xa8};  // 校准
    public static final byte[] RESPONSE_GET_CONFIHURATION = {0x55, (byte) 0xaa, 0x00, (byte) 0x89, 0x00, 0x00, 0x00, 0x0a, (byte) 0x22, (byte) 0xb6};  // 获取配置
    public static final byte[] RESPONSE_SET_CONFIGURATION = {0x55, (byte) 0xaa, 0x00, (byte) 0x8A, 0x00, 0x00, 0x00, 0x00, 0x63, (byte) 0xdc};  // 设定配置
    public static final byte[] RESPONSE_GET_COS_VERSION = {0x55, (byte) 0xaa, 0x00, (byte) 0xff, 0x00, 0x00, 0x00, 0x27, 0x23, (byte) 0xb9};  // cos version

}

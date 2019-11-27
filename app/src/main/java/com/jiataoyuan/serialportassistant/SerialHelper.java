package com.jiataoyuan.serialportassistant;

import com.jiataoyuan.bean.ComBean;
import com.jiataoyuan.serialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


/**
 * @author benjaminwan
 * 串口辅助工具类
 */
public abstract class SerialHelper {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort;
    private int iBaudRate;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;

    //----------------------------------------------------
    private SerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    SerialHelper() {
        this("/dev/s3c2410_serial0", 9600);
    }

    //----------------------------------------------------
    void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
        mSendThread = new SendThread();
        mSendThread.setSuspendFlag();
        mSendThread.start();
        _isOpen = true;
    }

    //----------------------------------------------------
    void close() {
        if (mReadThread != null)
            mReadThread.interrupt();
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        _isOpen = false;
    }

    //----------------------------------------------------
    private void send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------
    void sendHex(String sHex) {
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }

    //----------------------------------------------------
    void sendTxt(String sTxt) {
        byte[] bOutArray = sTxt.getBytes();
        send(bOutArray);
    }

    //----------------------------------------------------
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) return;
                    byte[] buffer = new byte[1024];
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        ComBean ComRecData = new ComBean(sPort, buffer, size);
                        onDataReceived(ComRecData);
                    }
                    try {
                        Thread.sleep(50);//延时50ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //----------------------------------------------------
    private class SendThread extends Thread {
        boolean suspendFlag = true;// 控制线程的执行

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                send(getbLoopData());
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //线程暂停
        void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //唤醒线程
        synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    private void setBaudRate(int iBaud) {
        if (!_isOpen) {
            iBaudRate = iBaud;
        }
    }

    void setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        setBaudRate(iBaud);
    }

    void setPort(String sPort) {
        if (!_isOpen) {
            this.sPort = sPort;
        }
    }

    //----------------------------------------------------
    boolean isOpen() {
        return _isOpen;
    }

    //----------------------------------------------------
    private byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    //----------------------------------------------------
    void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    //----------------------------------------------------
    protected abstract void onDataReceived(ComBean ComRecData);
}
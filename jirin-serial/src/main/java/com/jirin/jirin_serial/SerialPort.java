package com.jirin.jirin_serial;

import java.io.FileDescriptor;

/**
 * @BelongsProject: JiRinSerial
 * @BelongsPackage: com.jirin.seriallib
 * @Author: wyl
 * @CreateTime: 2023-04-07  14:09
 * @Description: TODO
 * @Version: 1.0
 */
public class SerialPort {

    static {
        System.loadLibrary("jirin_serial");
    }

    public native FileDescriptor open(String devPath, int baudrate);

    public native void close();
}
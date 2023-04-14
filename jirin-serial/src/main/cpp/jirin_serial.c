//
// Created by wyl on 2023/4/13.
//

#include <jni.h>
#include <fcntl.h>
#include <stdio.h>
#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "android/log.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "jirin_serial", __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "jirin_serial", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "jirin_serial", __VA_ARGS__)

int getBaudrate(jint baudrate);

static int open_device(const char* tty_name) {
    int serial_fd;

    LOGD("open_device:%s", tty_name);
    serial_fd = open(tty_name, O_RDWR | O_NOCTTY | O_NDELAY);
    if (serial_fd < 0) {
        perror("open");
        return -1;
    }
    return serial_fd;
}

static int set_serial_dev_attr(int fd, int s)
{
    int ret_val = 0;
    struct termios ios;
    speed_t speed = B115200;
    if (s == 0)
    {
        s = 115200;
    }

    if (fd < 0)
    {
        return -1;
    }

    switch (s)
    {
        case 0:
            speed = B0;
            break;
        case 50:
            speed = B50;
            break;
        case 75:
            speed = B75;
            break;
        case 100:
            speed = B110;
            break;
        case 134:
            speed = B134;
            break;
        case 150:
            speed = B150;
            break;
        case 200:
            speed = B200;
            break;
        case 300:
            speed = B300;
            break;
        case 600:
            speed = B600;
            break;
        case 1200:
            speed = B1200;
            break;
        case 1800:
            speed = B1800;
            break;
        case 2400:
            speed = B2400;
            break;
        case 9600:
            speed = B9600;
            break;
        case 19200:
            speed = B19200;
            break;
        case 38400:
            speed = B38400;
            break;
        case 57600:
            speed = B57600;
            break;
        case 115200:
            speed = B115200;
            break;
        case 230400:
            speed = B230400;
            break;
        case 460800:
            speed = B460800;
            break;
        case 500000:
            speed = B500000;
            break;
        case 576000:
            speed = B576000;
            break;
        case 921600:
            speed = B921600;
            break;
        case 1000000:
            speed = B1000000;
            break;
        case 1152000:
            speed = B1152000;
            break;
        case 1500000:
            speed = B1500000;
            break;
        case 2000000:
            speed = B2000000;
            break;
        case 2500000:
            speed = B2500000;
            break;
        case 3000000:
            speed = B3000000;
            break;
        case 3500000:
            speed = B3500000;
            break;
        case 4000000:
            speed = B4000000;
            break;
        default:
            speed = B4000000;
            break;
    }

    /*ALOGD("set_serial_dev_attr serial_device_speed=%ld, speed=%lu\n",
            serial_device_speed, speed);*/

    ret_val = tcgetattr(fd, &ios);

    /* Set the baudrate. */
    if (!ret_val)
    {
        ret_val = cfsetispeed(&ios, speed);
        // CHECK_SYS_ERR( ret_val, "cfsetispeed" );
        LOGD("cfsetispeed OK\n");
    }
    if (!ret_val)
    {
        ret_val = cfsetospeed(&ios, speed);
        LOGD("cfsetospeed OK\n");
    }

    ios.c_iflag &= ~INLCR; /* do not change '\n' to '\r' */
    ios.c_iflag &= ~ICRNL; /* do not change '\r' to '\n' */
    ios.c_iflag &= ~(IXON | IXOFF | IXANY);
    ios.c_iflag &= ~IXON;  /* disable start/stop output control */
    ios.c_lflag = 0;       /* disable ECHO, ICANON, etc... */
    ios.c_oflag &= ~ONLCR; /* do not change '\n' to '\r' '\n' */
    ios.c_oflag &= ~OCRNL; /* do not change '\n' to '\r' */

    ios.c_oflag &= ~OPOST;

    ios.c_cflag &= ~CSIZE;
    ios.c_cflag |= CS8;
    ios.c_cflag &= ~PARENB; /* Clear parity enable */
    ios.c_iflag &= ~INPCK;  /* Enable parity checking */
    ios.c_cflag &= ~CSTOPB;
    ios.c_cflag &= ~CRTSCTS;

    tcflush(fd, TCIFLUSH);
    if (!ret_val)
    {
        ret_val = tcsetattr(fd, TCSANOW, &ios);
        LOGD(" tcsetattr OK\n");
    }

    return ret_val;
}

jobject JNICALL
Java_com_jirin_jirin_1serial_SerialPort_open(JNIEnv *env, jobject thiz, jstring dev_path,
                                             jint baudrate) {
    int fd;
    jobject mFileDescriptor;

    jboolean iscopy;
    const char* sol_dev = (*env)->GetStringUTFChars(env, dev_path, &iscopy);
    fd = open_device(sol_dev);
    LOGD("open device fd: %d", fd);
    (*env)->ReleaseStringUTFChars(env, dev_path, sol_dev);
    if (fd == -1) {
        LOGE("Open serial failed!!!");
        return NULL;
    }
    set_serial_dev_attr(fd, baudrate);

    /* Create a corresponding file descriptor */
    {
        jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
        jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
        jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
        mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
        (*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint) fd);
    }
    return mFileDescriptor;
}

JNIEXPORT void JNICALL
Java_com_jirin_jirin_1serial_SerialPort_close(JNIEnv *env, jobject thiz) {
    jclass SerialPortClass = (*env)->GetObjectClass(env, thiz);
    jclass FileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");

    jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
    jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass, "descriptor", "I");

    jobject mFd = (*env)->GetObjectField(env, thiz, mFdID);
    jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);

    LOGD("close(fd = %d)", descriptor);
    close(descriptor);
}
package com.jirin.jirin_serial;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SerialPort mSerialPort;

    private TextView mTvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvText = findViewById(R.id.sample_text);

        // Example of a call to a native method
        initData();
    }

    private void initData() {
        mSerialPort = new SerialPort();
        mSerialPort.open("/dev/STTYEMS44", 115200);
    }

    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
}
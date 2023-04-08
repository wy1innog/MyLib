package com.example.mylib;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jirin.activity.base.BaseAppCompatActivity;
import com.example.jirin.utils.AppBadgeUtil;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Button mBtnBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnBadge = (Button) findViewById(R.id.btn_badge);
        mBtnBadge.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_badge:
                AppBadgeUtil.setBadgeCount(this, 5);
                break;

            default:
                break;
        }
    }
}
package com.example.mylib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.jirin.activity.base.BaseAppCompatActivity;
import com.example.jirin.service.FloatViewClickService;
import com.example.jirin.utils.AppBadgeUtil;
import com.example.jirin.utils.PermissionUtil;

/**
 * @author PengFan
 */
public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Button mBtnBadge;
    private Button mBtnStart;
    private Button mBtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnBadge = (Button) findViewById(R.id.btn_badge);
        mBtnStart = (Button) findViewById(R.id.btn_start_auto_click);
        mBtnStop = (Button) findViewById(R.id.btn_stop_auto_click);

        mBtnBadge.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_badge:
                AppBadgeUtil.setBadgeCount(this, 5);
                break;

            case R.id.btn_start_auto_click:
                if (!Settings.canDrawOverlays(this)) {
                    startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName())));
                } else {
                    if (PermissionUtil.checkAutoClickServiceEnable(this,
                            getPackageName())) {
                        startService(new Intent(this, FloatViewClickService.class));
                        moveTaskToBack(true);
                    } else {
                        PermissionUtil.setAccessibilitySettings(this);
                    }
                }
                break;

            case R.id.btn_stop_auto_click:
                stopService(new Intent(this, FloatViewClickService.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FloatViewClickService.class));
    }
}
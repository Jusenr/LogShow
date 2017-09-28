package com.putao.logshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jusenr.toolslibrary.utils.DensityUtils;
import com.jusenr.toolslibrary.utils.ToastUtils;
import com.putao.ptx.app.PTDialog;
import com.putao.ptx.widget.PTCircleProgressView;
import com.putao.ptx.widget.PTDateWheelViewWarpper;
import com.putao.ptx.widget.PTWheelView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Activity mActivity;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        final PTCircleProgressView progress = (PTCircleProgressView) findViewById(R.id.progress);
        final PTDateWheelViewWarpper warpper = (PTDateWheelViewWarpper) findViewById(R.id.warpper);
        final StringBuffer stringBuffer = new StringBuffer();
        warpper.setOnDateWheelDataChangeListener(new PTDateWheelViewWarpper.OnDateWheelDataChangeListener() {
            @Override
            public void onDateChange(PTWheelView wheelView, int year, int month, int day, int timeUnit, int hour, int minute, int second) {
                stringBuffer.append(year).append("-")
                        .append(month).append("-")
                        .append(day).append(" ")
                        .append(hour).append(":")
                        .append(minute).append(":")
                        .append(second);
            }
        });

        tv.setText(stringFromJNI());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TestActivity.class));
            }
        });

        findViewById(R.id.btn_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Test2Activity.class));
            }
        });
        findViewById(R.id.btn_text0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTDialog ptDialog = new PTDialog(mActivity, R.layout.layout_dialog_test, 50);
                ptDialog.show();
            }
        });
        findViewById(R.id.btn_text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PTDialog ptDialog = new PTDialog(mActivity, "Test", 50);
                ptDialog.setWidth(DensityUtils.getScreenWidth(mActivity) * 3 / 5);
                ptDialog.setHeight(DensityUtils.getScreenHeight(mActivity) / 3);
                ptDialog.show();
            }
        });
        findViewById(R.id.btn_text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setProgress(i += 5);
            }
        });
        findViewById(R.id.btn_text3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showAlertToast(mActivity, stringBuffer.toString());
            }
        });
        findViewById(R.id.btn_text4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Test3Activity.class));
            }
        });
        findViewById(R.id.btn_text5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.btn_text6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}

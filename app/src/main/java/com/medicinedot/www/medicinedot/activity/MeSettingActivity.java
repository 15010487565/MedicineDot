package com.medicinedot.www.medicinedot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.medicinedot.www.medicinedot.R;

import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.XCDSharePreference;

public class MeSettingActivity extends SimpleTopbarActivity implements EMCallBack{

    private LinearLayout bindingmobile;
    private RelativeLayout updata_password,about,opinion_feedback;
    private TextView bindingmobile_text;
    private Button exit;

    @Override
    protected Object getTopbarTitle() {
        return R.string.setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesetting);
        initView();
    }
    private String phonetemp;
    private void initView() {
        String phone = XCDSharePreference.getInstantiation(this).getSharedPreferences("phone");
        bindingmobile = (LinearLayout) findViewById(R.id.bindingmobile);
        bindingmobile.setOnClickListener(this);
        TextView bindingmobile_text = (TextView) findViewById(R.id.bindingmobile_text);
        if (phone !=null&&!"".equals(phone)){
            if (phone.length()>5){
                phonetemp = phone.substring(0,3)+"****"
                        +phone.substring(phone.length()-4,phone.length());
            }else {
                phonetemp = phone.substring(0,3)+"****";
            }
        }else {
            phonetemp = "***********";
        }
        bindingmobile_text.setText(phonetemp);
        updata_password = (RelativeLayout) findViewById(R.id.updata_password);
        updata_password.setOnClickListener(this);
        opinion_feedback = (RelativeLayout) findViewById(R.id.opinion_feedback);
        opinion_feedback.setOnClickListener(this);
        about = (RelativeLayout) findViewById(R.id.about);
        about.setOnClickListener(this);
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bindingmobile://绑定手机
                intent = new Intent(this, SettingBindingmobileActivity.class);
                intent.putExtra("phone",phonetemp);
                startActivity(intent);
                break;
            case R.id.updata_password://修改密码
                startActivity(new Intent(this, SettingUpPasswordActivity.class));
                break;

            case R.id.opinion_feedback://意见反馈
                startActivity(new Intent(this, SettingOpinionFeedbackActivity.class));
                break;
            case R.id.about://关于我们
                intent = new Intent(this, SettingAboutActivity.class);
//                intent.putExtra("title", getString(R.string.about));
//                intent.putExtra("type", "1");
//                intent.putExtra("WebViewURL", "https://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.exit:
                Intent in = new Intent("android.intent.action.LOGIN");
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                EMClient.getInstance().logout(true);
                break;
        }
    }
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {

    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }
    @Override
    public void onSuccess() {
        Log.e("TAG_环信","退出环信登录");
    }

    @Override
    public void onError(int code, String message) {
        Log.e("TAG_环信","退出环信登录失败code="+code+";message="+message);
        EMClient.getInstance().logout(false);
    }

    @Override
    public void onProgress(int i, String s) {

    }
}

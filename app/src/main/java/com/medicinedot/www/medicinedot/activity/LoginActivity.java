package com.medicinedot.www.medicinedot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.medicinedot.www.medicinedot.R;
import com.medicinedot.www.medicinedot.bean.CityListAllInfo;
import com.medicinedot.www.medicinedot.bean.Logininfo;
import com.medicinedot.www.medicinedot.entity.GlobalParam;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.activity.PermissionsActivity;
import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ClassUtils;
import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.utils.XCDSharePreference;

import static www.xcd.com.mylibrary.activity.PermissionsActivity.PERMISSIONS_GRANTED;

public class LoginActivity extends SimpleTopbarActivity implements EMCallBack{

    private EditText login_phone, login_password;
    private Button login;
    private TextView register, forget_password;
    private String loginphone;//账号
    private String loginpassword;//密码
    private String utype;//类型 1供应商 2药店
    @Override
    protected Object getTopbarTitle() {
        return "登录";
    }

    @Override
    protected Class<?> getTopbarLeftFunc() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_GRANTED, PERMISSIONS);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        login_phone = (EditText) findViewById(R.id.login_phone);
        login_phone.setOnFocusChangeListener(this);
        login_password = (EditText) findViewById(R.id.login_password);
        login_password.setOnFocusChangeListener(this);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        forget_password = (TextView) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        Map<String, Object> params = new HashMap<String, Object>();
        //获取城市雷列表
        params.put("uid", "1");
        okHttpGet(101, GlobalParam.ALLCITYLIST, params);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login://登陆
                loginphone = login_phone.getText().toString().trim();
                if ("".equals(loginphone) || loginphone == null) {
                    ToastUtil.showToast("手机号不能为空");
                    return;
                }
                boolean mobileNO = ClassUtils.isMobileNO(loginphone);
                if (!mobileNO) {
                    ToastUtil.showToast("请输入正确的手机号");
                    return;
                }
                loginpassword = login_password.getText().toString().trim();
                if ("".equals(loginpassword) || loginpassword == null) {
                    ToastUtil.showToast("密码不能为空");
                    return;
                }

                createDialogshow();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("phone", loginphone);
                params.put("pwd", loginpassword);
                okHttpPost(100, GlobalParam.LOGIN, params);
                break;
            case R.id.register://注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.forget_password://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.dialog_register://注册dialog
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                registerNotifyDialog.dismiss();
                break;
            case R.id.dialog_cancel://取消dialog
                registerNotifyDialog.dismiss();
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode == 200) {
            switch (requestCode) {
                case 100:
                    Logininfo info = JSON.parseObject(returnData, Logininfo.class);
                    Logininfo.DataBean data = info.getData();
                    if (data !=null&&!"".equals(data)){
                        utype = data.getUtype();
                        String uid = data.getUid();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("uid", uid);
                        String strname = data.getName();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("name", strname);
                        String content = data.getContent();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("content", content);
                        String sex = data.getSex();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("sex", sex);
                        String headimage = data.getHeadimg();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("headimg", headimage);
                        String token = data.getToken();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("token", token);
                        String straddress = data.getRegion();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("region", straddress);
                        String detailednessaddress = data.getAddress();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("address", detailednessaddress);
                        String phone = data.getPhone();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("phone", phone);
                        String is_member = data.getIs_member();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("is_member", is_member);
                        String endtime = data.getEndtime();
                        XCDSharePreference.getInstantiation(this).setSharedPreferences("endtime", endtime);
                        Log.e("TAG_","region="+straddress+";endtime="+endtime);
                        String hxname = data.getHxname();
                        String hxpwd = data.getHxpwd();
                        if (hxname!=null&&!"".equals(hxname)&&hxpwd!=null&&!"".equals(hxpwd)){
                            EMClient.getInstance().login(hxname,hxpwd,this);
                        }
                        if ("1".equals(utype)) {
                            //供应商Main
                            startActivity(new Intent(LoginActivity.this, MainSupplierActivity.class));
                        } else if ("2".equals(utype)) {
                            //药店Main
                            startActivity(new Intent(LoginActivity.this, MainDrugstoreActivity.class));
                        }
                    }

                    break;
                case 101:
                    CityListAllInfo cityallinfo = JSON.parseObject(returnData, CityListAllInfo.class);
                    creatCityList(cityallinfo);
                    break;
            }
        } else if (returnCode == 300) {
            if (activityIsActivity()) {
                showRegisterDialog();
            }
        } else {
            ToastUtil.showToast(returnMsg);
        }
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
    public void creatCityList(CityListAllInfo cityallinfo){
        try {

            File file = new File(Environment.getExternalStorageDirectory(),
                    "province_data.xml");
            if(file.exists()){
                file.delete();
            }else {
                file.mkdir(); //如果不存在则创建
            }
            FileOutputStream fos = new FileOutputStream(file);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "root");

            //省集合
            List<CityListAllInfo.DataBean> provincedata = cityallinfo.getData();
            if (provincedata !=null&&provincedata.size()>0){
                for (int i = 0,j = provincedata.size(); i < j; i++) {
                    CityListAllInfo.DataBean  provincedataBean = provincedata.get(i);
                    //省名字
                    String proviceName =  provincedataBean.getProviceName();
                    serializer.startTag(null, "province");
                    serializer.attribute(null, "name", proviceName);
                    //市集合
                    List<CityListAllInfo.DataBean.CityBean> citydata = provincedataBean.getCity();
                    if (citydata !=null&&citydata.size()>0){
                        for (int k = 0,l = citydata.size(); k < l ; k++) {
                            CityListAllInfo.DataBean.CityBean cityBean = citydata.get(k);
                            //市名字
                            String cityName = cityBean.getCityName();
                            serializer.startTag(null, "city");
                            serializer.attribute(null, "name", cityName);
                            //区集合
                            List<CityListAllInfo.DataBean.CityBean.AreaBean> area = cityBean.getArea();
                            if (area !=null&&area.size()>0){
                                for (int m = 0,n =area.size(); m < n; m++) {
                                    //区名字
                                    CityListAllInfo.DataBean.CityBean.AreaBean areaBean = area.get(m);
                                    String areaName = areaBean.getAreaName();
                                    serializer.startTag(null, "district");
                                    serializer.attribute(null, "name", areaName);
                                    serializer.attribute(null, "zipcode",
                                            String.valueOf(i)+String.valueOf(k)+"000");
                                    serializer.endTag(null, "district");
                                }
                            }
                            serializer.endTag(null, "city");
                        }
                    }
                    serializer.endTag(null, "province");
                }
            }
            serializer.endTag(null, "root");
            serializer.endDocument();
            fos.close();
            ToastUtil.showToast( "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast("写入失败");
        }
    }
    protected AlertDialog registerNotifyDialog;
    private TextView dialog_register, dialog_cancel;

    private void showRegisterDialog() {
        LayoutInflater factor = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View serviceView = factor.inflate(R.layout.client_dialog, null);
        dialog_register = (TextView) serviceView.findViewById(R.id.dialog_register);
        dialog_cancel = (TextView) serviceView.findViewById(R.id.dialog_cancel);
        dialog_register.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);
        try {
            Activity activity = LoginActivity.this;
            while (activity.getParent() != null) {
                activity = activity.getParent();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            registerNotifyDialog = builder.create();
            registerNotifyDialog.show();
            registerNotifyDialog.setContentView(serviceView);
            FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
            //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
            serviceView.setLayoutParams(layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess() {
        Log.e("TAG_环信", "环信登录聊天服务器成功！");
    }

    @Override
    public void onError(int code, String message) {
        Log.e("TAG_环信", "环信登录聊天服务器失败code="+code+";message="+message);
    }

    @Override
    public void onProgress(int i, String s) {

    }
}

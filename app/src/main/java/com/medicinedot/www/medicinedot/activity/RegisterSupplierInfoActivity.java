package com.medicinedot.www.medicinedot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medicinedot.www.medicinedot.R;
import com.medicinedot.www.medicinedot.entity.GlobalParam;
import com.medicinedot.www.medicinedot.threelevelganged.ArrayWheelAdapter;
import com.medicinedot.www.medicinedot.threelevelganged.BaseThreeActivity;
import com.medicinedot.www.medicinedot.threelevelganged.OnWheelChangedListener;
import com.medicinedot.www.medicinedot.threelevelganged.WheelView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.utils.ToastUtil;
import www.xcd.com.mylibrary.utils.XCDSharePreference;

import static com.medicinedot.www.medicinedot.R.id.id_district;

public class RegisterSupplierInfoActivity extends BaseThreeActivity implements View.OnClickListener,OnWheelChangedListener {

    //药品需求
    private TextView address,individual_resume;
    private EditText name;
    private LinearLayout address_select;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView btn_confirm,btn_off;
    private Button ok;
    private String uid;
    @Override
    protected Object getTopbarTitle() {
        return "填写个人信息";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registersupplierinfo);
    }
    @Override
    protected void addViewToLeftFunctionZone() {
        super.addViewToLeftFunctionZone();
        uid = getIntent().getStringExtra("uid");
        name = (EditText) findViewById(R.id.name);
        name.setOnFocusChangeListener(this);

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

        address_select = (LinearLayout) findViewById(R.id.address_select);
        //
        btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_off = (TextView) findViewById(R.id.btn_off);
        btn_confirm.setOnClickListener(this);
        btn_off.setOnClickListener(this);

        address = (TextView) findViewById(R.id.address);
        address.setOnClickListener(this);
        individual_resume = (TextView) findViewById(R.id.individual_resume);
        individual_resume.setOnClickListener(this);
        individual_resume.setText("请填写个人简介！");
        setUpViews();
        setUpListener();
        setUpData();
    }
    private String strname;
    private String straddress;
    private  String individualresume;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.address://个人简介
                selectRegion();
                break;
            case R.id.btn_confirm:
                address_select.setVisibility(View.GONE);
                address.setText(mCurrentProviceName + mCurrentCityName);
                break;
            case R.id.btn_off:
                address_select.setVisibility(View.GONE);
                break;
            case R.id.ok:
                strname = name.getText().toString().trim();
                if (strname==null||"".equals(strname)){
                    ToastUtil.showToast("店铺名称不能为空！");
                    return;
                }
                straddress = address.getText().toString().trim();
                if (straddress==null||"".equals(straddress)){
                    ToastUtil.showToast("所在地区不能为空！");
                    return;
                }
                individualresume = individual_resume.getText().toString().trim();
                if (individualresume==null||"".equals(individualresume)){
                    ToastUtil.showToast("个人简介称不能为空！");
                    return;
                }
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uid", uid);
                params.put("name", strname);//店铺名称
                params.put("region", straddress);//所在地区
                params.put("address", "");//详细地址
                params.put("content", individualresume);//utype : 用户类型 1 供应商 2 药店
                params.put("sex", "");
                params.put("headimg", "");
                okHttpPost(100, GlobalParam.PERFECT, params);
                break;
            case R.id.individual_resume:
                Intent intent = new Intent(this,MultUpInfoActivity.class);
                intent.putExtra("title","药品需求");
                intent.putExtra("hintcontent","药品需求");
                startActivityForResult(intent,0);
                break;
        }
    }

    //选择地区
    public void selectRegion(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(individual_resume.getWindowToken(), 0); //强制隐藏键盘
        address_select.setVisibility(View.VISIBLE);
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(id_district);
        mViewDistrict.setVisibility(View.GONE);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
//        Log.e("TAG_","pCurrent="+pCurrent);
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        mViewDistrict.setVisibility(View.VISIBLE);
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);

        if (mDistrictDatasMap ==null||mDistrictDatasMap.size()==0){
            mViewDistrict.setVisibility(View.GONE);
        }else {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        String[] cities = null;
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        if (mCitisDatasMap ==null){
            cities = new String[]{""};
        }else {
            cities = mCitisDatasMap.get(mCurrentProviceName);
            if (cities == null) {
                cities = new String[]{""};
            }
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }
    @Override
    public boolean getIsShowChoiceDialog() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case 0:
                    String hintcontent = data.getStringExtra("hintcontent");
                    individual_resume.setText(hintcontent);
                    break;

            }
        }
    }
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        if (returnCode ==200){
            switch (requestCode){
                case 100:
                    XCDSharePreference.getInstantiation(this).setSharedPreferences("name", strname);
                    XCDSharePreference.getInstantiation(this).setSharedPreferences("region", straddress);
                    XCDSharePreference.getInstantiation(this).setSharedPreferences("content", individualresume);
                    startActivity(new Intent(this,MainSupplierActivity.class));
                    break;
            }
        }else {
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
}

package com.medicinedot.www.medicinedot.listener;

import android.app.Activity;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.util.NetUtils;

/**
 * Created by Android on 2017/9/18.
 */

public class HuanxinConnectionListener implements EMConnectionListener {

    private Activity context;

    public HuanxinConnectionListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onConnected() {
    }
    @Override
    public void onDisconnected(final int error) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(error == EMError.USER_REMOVED){
                    // 显示帐号已经被移除
                    Log.e("TAG_环信","显示帐号已经被移除");
                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    Log.e("TAG_环信","显示帐号在其他设备登录");
                } else {
                    if (NetUtils.hasNetwork(context)){
                        //连接不到聊天服务器
                        Log.e("TAG_环信","连接不到聊天服务器");
                    } else{
                        //当前网络不可用，请检查网络设置
                        Log.e("TAG_环信","当前网络不可用，请检查网络设置");
                    }
                }
            }
        });
    }
}
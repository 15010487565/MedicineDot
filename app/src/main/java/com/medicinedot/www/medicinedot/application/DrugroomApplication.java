package com.medicinedot.www.medicinedot.application;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.yonyou.sns.im.core.YYIMChat;

import java.util.Iterator;
import java.util.List;

import www.xcd.com.mylibrary.base.application.BaseApplication;

/**
 * Created by Android on 2017/9/5.
 */

public class DrugroomApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        try {

            //初始化头像jar
            YYIMChat.getInstance().init(getApplicationContext());
            YYIMChat.getInstance().configLogger(Log.VERBOSE, true, true, false);
//            int pid = android.os.Process.myPid();
//            String processAppName = getAppName(pid);
//            Log.e("TAG_","processAppName="+processAppName);
//            if (processAppName == null ||!processAppName.equalsIgnoreCase(getApplicationContext().getPackageName())) {
                // 则此application::onCreate 是被service 调用的，直接返回
                EMOptions options = new EMOptions();
                // 默认添加好友时，是不需要验证的，改成需要验证
//                options.setAcceptInvitationAlways(false);
                //设置为 false 关闭。
                options.setAutoLogin(false);
                //初始化环信即时通讯
//                EMClient.getInstance().init(getApplicationContext(), options);
            //初始化环信即时通讯聊天界面
                EaseUI.getInstance().init(getApplicationContext(), options);
                //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
                EMClient.getInstance().setDebugMode(true);
//                return;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}

package com.medicinedot.www.medicinedot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.medicinedot.www.medicinedot.R;
import com.medicinedot.www.medicinedot.fragment.HomeSupplierFragment;
import com.medicinedot.www.medicinedot.fragment.MeSupplierFragment;
import com.medicinedot.www.medicinedot.fragment.RecentchatSupplierFragment;
import com.medicinedot.www.medicinedot.view.NoScrollViewPager;
import com.yonyou.sns.im.log.YYIMLogger;
import com.yonyou.sns.im.util.common.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.base.fragment.BaseFragment;
import www.xcd.com.mylibrary.widget.SnsTabWidget;

public class MainSupplierActivity extends SimpleTopbarActivity implements EMCallBack{

    /**供应商
     * fragment classes
     */
    private static Class<?> fragmentArray[] = {
            HomeSupplierFragment.class,
            RecentchatSupplierFragment.class,
            MeSupplierFragment.class};
    /**
     * tabs text
     */
    private static int[] MAIN_TAB_TEXT = new int[]{R.string.home, R.string.recentchat, R.string.me};
    /**
     * tabs image normal
     */
    private static int[] MAIN_TAB_IMAGE = new int[]{R.mipmap.icon_tab_home, R.mipmap.icon_tab_chat, R.mipmap.icon_tab_me};
    /**
     * tabs image selected
     */
    private static int[] MAIN_TAB_IMAGEHL = new int[]{R.mipmap.icon_tab_home_press,R.mipmap.icon_tab_chat_press,  R.mipmap.icon_tab_me_press};

//    /**
//     * Topbar功能列表
//     */
//    private static Class<?> rightFuncArray[] = {MainAddTopBtnFunc.class};
    /**
     * fragment列表
     */
    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();

    private NoScrollViewPager viewPager;
    private SnsTabWidget tabWidget;
    private LinearLayout main_bottom;

    /**
     * 第一次返回按钮时间
     */
    private long firstTime;

    @Override
    public boolean isTopbarVisibility() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // 初始化fragments
        initFragments();
        // 初始化ViewPager
        initPager();
        // 初始化Tab
        initTabWidget();
    }

    private void initView() {
        viewPager = (NoScrollViewPager) findViewById(R.id.main_viewpager);
        tabWidget = (SnsTabWidget) findViewById(R.id.main_tabwidget);
        main_bottom = (LinearLayout) findViewById(R.id.main_bottom);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重设title
//        resetTitle(tabWidget.getCurIndex());
    }

    /**
     * 获得所有的FragmentClass
     *
     * @return
     */
    protected Class<?>[] getFragmentClassArray() {
        return fragmentArray;
    }

    /**
     * 初始化fragments
     */
    protected void initFragments() {
        // 初始化fragments
        for (int i = 0; i < fragmentArray.length; i++) {
            BaseFragment fragment = null;
            try {
                fragment = (BaseFragment) fragmentArray[i].newInstance();
                fragment.setActivity(this);
            } catch (Exception e) {
                YYIMLogger.d(e);
            }
            fragmentList.add(fragment);
        }
    }

    /**
     * 初始化ViewPager
     */
    protected void initPager() {
        // adapter
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        // 默认选中第一个
        viewPager.setCurrentItem(0);
        // page change监听
        viewPager.setOnPageChangeListener(new MainPageChangeListener());
    }

    /**
     * 初始化Tab
     */
    protected void initTabWidget() {
        // LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < fragmentArray.length; i++) {
            // 实例化tabitem
            View view = inflater.inflate(R.layout.view_main_tabitem, null);
            // 为每一个Tab按钮设置图标、文字和内容
            setTextViewStyle(view, i, (i == 0));
            tabWidget.addView(view);
        }
        String type = getIntent().getStringExtra("type");
        if ("2".equals(type)){
            // 选中第一个
            tabWidget.setCurrentTab(2);
        }else {
            // 选中第一个
            tabWidget.setCurrentTab(0);
        }
        // 添加监听
        tabWidget.setTabSelectionListener(new MainTabSelectionListener());
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void setTextViewStyle(View view, int index, boolean isCur) {
        // TextView
        TextView textView = (TextView) view.findViewById(R.id.main_tabitem_text);
        // 设置文字
        textView.setText(MAIN_TAB_TEXT[index]);

        // TextView
        TextView textViewHl = (TextView) view.findViewById(R.id.main_tabitem_texthl);
        // 设置文字
        textViewHl.setText(MAIN_TAB_TEXT[index]);

        // ImageView
        ImageView imageView = (ImageView) view.findViewById(R.id.main_tabitem_icon);
        // 非高亮图标
        imageView.setImageResource(MAIN_TAB_IMAGE[index]);

        // ImageView
        ImageView imageViewHl = (ImageView) view.findViewById(R.id.main_tabitem_iconhl);
        // 高亮图标
        imageViewHl.setImageResource(MAIN_TAB_IMAGEHL[index]);

        resetTextViewStyle(view, index, isCur);
    }

    /**
     * 重设TextView的样式
     *
     * @param index
     * @param isCur
     */
    protected void resetTextViewStyle(View view, int index, boolean isCur) {
        // 选中页签
        if (isCur) {
            resetTextViewAlpha(view, 1);
        } else {// 未选中页签
            resetTextViewAlpha(view, 0);
        }
    }

    /**
     * 重设TextView的Alpha值
     *
     * @param view
     * @param f
     */
    private void resetTextViewAlpha(View view, float f) {
        if (view == null) {
            return;
        }
        // ViewHl  通过设置透明度实现切换
        View itemViewHl = (View) view.findViewById(R.id.main_tabitem_viewhl);
        itemViewHl.setAlpha(f);
        //通过布局隐藏实现切换
        View itemViewH = (View) view.findViewById(R.id.main_tabitem_view);
        if (f == 1) {
            itemViewH.setVisibility(View.GONE);
        }
        if (f == 0) {
            itemViewH.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重设页面的Alpha
     *
     * @param index
     * @param f
     */
    private void resetFragmentAlpha(int index, float f) {
        if (index < 0 || index >= fragmentList.size()) {
            return;
        }
        View view = fragmentList.get(index).getView();
        if (view != null) {
            view.setAlpha(f);
        }
    }

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            backHome();
        } else {
            firstTime = System.currentTimeMillis();
            // 再按一次返回桌面

            ToastUtil.showShort(this, R.string.main_press_again_back);
        }
    }

    private void backHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        EMClient.getInstance().logout(true);
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

    /**
     * tab change监听
     *
     * @author litfb
     * @version 1.0
     * @date 2014年9月23日
     */
//    private boolean getIntentStoreApp = true;
    private class MainTabSelectionListener implements SnsTabWidget.ITabSelectionListener {

        @Override
        public void onTabSelectionChanged(int tabIndex) {

            // 重设当前页
            viewPager.setCurrentItem(tabIndex, false);
//            if (tabIndex==2){
//                if (getIntentStoreApp){
////                    handler.sendEmptyMessage(2);
//
//                    Log.e("TAG_getIntentStoreApp","Tab");
//                }
//                getIntentStoreApp=true;
//            }
            // 重设title
//            resetTitle(tabIndex);
            if (!viewPager.hasFocus()) {
                viewPager.requestFocus();
            }
        }
    }

    /**
     * pager adapter
     *
     * @author litfb
     * @version 1.0
     * @date 2014年9月23日
     */
    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int paramInt) {
            return fragmentList.get(paramInt);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /**
     * page change监听
     *
     * @author litfb
     * @version 1.0
     * @date 2014年9月23日
     */
    private class MainPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
            int curIndex = tabWidget.getCurIndex();
            // 向右滑
            if (curIndex == paramInt1) {
                resetTextViewAlpha(tabWidget.getChildAt(curIndex), 1 - paramFloat);
                resetFragmentAlpha(curIndex, 1 - paramFloat);
                resetTextViewAlpha(tabWidget.getChildAt(curIndex + 1), paramFloat);
                resetFragmentAlpha(curIndex + 1, paramFloat);
            } else if (curIndex == paramInt1 + 1) {// 向左划
                resetTextViewAlpha(tabWidget.getChildAt(curIndex), paramFloat);
                resetFragmentAlpha(curIndex, paramFloat);
                resetTextViewAlpha(tabWidget.getChildAt(paramInt1), 1 - paramFloat);
                resetFragmentAlpha(paramInt1, 1 - paramFloat);
            }
        }

        @Override
        public void onPageSelected(int index) {
            // tabWidget焦点策略
            int oldFocusability = tabWidget.getDescendantFocusability();
            // 阻止冒泡
            tabWidget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            // 切换tab
            tabWidget.setCurrentTab(index);
            // 重设title
//            resetTitle(index);
            // 变换tab显示
            for (int i = 0; i < fragmentArray.length; i++) {
                View view = tabWidget.getChildAt(i);
                resetTextViewStyle(view, i, (i == index));
            }
            // 还原焦点策略
            tabWidget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int paramInt) {

        }

    }

    @Override
    protected Object getTopbarTitle() {
        return R.string.home;
    }

    @Override
    protected Class<?> getTopbarLeftFunc() {

//		return MainSearchTopBtnFunc.class;
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

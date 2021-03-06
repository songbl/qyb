package com.zhuandian.qxe.service;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zhuandian.qxe.R;
import com.zhuandian.qxe.chat.CustomUserProvider;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.leancloud.chatkit.LCChatKit;

/**
 * 软件全局配置类，
 * 1.用户初始化全局参数，
 * 2.初始化第三方框架
 * Created by 谢栋 on 2017/1/2.
 */
public class QYBApplication extends Application {

    // LeanCloud  appId、appKey
    private final String APP_ID = "XL0csHhKUXpGOtFspgAJa4RJ-gzGzoHsz";
    private final String APP_KEY = "7sckd1rcz1wcMkmzK1fq53v8";
    private static Context mContext;  //全局通用的context

    public static Context getAPPContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);

        //初始化Bmob的SDK
        Bmob.initialize(this, getString(R.string.bmobkey));
        //初始化后台统计功能
        cn.bmob.statistics.AppStat.i(getString(R.string.bmobkey), null);
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);
        //在线更新初始化
//        BmobUpdateAgent.initAppVersion();
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                // TODO Auto-generated method stub
                //根据updateStatus来判断更新是否成功
                Log.i("xiedong", updateStatus + "状态码");
            }
        });
    }

    //获取在注册界面得到的sharedpreferences对象存放的内容
    public static String getUsername(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "匿名用户");
        return username;
    }
}

package com.bshui.unlock;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mUnlockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)){
                //屏幕关闭
                Log.i("bshui","Screen off");
                //唤醒手机屏幕并解锁
                wakeUpAndUnlock(getApplicationContext());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册一个系统BroadCastReceiver
        registerReceiver(mUnlockReceiver,
                new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    public static void wakeUpAndUnlock(Context mContext){
        //获取电源管理器对象
        PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"bright"
        );
        wl.acquire();//点亮屏幕
        wl.release();//释放

        //屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager)mContext.getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        //屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard();//解锁

    }
}

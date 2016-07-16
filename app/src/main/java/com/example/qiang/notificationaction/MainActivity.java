package com.example.qiang.notificationaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //action的定义
    public final static String ACTION_BUTTON = "ButtonClick";
    //定义一个广播
    public ButtonBroadcastReceiver bReceiver;

    private TextView mTextView;
    //消息的标识
    private static final int NOTIFICATION_FLAG = 1;
    /**
     * 播放/暂停 按钮点击 ID
     */
    public final static int BUTTON_PALY_ID = 2;

    public final static String INTENT_BUTTONID_TAG = "ButtonId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.no_text);
        initButtonReceiver();
    }

    public void notificationMethod(View view) {
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (view.getId()) {
            case R.id.btn1:

                Notification myNotify = new Notification();
                myNotify.icon = R.drawable.natoli;
                myNotify.tickerText = "您有新短消息，请注意查收！";
                myNotify.when = System.currentTimeMillis();
                myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
                myNotify.priority = Notification.PRIORITY_MAX;//设置优先级
                //自定义消息框
                RemoteViews rv = new RemoteViews(getPackageName(), R.layout.my_notification);
                RemoteViews rv_big = new RemoteViews(getPackageName(), R.layout.my_big_notification);
                //分别绑定大小消息框
                myNotify.contentView = rv;
                myNotify.bigContentView = rv_big;

                Intent buttonIntent = new Intent(ACTION_BUTTON);

                buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
                //这里加了广播，所及INTENT的必须用getBroadcast方法
                PendingIntent intent_prev = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                rv_big.setOnClickPendingIntent(R.id.bo_button, intent_prev);

                PendingIntent contentIntent_main = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
                myNotify.contentIntent = contentIntent_main;

                manager.notify(NOTIFICATION_FLAG, myNotify);

                break;
            case R.id.btn2:
                // 清除id为NOTIFICATION_FLAG的通知
                manager.cancel(NOTIFICATION_FLAG);
                // 清除所有的通知
                // manager.cancelAll();
                break;
        }
    }


    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {

                    case BUTTON_PALY_ID:
                        Log.d("========", "play");
                        mTextView.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }

        }
    }

    public void initButtonReceiver() {
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);

    }
}

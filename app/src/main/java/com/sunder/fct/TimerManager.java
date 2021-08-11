package com.sunder.fct;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;

public class TimerManager {
    private final static long TIMER = 60*1000;
    private final static String DATA_CREATED = "DATA_CREATED";
    private long mDataCreated = 0;
    private Handler handler;
    private HandlerThread handlerThread;
    private HttpManager mHttpManager;

    public TimerManager() {
        handlerThread = new HandlerThread("loop");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public void updateDate(long dataCreated){
        System.err.printf("\n---updateDate---%1$d---\n",dataCreated);
        mDataCreated = dataCreated;
        handler.removeCallbacks(timerRunnable);
        handler.postDelayed(timerRunnable,100);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long timeNow = new Date().getTime();
            if ((timeNow - mDataCreated) > TIMER) {
                if(!mHttpManager.requestCall()){
                    handler.postDelayed(this, TIMER);
                    System.err.println("---requestCall = true----");
                }
               //  System.err.printf("\n---%1$d---%2$d---\n",timeNow,mDataCreated);
                //mHttpManager.showError();
                //updateDate(new Date().getTime());
            }else{
                System.err.println("----else handler.postDelayed");
                handler.postDelayed(this, TIMER);
            }
        }
    };

    public void registerListener(HttpManager httpManager){
        mHttpManager = httpManager;
    }

    public void onDestroy() {
        handler.removeCallbacks(timerRunnable);
        handlerThread.quit();
        handlerThread.interrupt();
    }

    public long getTime() {
        return mDataCreated;
    }
}

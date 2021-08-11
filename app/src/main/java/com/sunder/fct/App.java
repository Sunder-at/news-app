package com.sunder.fct;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;


public class App extends Application {

    private DaoSession daoSession;
    private HttpManager httpManager;
    private TimerManager timerManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.OpenHelper helper = new NoteOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        httpManager =  new HttpManager(daoSession.getNoteDao());
        timerManager = new TimerManager();
        httpManager.registerListener(timerManager);
        timerManager.registerListener(httpManager);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public TimerManager getTimerManager() {
        return timerManager;
    }

    private class NoteOpenHelper extends DaoMaster.OpenHelper{

        public NoteOpenHelper(Context context, String name) {
            super(context, name);

        }

        public NoteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

    }

}

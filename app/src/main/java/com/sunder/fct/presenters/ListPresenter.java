package com.sunder.fct.presenters;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.sunder.fct.Note;
import com.sunder.fct.views.ListView;

import java.util.List;


public class ListPresenter{
    private final static String DATA_CREATED = "DATA_CREATED";
    private ListView mListView;

    public ListPresenter(ListView listView){
        mListView = listView;
    }

    public void updateDataset() {
        mListView.updateDataset();
    }

    public void onClick(int i) {
        mListView.startContentActivity(i);
    }

    public void onHttpError() {
        mListView.showHttpError();
    }
}

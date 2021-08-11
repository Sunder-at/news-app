package com.sunder.fct.presenters;


import com.sunder.fct.Note;
import com.sunder.fct.views.ContentView;

import java.util.List;

public class ContentPresenter{
    private ContentView mContentView;

    public ContentPresenter(ContentView contentView){
        mContentView = contentView;

    }

    public void updateDataset(){
        mContentView.updateDataset();
    }

    public void onHttpError() {
        mContentView.showHttpError();
    }
}

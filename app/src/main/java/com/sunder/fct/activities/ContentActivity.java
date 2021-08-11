package com.sunder.fct.activities;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.sunder.fct.App;
import com.sunder.fct.ErrorAlertDialog;
import com.sunder.fct.R;
import com.sunder.fct.adapters.ContentPagerAdapter;
import com.sunder.fct.presenters.ContentPresenter;
import com.sunder.fct.views.ContentView;


public class ContentActivity extends AppCompatActivity implements ContentView {

    private ContentPresenter contentPresenter;
    private ViewPager viewPager;
    private ContentPagerAdapter contentPagerAdapter;
    private ErrorAlertDialog errorAlertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_activity);
        contentPresenter =  new ContentPresenter(this);
        ((App)getApplication()).getHttpManager().registerListener(contentPresenter);
        viewPager = findViewById(R.id.content_view_pager);
        contentPagerAdapter = new ContentPagerAdapter(getSupportFragmentManager(),
                ((App)getApplication()).getDaoSession().getNoteDao());
        viewPager.setAdapter(contentPagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("NOTE",0));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorAlertDialog = new ErrorAlertDialog(
                        ContentActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((App)getApplication()).getHttpManager().unregisterListener(contentPresenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void updateDataset(){
        contentPagerAdapter.updateData();
    }

    @Override
    public void showHttpError() {
        if(getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorAlertDialog.show();
                }
            });
        }
    }
}

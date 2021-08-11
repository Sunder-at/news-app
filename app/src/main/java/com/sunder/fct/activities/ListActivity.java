package com.sunder.fct.activities;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sunder.fct.App;
import com.sunder.fct.ErrorAlertDialog;
import com.sunder.fct.adapters.ListedItemsAdapter;
import com.sunder.fct.Note;
import com.sunder.fct.R;
import com.sunder.fct.presenters.ListPresenter;
import com.sunder.fct.views.ListView;


public class ListActivity extends AppCompatActivity implements ListView {

    private final static String DATA_CREATED = "DATA_CREATED";
    private ListPresenter listPresenter;
    private long dataCreated = 0;
    private ErrorAlertDialog errorAlertDialog;
    private ListedItemsAdapter listedItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            dataCreated = savedInstanceState.getLong(DATA_CREATED,0);
        listPresenter = new ListPresenter(this);
        ((App)getApplication()).getHttpManager().registerListener(listPresenter);
        setContentView(R.layout.list_activity);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.main_activity);
        swipeRefreshLayout.setOnRefreshListener(() -> {

        });
        listedItemsAdapter = new ListedItemsAdapter(
                ((App)getApplication()).getDaoSession().getNoteDao(),
                i -> listPresenter.onClick(i));
        RecyclerView listItemsView = findViewById(R.id.listed_items_view);
        listItemsView.setHasFixedSize(true);
        listItemsView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listItemsView.setAdapter(listedItemsAdapter);
        getSupportActionBar().setTitle("Factory");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listedItemsAdapter.notifyDataSetChanged();
                errorAlertDialog = new ErrorAlertDialog(ListActivity.this);

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((App)getApplication()).getTimerManager().updateDate(dataCreated);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DATA_CREATED,((App)getApplication()).getTimerManager().getTime());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((App)getApplication()).getTimerManager().onDestroy();
    }

    @Override
    public void updateDataset() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listedItemsAdapter.updateData();
            }
        });
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

//    /**
//     * Calls {@link SwipeRefreshLayout#setRefreshing(boolean)}.
//     * @param bool true to set refresh animation, false to cancel
//     */
//    /*
//    @Override
//    public void setRefreshing(boolean bool){
//        mainHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                ((SwipeRefreshLayout)findViewById(R.id.main_activity)).setRefreshing(bool);
//            }
//        });
//    }

    /**
     * Starts {@link ContentActivity} and passes {@link Note} for display.
     * Used by {@link ListedItemsAdapter#onBindViewHolder(ListedItemsAdapter.ListedItemsViewHolder, int)}
     *
     * @param noteID Item on list that was clicked on
     */
    @Override
    public void startContentActivity(int noteID){
        Intent intent = new Intent(this,ContentActivity.class);
        intent.putExtra("NOTE", noteID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

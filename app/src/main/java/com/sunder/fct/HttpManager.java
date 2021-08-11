package com.sunder.fct;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.widget.SwipeRefreshLayout;

import com.sunder.fct.presenters.ContentPresenter;
import com.sunder.fct.presenters.ListPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpManager {
    private final static String API_KEY = "35d49cd51ce2426e8cb9d366d2f55f5f";
    private HttpUrl URL;
    private OkHttpClient client;
    private NoteDao noteDao;
    private ContentPresenter mContentPresenter = null;
    private ListPresenter mListPresenter = null;
    private TimerManager mTimerManager;

    public HttpManager(NoteDao noteDao){
        this.noteDao = noteDao;
        URL = new HttpUrl.Builder().scheme("https").host("newsapi.org")
                .addPathSegment("v1").addPathSegment("articles")
                .addQueryParameter("source","bbc-news")
                .addQueryParameter("sortBy","top")
                .addQueryParameter("apiKey",API_KEY)
                .build();
        client = new OkHttpClient.Builder().build();
    }

    /**
     * Adds {@link Runnable} to the main looper that starts {@link #requestHttpResponse()}.
     * When it catches any Exception it shows {@link ErrorAlertDialog}.
     * Stops {@link SwipeRefreshLayout} from refreshing
     */
    public boolean requestCall(){
        try {
            requestHttpResponse();
            notifyDatasetChange();
            mTimerManager.updateDate(new Date().getTime());
        } catch (Exception e) {
            showError();
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void showError(){
        if(mListPresenter != null) mListPresenter.onHttpError();
        if(mContentPresenter != null) mContentPresenter.onHttpError();
    }
    /**
     * Fetches data via {@link Response} in form of {@link JSONObject}, finds any image links and
     * fetches them. Then deletes all in {@link NoteDao} and repopulates it with new data.
     *
     * @throws Exception Can be caused by {@link Call#execute()}, {@link JSONException} or invalid status
     */
    private void requestHttpResponse() throws Exception {
        //if(true) return;

        if(noteDao == null) throw new Exception("NoteDAO is null");
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        String response;
        Response r = call.execute();
        response = r.body().string();
        r.close();

        JSONObject jsonObject;
        jsonObject = new JSONObject(response);
        if (!jsonObject.getString("status").matches("ok"))
            throw new Exception("Invalid body content");
        JSONArray articles = jsonObject.getJSONArray("articles");
        noteDao.deleteAll();
        List<byte[]> bufferList = new ArrayList<>();
        for (int i=0;  i < articles.length(); i++) {
            byte[] is = getImageStream(((JSONObject)articles.opt(i)).getString("urlToImage"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeByteArray(is,0,is.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG,40,os);
            bufferList.add(os.toByteArray());
        }
        for (int i=0;  i < articles.length(); i++) {
            noteDao.insert(new Note(
                    ((JSONObject)articles.get(i)).getString("author"),
                    ((JSONObject)articles.get(i)).getString("title"),
                    ((JSONObject)articles.get(i)).getString("description"),
                    ((JSONObject)articles.get(i)).getString("url"),
                    ((JSONObject)articles.get(i)).getString("urlToImage"),
                    ((JSONObject)articles.get(i)).getString("publishedAt"),
                    bufferList.get(i)
            ));
        }
    }
    private void notifyDatasetChange(){
        if(mContentPresenter != null) mContentPresenter.updateDataset();
        if(mListPresenter != null) mListPresenter.updateDataset();

    }
    /**
     * Called by {@link #requestHttpResponse()} to fetch a single image provided by URL
     *
     * @param url URL that points to an image
     * @return Image in byte[] format
     * @throws IOException Caused by {@link Call#execute()}
     */
    private byte[] getImageStream(String url) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        byte[] response;
        Response r = call.execute();
        response = r.body().bytes();
        r.close();
        return response;
    }

    public void registerListener(ContentPresenter contentPresenter) {
        mContentPresenter = contentPresenter;
    }
    public void registerListener(ListPresenter listPresenter) {
        mListPresenter = listPresenter;
    }
    public void registerListener(TimerManager timerManager) {
        mTimerManager = timerManager;
    }
    public void unregisterListener(ContentPresenter contentPresenter) {
        mContentPresenter = null;
    }
    public void unregisterListener(ListPresenter listPresenter) {
        mListPresenter = null;
    }
    public void unregisterListener(TimerManager timerManager) {
        mTimerManager = null;
    }
}

package com.sunder.fct.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sunder.fct.ContentScreenSlideFragment;
import com.sunder.fct.Note;
import com.sunder.fct.NoteDao;

import java.util.List;

public class ContentPagerAdapter extends FragmentStatePagerAdapter {

    private final static String ARG_NOTE = "NOTE";
    private NoteDao mNoteDao;
    private List<Note> noteList;

    public ContentPagerAdapter(FragmentManager fm, NoteDao noteDao) {
        super(fm);
        mNoteDao = noteDao;
        noteList = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Id).list();
    }
    public void updateData() {
        noteList = mNoteDao.queryBuilder().orderAsc(NoteDao.Properties.Id).list();
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ContentScreenSlideFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_NOTE,noteList.get(i));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }
}

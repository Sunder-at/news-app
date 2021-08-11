package com.sunder.fct;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class ContentScreenSlideFragment extends Fragment {

    private final static String ARG_NOTE = "NOTE";
    private Note note;
    public ContentScreenSlideFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        note = (Note)getArguments().getSerializable(ARG_NOTE);
        View view = inflater.inflate(R.layout.content_item, container, false);
        ((TextView)view.findViewById(R.id.text_content_title)).setText(note.getTitle());
        ((TextView)view.findViewById(R.id.text_content_body)).setText(note.getDescription());
        Glide.with(view).load(note.getImage()).override(Target.SIZE_ORIGINAL).
                into((ImageView)view.findViewById(R.id.image_content_view));
        //view.getSupportActionBar().setTitle(note.getTitle());

        return view;
    }
}

package com.sunder.fct.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunder.fct.Note;
import com.sunder.fct.NoteDao;
import com.sunder.fct.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class ListedItemsAdapter extends
        RecyclerView.Adapter<ListedItemsAdapter.ListedItemsViewHolder> {
    private NoteDao mNoteDao;
    private List<Note> noteList;
    private Listener mListener;

    public ListedItemsAdapter(NoteDao noteDao, Listener listener) {
        mNoteDao = noteDao;
        noteList = mNoteDao.queryBuilder().orderAsc(NoteDao.Properties.Id).list();
        mListener = listener;
    }

    public void updateData() {
        noteList = mNoteDao.queryBuilder().orderAsc(NoteDao.Properties.Id).list();
        notifyDataSetChanged();
    }

    public static class ListedItemsViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;
        public ListedItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_single_title);
            image = itemView.findViewById(R.id.image_single);
        }
    }

    @NonNull
    @Override
    public ListedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ListedItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListedItemsViewHolder holder, int i) {
        Note note = noteList.get(i);
        holder.title.setText(note.getTitle());
        Glide.with(holder.itemView).load(note.getImage()).centerCrop().into(holder.image);
        holder.itemView.setOnClickListener(view -> {
           mListener.onClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    public interface Listener{
        void onClick(int i);
    }
}
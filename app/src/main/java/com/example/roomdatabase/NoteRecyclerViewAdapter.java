package com.example.roomdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdatabase.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteRecyclerViewHolder> {

    private List<Note> listOfNotes = new ArrayList<>();
    private Context context;

    public NoteRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public NoteRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRecyclerViewHolder holder, int position) {
        holder.priorityTextView.setText(String.valueOf(listOfNotes.get(position).getPriority()));
        holder.titleTextView.setText(listOfNotes.get(position).getTitle());
        holder.descriptionTextView.setText(listOfNotes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }

    public void setListOfNotes(List<Note> listOfNotes) {
        this.listOfNotes = listOfNotes;
        notifyDataSetChanged();
    }

    public class NoteRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView priorityTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;

        public NoteRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}

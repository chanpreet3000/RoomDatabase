package com.example.roomdatabase;

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
    private onItemClickListener listener;

    public NoteRecyclerViewAdapter() {
    }

    @NonNull
    public NoteRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteRecyclerViewHolder holder, int position) {
        holder.priorityTextView.setText(String.valueOf(listOfNotes.get(position).getPriority()));
        holder.titleTextView.setText(listOfNotes.get(position).getTitle());
        holder.descriptionTextView.setText(listOfNotes.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }

    public Note getNoteAt(int position) {
        return listOfNotes.get(position);
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
            itemView.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getNoteAt(getAdapterPosition()));
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}

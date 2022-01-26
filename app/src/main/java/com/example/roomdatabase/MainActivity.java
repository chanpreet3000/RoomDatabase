package com.example.roomdatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.widget.Toast;

import com.example.roomdatabase.data.NoteViewModel;
import com.example.roomdatabase.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView notesRecyclerView;
    private FloatingActionButton addNoteFAB;
    public static final int ADD_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteRecyclerViewAdapter noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this);
        notesRecyclerView.setAdapter(noteRecyclerViewAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getNotes().observe(this, notes -> noteRecyclerViewAdapter.setListOfNotes(notes));
        addNoteFAB = findViewById(R.id.addNoteFAB);
        addNoteFAB.setOnClickListener(view -> startActivityForResult(new Intent(this, AddNoteActivity.class), ADD_REQUEST));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);
            noteViewModel.insert(new Note(title, description, priority));
            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not Saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
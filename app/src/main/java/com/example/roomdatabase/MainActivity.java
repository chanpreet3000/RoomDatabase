package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roomdatabase.data.NoteViewModel;
import com.example.roomdatabase.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    public static final int ADD_REQUEST = 1;
    public static final int UPDATE_REQUEST = 2;
    private Note lastDeletedNote = null;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);

        RecyclerView noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteRecyclerViewAdapter noteRecyclerViewAdapter = new NoteRecyclerViewAdapter();
        noteRecyclerView.setAdapter(noteRecyclerViewAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getNotes().observe(this, noteRecyclerViewAdapter::setListOfNotes);
        FloatingActionButton addNoteFAB = findViewById(R.id.addNoteFAB);
        addNoteFAB.setOnClickListener(view -> startActivityForResult(new Intent(this, AddNoteActivity.class), ADD_REQUEST));

        //Recycler View Item Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note noteToBeDeleted = noteRecyclerViewAdapter.getNoteAt(viewHolder.getAdapterPosition());
                noteViewModel.delete(noteToBeDeleted);
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
                lastDeletedNote = noteToBeDeleted;
                Snackbar.make(constraintLayout, "Undo this change?", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                noteViewModel.insert(lastDeletedNote);
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(noteRecyclerView);


        //Recycler View Item Click Listener
        noteRecyclerViewAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());
            intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
            startActivityForResult(intent, UPDATE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_REQUEST) {
                String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);
                noteViewModel.insert(new Note(title, description, priority));
                Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
            } else if (requestCode == UPDATE_REQUEST) {
                String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
                String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);
                int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);
                noteViewModel.update(new Note(id, title, description, priority));
            } else {
                Toast.makeText(this, "Note not Saved!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all your notes?")
                    .setPositiveButton("No", null)
                    .setNegativeButton("Yes", (dialogInterface, i) -> noteViewModel.deleteAll()).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
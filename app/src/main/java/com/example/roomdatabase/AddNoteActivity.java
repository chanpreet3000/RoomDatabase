package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private NumberPicker priorityNumberPicker;
    public static final String EXTRA_TITLE = "com.example.roomdatabase.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.roomdatabase.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.roomdatabase.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.example.roomdatabase.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priorityNumberPicker = findViewById(R.id.priorityNumberPicker);

        priorityNumberPicker.setMaxValue(10);
        priorityNumberPicker.setMinValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = getIntent().getExtras().getString(EXTRA_TITLE);
            String description = getIntent().getExtras().getString(EXTRA_DESCRIPTION);
            int priority = getIntent().getExtras().getInt(EXTRA_PRIORITY);

            titleEditText.setText(title);
            descriptionEditText.setText(description);
            priorityNumberPicker.setValue(priority);

            setTitle("Edit Note");
        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_item) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        int priority = priorityNumberPicker.getValue();
        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter a title and a description.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_ID, id);
        setResult(RESULT_OK, data);
        finish();
    }
}
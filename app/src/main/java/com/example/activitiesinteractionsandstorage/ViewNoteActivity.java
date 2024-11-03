package com.example.activitiesinteractionsandstorage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.IOException;

public class ViewNoteActivity extends AppCompatActivity {
    private TextView noteContentTextView;
    private ImageButton backButton;
    private ImageButton addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        addButton = findViewById(R.id.add_button);

        noteContentTextView = findViewById(R.id.noteContentTextView);

        // get the note title from the intent
        String noteTitle = getIntent().getStringExtra("NOTE_TITLE");

        // load the content of the note from internal storage
        loadNoteContent(noteTitle);

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> finish());

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(ViewNoteActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void loadNoteContent(String noteTitle) {
        try (FileInputStream fis = openFileInput(noteTitle)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String content = new String(bytes);
            noteContentTextView.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
            noteContentTextView.setText(getString(R.string.Error_Loading));
        }
    }
}

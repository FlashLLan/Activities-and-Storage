package com.example.activitiesinteractionsandstorage;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView notesListView;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;
    private ImageButton addButton;
    private ImageButton deleteButton;
    private boolean deleteMode = false; //check if delete mode is enabled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesListView = findViewById(R.id.notesListView);
        addButton = findViewById(R.id.add_button);
        deleteButton = findViewById(R.id.delete_button);

        notesList = new ArrayList<>();
        loadNotes();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(view -> {
            deleteMode = !deleteMode;
            Toast.makeText(this, deleteMode ? "Delete mode enabled" : "Delete mode disabled", Toast.LENGTH_SHORT).show();
        });


        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (deleteMode) {
                String selectedNote = notesList.get(position);
                showDeleteConfirmationDialog(selectedNote);
            } else {
                // open ViewNoteActivity if not in delete mode
                String noteTitle = notesList.get(position);
                Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
                intent.putExtra("NOTE_TITLE", noteTitle);
                startActivity(intent);
            }
        });
    }

    private void loadNotes() {
        notesList.clear();
        File dir = getFilesDir();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                notesList.add(file.getName());
            }
        }
    }

    // a confirmation of deleting
    private void showDeleteConfirmationDialog(String noteTitle) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete the note \"" + noteTitle + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNoteFile(noteTitle))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // delete the note
    private void deleteNoteFile(String noteTitle) {
        File file = new File(getFilesDir(), noteTitle);
        if (file.delete()) {
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            loadNotes();
            adapter.notifyDataSetChanged();
            deleteMode = false; // disable delete mode after deletion
        } else {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (item.getItemId() == R.id.delete_note) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
        adapter.notifyDataSetChanged();
    }
}

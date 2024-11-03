package com.example.activitiesinteractionsandstorage;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {
    private ListView notesListView;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        notesListView = findViewById(R.id.notesListView);
        notesList = new ArrayList<>();
        loadNotes();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedNote = notesList.get(position);
            showDeleteConfirmationDialog(selectedNote);
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

    //a confirmation
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
        } else {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }
}

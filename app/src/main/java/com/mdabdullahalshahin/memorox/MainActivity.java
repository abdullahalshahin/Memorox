package com.mdabdullahalshahin.memorox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    DatabaseHelper db;
    EditText etSearch;

    ArrayList<Note> allNotes = new ArrayList<>();
    ArrayList<Note> filteredNotes = new ArrayList<>();
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        etSearch = findViewById(R.id.etSearch);

        db = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditNoteActivity.class));
        });

        // Live Search (On Type)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }

            @Override public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        allNotes = db.getAllNotes();
        filteredNotes = new ArrayList<>(allNotes);

        adapter = new NoteAdapter(this, filteredNotes);
        recyclerView.setAdapter(adapter);
    }

    private void filterNotes(String text) {
        filteredNotes.clear();

        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(text.toLowerCase())) {

                filteredNotes.add(note);
            }
        }

        adapter.notifyDataSetChanged();
    }
}

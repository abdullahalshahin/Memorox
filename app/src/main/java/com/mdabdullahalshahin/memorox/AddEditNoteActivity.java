package com.mdabdullahalshahin.memorox;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditNoteActivity extends BaseActivity {

    EditText etTitle, etContent;
    Button btnSave, btnDelete;

    View color1, color2, color3, color4, color5, color6, color7;

    DatabaseHelper db;
    int noteId = 0;

    // Default = White (no color selected)
    String selectedColor = "#FFFFFF";

    View[] colorViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        // 🎨 Color Views
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        color4 = findViewById(R.id.color4);
        color5 = findViewById(R.id.color5);
        color6 = findViewById(R.id.color6);
        color7 = findViewById(R.id.color7);

        colorViews = new View[]{color1, color2, color3, color4, color5, color6, color7};

        db = new DatabaseHelper(this);

        // 🎨 CLICK LISTENER FOR COLORS (Select / Deselect)
        View.OnClickListener colorClick = v -> {
            String tappedColor = v.getTag().toString();

            // If user taps the same color → deselect
            if (selectedColor.equals(tappedColor)) {
                selectedColor = "#FFFFFF"; // default
                resetColorScale();
                return;
            }

            // Select new color
            selectedColor = tappedColor;
            resetColorScale();
            v.setScaleX(1.3f);
            v.setScaleY(1.3f);
        };

        // Attach listener to all color views
        for (View c : colorViews) {
            c.setOnClickListener(colorClick);
        }

        // ✔ Load existing note (Edit mode)
        if (getIntent().hasExtra("id")) {
            noteId = getIntent().getIntExtra("id", 0);
            etTitle.setText(getIntent().getStringExtra("title"));
            etContent.setText(getIntent().getStringExtra("content"));

            selectedColor = getIntent().getStringExtra("color"); // load stored color

            highlightSavedColor(); // highlight selected color
        } else {
            btnDelete.setVisibility(Button.GONE);
        }

        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());
    }


    // Reset highlight effect for all colors
    private void resetColorScale() {
        for (View c : colorViews) {
            c.setScaleX(1f);
            c.setScaleY(1f);
        }
    }

    // Highlight the color that was saved earlier (Edit mode)
    private void highlightSavedColor() {
        for (View v : colorViews) {
            if (v.getTag().toString().equals(selectedColor)) {
                v.setScaleX(1.3f);
                v.setScaleY(1.3f);
                break;
            }
        }
    }


    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note();
        note.setId(noteId);
        note.setTitle(title);
        note.setContent(content);
        note.setColor(selectedColor);

        if (noteId == 0) {
            db.addNote(note);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateNote(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteNote() {
        db.deleteNote(noteId);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}

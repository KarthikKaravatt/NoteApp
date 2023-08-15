package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {
    public static String TEXT_KEY = "com.example.main.activity.text"; // Key to for intent
    private final static int MAX_NOTES = 4;

    private final List<Button> noteButtons = new ArrayList<>(MAX_NOTES); // holds the buttons
    private final List<String> noteText = new ArrayList<>(MAX_NOTES); // holds the notes
    private Button newNoteButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int numNotes = 0; // determines what button becomes visible
    private String lastPressedButton; // The note button that was pressed last


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setupNotesButtons
        setupNotesButtons();
        // Create the activity launcher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                // What happens when the Note activity executes finish()
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // get the text data from the NoteInputActivity
                        Intent data = result.getData();
                        noteText.set(Integer.parseInt(lastPressedButton) - 1, data.getStringExtra(NoteInputActivity.TEXT_KEY));
                    }
                });
        setupOnClickListeners();
    }

    // starts a new NoteInputActivity
    private void setupNotesButtons() {
        newNoteButton = findViewById(R.id.save);
        Stream.of(R.id.NoteOne, R.id.NoteTwo, R.id.NoteThree, R.id.NoteFour)
                .map(id -> (Button) findViewById(id))
                .forEach(noteButtons::add);
        // set all buttons to invisible
        noteButtons.forEach(button -> button.setVisibility(View.INVISIBLE));
    }


    private void setupOnClickListeners() {
        // If new Note is clicked
        View.OnClickListener newNoteClicked = view -> {
            if (numNotes < 4) {
                // Get button and set it to visible
                Button button = noteButtons.get(numNotes);
                button.setVisibility(View.VISIBLE);
                noteText.add(Integer.parseInt(button.getText().toString()) - 1, "");
                lastPressedButton = button.getText().toString();
                // counter is incremented so the next button will be set to visible
                // when new Note is clicked again
                numNotes++;
                startNoteActivity(null);
            }
        };
        // If existing note is clicked
        View.OnClickListener noteClicked = view -> {
            Button thisButton = (Button) view;
            String buttonText = thisButton.getText().toString();
            lastPressedButton = buttonText;
            String text;
            text = noteText.get(Integer.parseInt(buttonText) - 1);
            startNoteActivity(text);
        };
        newNoteButton.setOnClickListener(newNoteClicked);
        noteButtons.forEach(button -> button.setOnClickListener(noteClicked));
    }

    public void startNoteActivity(String text) {
        Intent intent = new Intent(this, NoteInputActivity.class);
        intent.putExtra(TEXT_KEY, text);
        activityResultLauncher.launch(intent);
    }

}

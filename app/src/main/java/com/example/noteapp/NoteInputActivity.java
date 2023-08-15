package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;


public class NoteInputActivity extends AppCompatActivity {
    public static String TEXT_KEY = "com.example.note.input.activity.text"; // key for intent
    public static String UPDATE_BUTTON_TEXT = "Update";
    private TextInputEditText textInputEditText;
    private Button saveButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        if (savedInstanceState != null) {
            String text = savedInstanceState.getString(TEXT_KEY);
            textInputEditText.setText(text);
        }
        // The data from the MainActivity
        handleDataFromMainActivity();
        // what happens when save is pressed
        View.OnClickListener saveClicked = view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TEXT_KEY, textInputEditText.getText().toString());
            setResult(RESULT_OK, intent);
            // finish ends the activity
            finish();
        };
        saveButton.setOnClickListener(saveClicked);
    }

    private void setupUI() {
        setContentView(R.layout.activity_noteinput);
        saveButton = findViewById(R.id.save);
        textInputEditText = findViewById(R.id.TextField);
    }

    private void handleDataFromMainActivity() {
        Intent mainActivityData = getIntent();
        String noteText = mainActivityData.getStringExtra(MainActivity.TEXT_KEY);
        // null means newNote button was pressed so text is not set
        if (noteText != null) {
            // change save button name
            saveButton.setText(UPDATE_BUTTON_TEXT);
            textInputEditText.setText(noteText);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_KEY, textInputEditText.getText().toString());

    }
}

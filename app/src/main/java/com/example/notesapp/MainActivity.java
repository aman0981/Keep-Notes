package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button btnCreateNote;
    RecyclerView recyclerNotes;
    FloatingActionButton fabAdd;
    DatabaseHelper databaseHelper;
    LinearLayout llNoNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initvar();
        showNotes();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.add_note_lay);
                EditText edtTitle, edtContent;
                Button btnAdd;

                edtContent = dialog.findViewById(R.id.edtContent);
                edtTitle = dialog.findViewById(R.id.edtTitle);
                btnAdd = dialog.findViewById(R.id.btnAdd);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = edtTitle.getText().toString();
                        String content = edtContent.getText().toString();

                        if(!content.equals("")){

                            databaseHelper.noteDao().addNote(new Note(title,content));
                            showNotes();

                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Please Write Something!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabAdd.performClick();
            }
        });
    }

    public void showNotes() {

        ArrayList<Note> arrNotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();

        if(arrNotes.size()>0){
            recyclerNotes.setVisibility(View.VISIBLE);
            llNoNotes.setVisibility(View.GONE);

            recyclerNotes.setAdapter(new RecyclerNotesAdapter(MainActivity.this,arrNotes,databaseHelper));
        }
        else{
            llNoNotes.setVisibility(View.VISIBLE);
            recyclerNotes.setVisibility(View.GONE);
        }
    }

    private void initvar() {
        btnCreateNote = findViewById(R.id.btnCreateNote);
        recyclerNotes = findViewById(R.id.recyclerNotes);
        fabAdd = findViewById(R.id.fabAdd);

        llNoNotes = findViewById(R.id.llNoNotes);

        recyclerNotes.setLayoutManager(new GridLayoutManager(MainActivity.this,2));

        databaseHelper = DatabaseHelper.getInstance(this);

    }
}
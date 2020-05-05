package com.example.messangerapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;

public class Notes extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());//отвечает за сообщения
    EditText mEditTextNote;
    ImageButton mSendButton;
    RecyclerView mNotesRecycler;
    private static int MAX_NOTE_LENGTH = 1000;
    ArrayList<Note> notes = new ArrayList<>();
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSendButton = findViewById(R.id.send_note_b);
        mEditTextNote = findViewById(R.id.note_input);
        mNotesRecycler = findViewById(R.id.notes_recycler);
        mNotesRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mNotesRecycler.setLayoutManager(linearLayoutManager);

        final NoteAdapter noteAdapter = new NoteAdapter(Notes.this, notes);

        mNotesRecycler.setAdapter(noteAdapter);

        note = findViewById(R.id.note_item);


        mSendButton.setOnClickListener(new View.OnClickListener() {//отправка сообщения по клику
            @Override
            public void onClick(View v) {
                String nt = mEditTextNote.getText().toString();
                if (nt.equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите заметку",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nt.length() > MAX_NOTE_LENGTH) {
                    Toast.makeText(getApplicationContext(), "Слишком длинная заметка", Toast
                            .LENGTH_SHORT).show();
                    return;
                }

                Note note = new Note();
                note.setNot(nt);
                note.setUid(user.getUid());
                DatabaseReference mR = myRef.push();
                String uid = mR.getKey();
                note.setMesuid(uid);
                mR.setValue(note);
                mEditTextNote.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                String nt = dataSnapshot.child("not").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String mesuid = dataSnapshot.child("mesuid").getValue(String.class);
                Note note = new Note();
                note.setNot(nt);
                note.setUid(uid);
                note.setMesuid(mesuid);
                notes.add(note);
                noteAdapter.notifyDataSetChanged();
                mNotesRecycler.smoothScrollToPosition(notes.size());

            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<String, ViewHolderNote> adapter;
        mNotesRecycler.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Notes.this, Messages.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}

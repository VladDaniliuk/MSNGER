package com.example.messangerapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());//отвечает за сообщения

    EditText mEditTextNote;
    ImageView buttonBack;
    ImageView buttonVoiceInput;
    ImageButton mSendButton;
    LinearLayout layout;
    EditText EditTextSearch;

    private static int MAX_NOTE_LENGTH = 1000;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 0;

    ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mSendButton = findViewById(R.id.send_note_b);
        mEditTextNote = findViewById(R.id.note_input);
        buttonBack = findViewById(R.id.button_back);
        buttonVoiceInput = findViewById(R.id.button_voice_input);
        EditTextSearch = findViewById(R.id.edit_text_search);
        layout = findViewById(R.id.linearLayout);


        //отправка сообщения по клику
        mSendButton.setOnClickListener(v -> {
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
        });

        buttonBack.setOnClickListener(v -> {
            startActivity(new Intent(Notes.this, Messages.class));
            finish();
        });

        buttonVoiceInput.setOnClickListener(v -> {
            Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // намерение для вызова формы обработки речи (ОР)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // сюда он слушает и запоминает
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE); // вызываем активность ОР

        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                String nt = dataSnapshot.child("not").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String mesuid = dataSnapshot.child("mesuid").getValue(String.class);

                View view = getLayoutInflater().inflate(R.layout.item_note,null);

                TextView note = view.findViewById(R.id.note_item);
                Button delete = view.findViewById(R.id.delete);

                note.setText(nt);

                view.setOnLongClickListener(view1 -> {
                        if(delete.getVisibility() == View.GONE) {
                            delete.setVisibility(View.VISIBLE);
                        } else {
                            delete.setVisibility(View.GONE);
                        }
                        return true;
                });

                delete.setOnClickListener(view1 -> {
                    delete.setVisibility(View.GONE);
                    FirebaseDatabase.getInstance().getReference().child("Notes").child(user.
                            getUid()).child(mesuid).removeValue();
                    layout.removeView(view);
                });

                layout.addView(view);
            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull final DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList  searchTextVoice = data.getStringArrayListExtra(RecognizerIntent.
                    EXTRA_RESULTS);
            EditTextSearch = findViewById(R.id.edit_text_search);
            EditTextSearch.setText(searchTextVoice.toString().replace("[", "").
                    replace("]", ""));
        }
        super.onActivityResult(requestCode, resultCode, data);
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

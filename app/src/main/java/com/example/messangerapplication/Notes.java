package com.example.messangerapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Note;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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

    FloatingActionButton buttonNewNote;
    ImageView buttonBack;
    ImageView buttonVoiceInput;
    ImageButton mSendButton;
    LinearLayout layout;
    EditText EditTextSearch;
    BottomAppBar bottomAppBar;

    private static int MAX_NOTE_LENGTH = 1000;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 0;

    ArrayList<Note> notes = new ArrayList<>();
    ArrayList<View> noteViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mSendButton = findViewById(R.id.send_note_b);
        buttonNewNote = findViewById(R.id.b_new_note);
        buttonBack = findViewById(R.id.button_back);
        buttonVoiceInput = findViewById(R.id.button_voice_input);
        EditTextSearch = findViewById(R.id.edit_text_search);
        layout = findViewById(R.id.linearLayout);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        bottomAppBar.setNavigationOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Notes.this,
                    R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_notes_menu, (LinearLayout)findViewById(R.id.add_note),false);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        buttonNewNote.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Notes.this,
                    R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_note_add, (LinearLayout)findViewById(R.id.add_note),false);
            bottomSheetDialog.setContentView(bottomSheetView);

            bottomSheetDialog.show();

            Button buttonSaveNote = bottomSheetView.findViewById(R.id.button_save_note);
            EditText editTextNewNote = bottomSheetView.findViewById(R.id.editText_new_note);

            buttonSaveNote.setOnClickListener(view1 -> {
                String nt = editTextNewNote.getText().toString();
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
                editTextNewNote.setText("");
                bottomSheetDialog.dismiss();
            });
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

        EditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for(View noteView : noteViews) {
                    TextView a = noteView.findViewById(R.id.note_item);
                    if(!a.getText().toString().contains(EditTextSearch.getText().toString())) {
                        noteView.setVisibility(View.GONE);
                    } else {
                        noteView.setVisibility(View.VISIBLE);
                    }
                }
                if(EditTextSearch.getText().toString().length() == 0) {
                    for(View noteView : noteViews) {
                        noteView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                String nt = dataSnapshot.child("not").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String mesuid = dataSnapshot.child("mesuid").getValue(String.class);

                View view = getLayoutInflater().inflate(R.layout.item_note,null);

                TextView note = view.findViewById(R.id.note_item);
                note.setText(nt);

                LinearLayout ll = view.findViewById(R.id.qqw);
                
                ll.setOnLongClickListener(view1 -> {
                    Context context;
                    AlertDialog dialog = new AlertDialog.Builder(Notes.this).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("Notes").child(user.
                                    getUid()).child(mesuid).removeValue();
                            layout.removeView(view);
                            noteViews.remove(noteViews.indexOf(view));
                        }
                    }).create();
                    dialog.setTitle(nt);
                    dialog.show();
                    return true;
                });

                noteViews.add(view);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
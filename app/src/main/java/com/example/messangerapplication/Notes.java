package com.example.messangerapplication;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.AdjustedCornerSize;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.stream.Stream;

class Hashtags {
        String name;
        CheckBox checkBox;

        public Hashtags(String name, CheckBox checkBox) {
            this.name = name;
            this.checkBox = checkBox;
        }
};

public class Notes extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());//отвечает за сообщения

    FloatingActionButton buttonNewNote;
    ImageView buttonBack;
    ImageView buttonDeleteSearch;
    ImageView buttonVoiceInput;
    ImageButton mSendButton;
    LinearLayout layout;
    EditText EditTextSearch;
    BottomAppBar bottomAppBar;

    private static int MAX_NOTE_LENGTH = 1000;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 0;

    ArrayList<View> noteViews = new ArrayList<>();
    ArrayList<String> myHashtags = new ArrayList<>();
    ArrayList<Hashtags> noteHashtagList = new ArrayList<>();

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
        buttonDeleteSearch = findViewById(R.id.button_delete_search);

        buttonDeleteSearch.setOnClickListener(view -> {
            EditTextSearch.setText("");
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        bottomAppBar.setNavigationOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Notes.this,
                    R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_notes_menu, (LinearLayout)findViewById(R.id.add_note),false);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            LinearLayout linearLayoutListHashtag = bottomSheetView.findViewById(R.id.hashtag);

            myRef.child("NoteHashtag").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String hashtag = snapshot.getValue(String.class);
                    View view = getLayoutInflater().inflate(R.layout.item_note_hashtag,null);
                    CheckBox checkBox = view.findViewById(R.id.checkBox_hashtag);
                    checkBox.setVisibility(View.GONE);
                    TextView textView = view.findViewById(R.id.textView_note_hashtag);
                    textView.setText(hashtag);
                    linearLayoutListHashtag.addView(view);
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

        buttonNewNote.setOnClickListener(view -> {


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Notes.this,
                    R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_note_add, (LinearLayout)findViewById(R.id.add_note),false);

            bottomSheetDialog.setContentView(bottomSheetView);

            Note note = new Note();

            Button buttonSaveNote = bottomSheetView.findViewById(R.id.button_save_note);
            EditText editTextNewNote = bottomSheetView.findViewById(R.id.editText_new_note);
            TextView buttonAddHashtag = bottomSheetView.findViewById(R.id.add_hashtag);

            buttonAddHashtag.setOnClickListener(view1 -> {
                BottomSheetDialog bottomSheetDialogListHashtag = new BottomSheetDialog(Notes.this,
                        R.style.BottomSheetDialogTheme);
                View bottomSheetViewListHashtag = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_notes_hashtag_list, (LinearLayout)findViewById(R.id.add_note),false);

                bottomSheetDialogListHashtag.setContentView(bottomSheetViewListHashtag);
                bottomSheetDialogListHashtag.setCancelable(false);
                bottomSheetDialogListHashtag.show();

                noteHashtagList.stream().forEach(x -> {
                    note.getHash().stream().forEach(y -> {
                        x.checkBox.setChecked(x.name.equals(y) ? true : false);
                    });
                });

                EditText editTextAddHashtag = bottomSheetViewListHashtag.findViewById(R.id.editText_new_hashtag);
                Button buttonCreateHashtag = bottomSheetViewListHashtag.findViewById(R.id.button_new_hashtag);
                Button buttonSubmit = bottomSheetViewListHashtag.findViewById(R.id.submit);
                LinearLayout linearLayoutHashtagList = bottomSheetViewListHashtag.findViewById(R.id.linearLayout_list_hashtag);

                buttonCreateHashtag.setOnClickListener(view2 -> {

                    if (editTextAddHashtag.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Введите заметку",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (noteHashtagList.stream().anyMatch(x->x.name.equals(editTextAddHashtag.getText().toString()))) {
                        Toast.makeText(getApplicationContext(), "Такая метка уже существует", Toast
                                .LENGTH_SHORT).show();
                        return;
                    }

                    DatabaseReference mR = myRef.child("NoteHashtag").push();
                    mR.setValue(editTextAddHashtag.getText().toString());
                    editTextAddHashtag.setText("");
                });

                buttonSubmit.setOnClickListener(view2 -> {
                    myHashtags.removeAll(myHashtags);
                    LinearLayout hashtag = bottomSheetView.findViewById(R.id.hashtag);
                    hashtag.removeAllViews();
                    noteHashtagList.stream().forEach(x -> {
                        if (x.checkBox.isChecked()) {
                            View view3 = getLayoutInflater().inflate(R.layout.item_add_note_hashtag,null);

                            TextView TextViewHashtag = view3.findViewById(R.id.add_hashtag);
                            TextViewHashtag.setText(x.name);
                            hashtag.addView(view3);
                            x.checkBox.setChecked(false);
                            myHashtags.add(x.name);
                        }
                        note.setHash(myHashtags);
                        myHashtags.removeAll(myHashtags);
                    });


                    bottomSheetDialogListHashtag.dismiss();
                });

                myRef.child("NoteHashtag").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                        String noteHastag = dataSnapshot.getValue(String.class);

                        View view = getLayoutInflater().inflate(R.layout.item_note_hashtag,null);

                        TextView note = view.findViewById(R.id.textView_note_hashtag);
                        CheckBox checkBoxHashtag = view.findViewById(R.id.checkBox_hashtag);
                        ConstraintLayout itemNoteHashtag = view.findViewById(R.id.item_note_hashtag);

                        note.setText(noteHastag);

                        itemNoteHashtag.setOnClickListener(view2 -> {
                            checkBoxHashtag.setChecked(!checkBoxHashtag.isChecked());
                        });
                        linearLayoutHashtagList.addView(view);
                        Hashtags hashtags = new Hashtags(noteHastag,checkBoxHashtag);
                        myHashtags.stream().anyMatch(x-> {
                            if(x.equals(hashtags.name)) {
                                hashtags.checkBox.setChecked(true);
                                return true;
                            }
                            return false;
                        });
                        noteHashtagList.add(hashtags);
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            });

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

                note.setNot(nt);
                note.setUid(user.getUid());
                DatabaseReference mR = myRef.child("NoteText").push();
                String uid = mR.getKey();
                note.setMesuid(uid);
                mR.setValue(note);
                editTextNewNote.setText("");
                myHashtags.removeAll(myHashtags);
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
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
                buttonDeleteSearch.setVisibility(View.VISIBLE);
                noteViews.stream().forEach(a->{
                    TextView a1 = a.findViewById(R.id.note_item);
                    if(!a1.getText().toString().contains(EditTextSearch.getText().toString())) {
                        a.setVisibility(View.GONE);
                    } else {
                        a.setVisibility(View.VISIBLE);
                    }
                });
                if(EditTextSearch.getText().toString().length() == 0) {
                    buttonDeleteSearch.setVisibility(View.GONE);
                    noteViews.stream().forEach(a->{
                        a.setVisibility(View.VISIBLE);
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myRef.child("NoteText").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Note note = snapshot.getValue(Note.class);

                    View view = getLayoutInflater().inflate(R.layout.item_note, null);
                    TextView nt = view.findViewById(R.id.note_item);
                    ConstraintLayout constraintLayout = view.findViewById(R.id.note_item_with_button);
                    HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
                    nt.setText(note.getNot().toString());

                    RelativeLayout ll = view.findViewById(R.id.qqw);
                    LinearLayout linearLayoutHashtags = view.findViewById(R.id.hashtag);

                    if(!note.getHash().isEmpty()) {
                        Arrays.stream(note.getHash().toArray()).forEach(x -> {
                        View view1 = getLayoutInflater().inflate(R.layout.item_add_note_hashtag,null);
                        TextView textViewHashtag = view1.findViewById(R.id.add_hashtag);
                        textViewHashtag.setText(x.toString());
                        linearLayoutHashtags.addView(view1);
                        });
                    }
                    noteViews.add(view);
                    layout.addView(view);

                ll.setOnLongClickListener(view1 -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view2 = inflater.inflate(R.layout.activity_note_editor, null);
                    builder.setView(view2);

                    ImageView buttonDeleteNote = view2.findViewById(R.id.dutton_delete);
                    EditText editText = view2.findViewById(R.id.editText);
                    LinearLayout linearLayout = view2.findViewById(R.id.linearLayout);
                    Button buttonSave = view2.findViewById(R.id.button_save_note);

                    editText.setText(note.getNot());

                    if(!note.getHash().isEmpty()) {
                        Arrays.stream(note.getHash().toArray()).forEach(x -> {
                            View view3 = getLayoutInflater().inflate(R.layout.item_add_note_hashtag,null);
                            TextView textViewHashtag = view3.findViewById(R.id.add_hashtag);
                            textViewHashtag.setText(x.toString());
                            linearLayout.addView(view3);
                        });
                    }

                    builder.create();
                    AlertDialog alertDialog =builder.show();

                    buttonDeleteNote.setOnClickListener(view3 -> {
                        myRef.child("NoteText").child(snapshot.getKey()).removeValue();
                        layout.removeView(view);
                        noteViews.remove(noteViews.indexOf(view));
                        alertDialog.dismiss();
                    });

                    buttonSave.setOnClickListener(view3 -> {
                        myRef.child("NoteText").child(snapshot.getKey().toString())
                                .child("not").setValue(editText.getText().toString());
                        note.setNot(editText.getText().toString());
                        nt.setText(editText.getText().toString());
                        alertDialog.dismiss();
                    });

                    return true;
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /*myRef.child("NoteText").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Note note = new Note();
                note.setMesuid(dataSnapshot.child("mesuid").getValue(String.class));
                note.setUid(dataSnapshot.child("uid").getValue(String.class));
                note.setNot(dataSnapshot.child("not").getValue(String.class));

                myRef.child("NoteText").child(dataSnapshot.getKey().toString()).child("hash").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot :snapshot.getChildren()){
                            note.setHash(postSnapshot.getValue(ArrayList.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                View view = getLayoutInflater().inflate(R.layout.item_note, null);
                TextView nt = view.findViewById(R.id.note_item);
                nt.setText(note.getNot().toString());

                RelativeLayout ll = view.findViewById(R.id.qqw);
                LinearLayout linearLayoutHashtags = view.findViewById(R.id.hashtag);

                myHashtags.clear();


                ll.setOnLongClickListener(view1 -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view2 = inflater.inflate(R.layout.activity_note_editor, null);
                    builder.setView(view2);

                    ImageView buttonDeleteNote = view2.findViewById(R.id.dutton_delete);
                    EditText editText = view2.findViewById(R.id.editText);
                    LinearLayout linearLayout = view2.findViewById(R.id.linearLayout);
                    Button buttonSave = view2.findViewById(R.id.button_save_note);
/**//*


                    myHashtags.stream().forEach(x->{
                        View view3 = getLayoutInflater().inflate(R.layout.item_add_note_hashtag,null);
                        TextView textView = view3.findViewById(R.id.add_hashtag);
                        textView.setText(x);
                        linearLayout.addView(view3);
                    });
*//**//*
                    editText.setText(note.getNot());

                    builder.create();
                    AlertDialog alertDialog =builder.show();

                    buttonDeleteNote.setOnClickListener(view3 -> {
                        myRef.child("NoteText").child(dataSnapshot.getKey()).removeValue();
                        layout.removeView(view);
                        noteViews.remove(noteViews.indexOf(view));
                        alertDialog.dismiss();
                    });

                    buttonSave.setOnClickListener(view3 -> {
                        myRef.child("NoteText").child(dataSnapshot.getKey().toString())
                                .child("not").setValue(editText.getText().toString());
                        note.setNot(editText.getText().toString());
                        alertDialog.dismiss();
                    });

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
        });*/
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

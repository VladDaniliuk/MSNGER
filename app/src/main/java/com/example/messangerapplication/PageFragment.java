package com.example.messangerapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PageFragment extends AbsSmiles {

    private static DatabaseReference myRef;

    static PageFragment newInstance (int page,DatabaseReference db) {
        PageFragment pageFragment = new PageFragment();
        myRef = db;
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setSmiles(inflater,container,savedInstanceState,myRef);
    }
}

abstract class AbsSmiles extends Fragment {
    private static String[] smiles = {"Pepe", "LGBT", "Faces", "Logos", "Minecraft", "PUBG"};
    DatabaseReference myRefSmiles = FirebaseDatabase.getInstance().getReference().child("Smiles");

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name;
    LinearLayout layout;

    int pageNumber;
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    static String getTitle(Context context, int position) {
        return smiles[position];
    }

    protected View setSmiles(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, DatabaseReference myRef) {
        View view1 = inflater.inflate(R.layout.fragment1, null);

        layout = view1.findViewById(R.id.linearLayout);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User contact = dataSnapshot.getValue(User.class);
                        name = contact.getName();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        myRefSmiles.child(String.valueOf(pageNumber + 1)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                View view = getLayoutInflater().inflate(R.layout.item_smiles, null);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams
                        (view1.getWidth() / 5,
                                view1.getWidth() / 5);

                ImageView[] imageViews = {
                        view.findViewById(R.id.smile1),
                        view.findViewById(R.id.smile2),
                        view.findViewById(R.id.smile3),
                        view.findViewById(R.id.smile4),
                        view.findViewById(R.id.smile5)};

                String[] ids = {
                        dataSnapshot.child("1").getValue(String.class),
                        dataSnapshot.child("2").getValue(String.class),
                        dataSnapshot.child("3").getValue(String.class),
                        dataSnapshot.child("4").getValue(String.class),
                        dataSnapshot.child("5").getValue(String.class)};

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Mess mess = new Mess();
                        mess.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
                        mess.setUs(name);
                        mess.setType("smile");
                        mess.setUid(user.getUid());
                        DatabaseReference mR = myRef.push();
                        String uid = mR.getKey();
                        mess.setMesuid(uid);
                        switch (view.getId()) {
                            case R.id.smile1:
                                if (imageViews[0].getDrawable() != null) {
                                    mess.setMes(ids[0]);
                                    mR.setValue(mess);
                                }
                                break;
                            case R.id.smile2:
                                if (imageViews[1].getDrawable() != null) {
                                    mess.setMes(ids[1]);
                                    mR.setValue(mess);
                                }
                                break;
                            case R.id.smile3:
                                if (imageViews[2].getDrawable() != null) {
                                    mess.setMes(ids[2]);
                                    mR.setValue(mess);
                                }
                                break;
                            case R.id.smile4:
                                if (imageViews[3].getDrawable() != null) {
                                    mess.setMes(ids[3]);
                                    mR.setValue(mess);
                                }
                                break;
                            case R.id.smile5:
                                if (imageViews[4].getDrawable() != null) {
                                    mess.setMes(ids[4]);
                                    mR.setValue(mess);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                };

                for (int i = 0; i < 5; i++) {
                    Picasso.with(imageViews[i].getContext()).load(ids[i]).into(imageViews[i]);
                    imageViews[i].setVisibility(View.VISIBLE);
                    imageViews[i].setLayoutParams(parms);
                    imageViews[i].setOnClickListener(onClickListener);
                }

                layout.addView(view);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view1;
    }
}
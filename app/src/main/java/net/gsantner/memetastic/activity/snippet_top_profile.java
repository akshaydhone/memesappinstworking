package net.gsantner.memetastic.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.gsantner.memetastic.R;

public class snippet_top_profile extends AppCompatActivity  {


    TextView posts,followers,following,textView6,textView7,textView8,desc;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snippet_top_profile);
        posts=(TextView)findViewById(R.id.tv_posts);
        followers=(TextView)findViewById(R.id.tv_followers);
        following=(TextView)findViewById(R.id.tv_following);
        textView6=(findViewById(R.id.textView6));
        textView7=(findViewById(R.id.textView7));
        textView8=(findViewById(R.id.textView8));
       // desc=(TextView)findViewById(R.id.desc);
        //getSupportActionBar().setTitle("TopProfile");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reff= FirebaseDatabase.getInstance().getReference("Users").child("rameshgmail.com");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bio=dataSnapshot.child("Bio").getValue().toString();
                String email=dataSnapshot.child("Email").getValue().toString();
                String name=dataSnapshot.child("Name").getValue().toString();
                String username=dataSnapshot.child("UserName").getValue().toString();
                desc.setText("Hello");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}


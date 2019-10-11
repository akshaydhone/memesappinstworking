package net.gsantner.memetastic.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.github.gsantner.memetastic.R;

public class CreateGroup extends AppCompatActivity {

    TextView creategroup;
    EditText searchmember;
    ListView list_of_members;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String>list;
    ArrayAdapter<String>adapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  user=new User();

        creategroup=(TextView)findViewById(R.id.creategroup);
        searchmember=(EditText)findViewById(R.id.searchmember);
        list_of_members=(ListView)findViewById(R.id.list_of_members);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("Users");




        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.user_info,R.id.users,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
           user=ds.getValue(User.class);
             list.add(user.getName());
                }
       list_of_members.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}

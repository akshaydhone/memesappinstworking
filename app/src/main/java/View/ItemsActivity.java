package View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.gsantner.memetastic.App;
import net.gsantner.memetastic.activity.MainActivity;
import net.gsantner.memetastic.data.MemeData;
import net.gsantner.memetastic.service.AssetUpdater;
import net.gsantner.memetastic.util.AppSettings;
import net.gsantner.memetastic.util.PermissionChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Adapter.RecyclerAdapter;
import Model.Teacher;
//import R;
import butterknife.BindView;
import io.github.gsantner.memetastic.R;
import net.gsantner.memetastic.activity.MainActivity;



public class ItemsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Teacher> mTeachers;
    private int _currentMainMode = 0;
    private static final String BOTTOM_NAV_POSITION = "bottom_nav_position";


    @BindView(R.id.main__activity__list_empty__text)
    TextView _emptylistText;

    @BindView(R.id.toolbar)
    Toolbar _toolbar;


    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView _bottomNav;

    App app;
    private AppSettings _appSettings;


    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        startActivity(intent);
    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_items );

        if (savedInstanceState != null) {
            _currentMainMode = savedInstanceState.getInt(BOTTOM_NAV_POSITION);
        }
        //setSupportActionBar(_toolbar);
        //getSupportActionBar().setTitle("Posts");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter (ItemsActivity.this, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ItemsActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("teachers_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTeachers.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher upload = teacherSnapshot.getValue(Teacher.class);
                    upload.setKey(teacherSnapshot.getKey());
                    mTeachers.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        );







       BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                List<MemeData.Image> imageList = null;
                switch(menuItem.getItemId())
                {
                    case R.id.nav_mode_create:
                        //Intent i=new Intent(getApplicationContext(),ItemsActivity.class);
                        //startActivity(i);
                        break;


                    case R.id.nav_mode_favs:

//                        _toolbar.setTitle(R.string.favs);
                        //Toast.makeText(ItemsActivity.this, "My memes star", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_mode_saved:


                        break;
                        //Intent k=new Intent(getApplicationContext(),CallsToAttend.class);
                        //startActivity(k);




                    case R.id.nav_mode_hidden:
                        //Intent l=new Intent(getApplicationContext(),CallsToAttend.class);
                        //startActivity(l);
                        break;

                   case R.id.nav_more:
                       // Intent m=new Intent(getApplicationContext(),ViewProfile.class);
                        //startActivity(m);
                       //_currentMainMode = 4;
                       //_toolbar.setTitle(R.string.more);
                        break;
                }
                return true;
            }
        });



    }



    public void onItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Teacher selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}


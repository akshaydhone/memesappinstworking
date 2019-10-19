package com.dannproductions.instaclone.Home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//import com.dannproductions.instaclone.R;
import com.dannproductions.instaclone.Utils.BottomNavigationViewHelper;
import com.dannproductions.instaclone.Utils.CheckPermissions;
import com.dannproductions.instaclone.Utils.SectionPagerAdapter;
import com.dannproductions.instaclone.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.gsantner.memetastic.activity.MemeCreateActivity;
import net.gsantner.memetastic.util.ActivityUtils;
import net.gsantner.memetastic.util.PermissionChecker;
import net.gsantner.opoc.util.FileUtils;

import java.io.IOException;
import java.util.Objects;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import io.github.gsantner.memetastic.R;

import static net.gsantner.memetastic.activity.MainActivity.REQUEST_LOAD_GALLERY_IMAGE;
import static net.gsantner.memetastic.activity.MainActivity.REQUEST_TAKE_CAMERA_PICTURE;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private String cameraPictureFilepath = "";
    private static final int ACTIVITY_NO = 0;
    private Context mContext = HomeActivity.this;
    private final int camFragmentPos = 0;
    private final String[] permissionList =  {
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();

    }


    /**
     * Responsible for adding the 3 tabs: Camera, Home, Messages
     */
    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(HomeActivity.this.getSupportFragmentManager());
        adapter.addFragment(new CameraFragment()); //index 0
        adapter.addFragment(new HomeFragment());  //index 1
        adapter.addFragment(new MessagingFragment()); //index 2
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_action_camera);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.instagram_logo);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_action_message);
        viewPager.setCurrentItem(1,false);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==camFragmentPos){

                    if(Build.VERSION.SDK_INT >= 23&&!CheckPermissions.hasPermissions(mContext,permissionList)) {
                        ActivityCompat.requestPermissions(HomeActivity.this,permissionList, 0);
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }



    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NO);
        menuItem.setChecked(true);

    }


    private void initImageLoader(){

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(HomeActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if (requestCode==0&&!CheckPermissions.hasPermissions(mContext, permissionList)) {
                    finish();
                }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main__menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();


        if(id==R.id.action_picture_from_camera){


            showCameraDialog();
            return true;
        }

        else if(id==R.id.action_picture_from_gallery){
            if (PermissionChecker.doIfPermissionGranted(this))
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ActivityUtils.get(this).animateToActivity(i, false, REQUEST_LOAD_GALLERY_IMAGE);
            }
            return true;

        }

        else{
            Toast.makeText(mContext, "Search func", Toast.LENGTH_SHORT).show();

        }


        return super.onOptionsItemSelected(item);
    }


    public void showCameraDialog(){

        if (!PermissionChecker.doIfPermissionGranted(this)) {
            return;

        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try {
                // Create an image file name
                String imageFileName = getString(R.string.app_name) + "_" + System.currentTimeMillis();
                File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM), "Camera");
                photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

                // Save a file: path for use with ACTION_VIEW intents
                cameraPictureFilepath = photoFile.getAbsolutePath();

            } catch (IOException ex) {
                ActivityUtils.get(this).showSnackBar(R.string.error_cannot_start_camera, false);
            }


            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri uri = FileProvider.getUriForFile(this, getString(R.string.app_fileprovider), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }
                ActivityUtils.get(this).animateToActivity(takePictureIntent, false, REQUEST_TAKE_CAMERA_PICTURE);

            }

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        if (requestCode == REQUEST_TAKE_CAMERA_PICTURE) {
            if (resultCode == RESULT_OK) {

                onImageTemplateWasChosen(cameraPictureFilepath);
            } else {
                ActivityUtils.get(this).showSnackBar(R.string.error_picture_selection, false);
            }


        }





        if (requestCode == REQUEST_LOAD_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                String picturePath = null;

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    for (String column : filePathColumn) {
                        int curColIndex = cursor.getColumnIndex(column);
                        if (curColIndex == -1) {
                            continue;
                        }
                        picturePath = cursor.getString(curColIndex);
                        if (!TextUtils.isEmpty(picturePath)) {
                            break;
                        }


                    }
                    cursor.close();
                }

                // Retrieve image from file descriptor / Cloud, e.g.: Google Drive, Picasa
                if (picturePath == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
                        if (parcelFileDescriptor != null) {
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            FileInputStream input = new FileInputStream(fileDescriptor);
                            // Create temporary file in cache directory
                            picturePath = File.createTempFile("image", "tmp", getCacheDir()).getAbsolutePath();
                            FileUtils.writeFile(
                                    new File(picturePath),
                                    FileUtils.readCloseBinaryStream(input)
                            );
                        }
                    } catch (IOException e) {
                        // nothing we can do here, null value will be handled below
                    }
                }

                // Finally check if we got something
                if (picturePath == null) {

                    ActivityUtils.get(this).showSnackBar(R.string.error_couldnot_load_picture_from_storage, false);
                }
                else {
                    onImageTemplateWasChosen(picturePath);
                }
            }

        }
    }

    public void onImageTemplateWasChosen(String filePath) {
        final Intent intent = new Intent(this, MemeCreateActivity.class);
        intent.putExtra(MemeCreateActivity.EXTRA_IMAGE_PATH, filePath);
        ActivityUtils.get(this).animateToActivity(intent, false, MemeCreateActivity.RESULT_MEME_EDITING_FINISHED);
    }



}

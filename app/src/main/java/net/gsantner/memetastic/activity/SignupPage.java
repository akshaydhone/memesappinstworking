package net.gsantner.memetastic.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import io.github.gsantner.memetastic.R;

public class SignupPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password,name,bio,fullname;
    private TextView tvForgot;
    private Button signin, signup,signuppage;
    FirebaseUser user;
    static String LoggedIn_User_Email;
    public static FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }



        mAuth = FirebaseAuth.getInstance(); // important Call

        signin = (Button)findViewById(R.id.signin);
        signuppage = (Button)findViewById(R.id.signuppage);



        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);
        name = (EditText)findViewById(R.id.etName);
        bio=(EditText)findViewById(R.id.etBio);
        fullname=(EditText)findViewById(R.id.etFullName);
        tvForgot=(TextView)findViewById(R.id.tvForgot);



        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(i);
            }
        });

        signuppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),LoginPage.class);
                startActivity(i);
            }
        });


        /*if(mAuth.getCurrentUser() != null)
        {
            //User NOT logged In
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }*/


        user = mAuth.getCurrentUser();
        Log.d("LOGGED", "user: " + user);


        //Setting the tags for Current User.
        if (user != null) {
            LoggedIn_User_Email =user.getEmail();
        }



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail = email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();


                if(getemail.isEmpty()){
                    email.setError("Enter Email address");
                    email.requestFocus();
                    //Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getepassword.isEmpty()){
                    password.setError("Enter Password");
                    password.requestFocus();
                   // Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }




                callsignin(getemail,getepassword);

            }
        });






        /*signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getemail = email.getText().toString().trim();
                String getepassword = password.getText().toString().trim();
                String getbio=bio.getText().toString().trim();
                String getfullname=fullname.getText().toString().trim();

                if(getemail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getepassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getepassword.length()<6){
                    Toast.makeText(getApplicationContext(), "Password should minimum 6 Character", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(getbio.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Bio", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(getfullname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                callsignup(getemail,getepassword);

                // Intent i=new Intent(getApplicationContext(),SignupPage.class);
                //startActivity(i);

            }

        });*/


        //Create Account



    }




    //Create Account
    private void callsignup(String email,String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signed up Failed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            userProfile();


                            FirebaseUser user = mAuth.getCurrentUser();
                            String UserID=user.getEmail().replace("@","").replace(".","");
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

                            DatabaseReference ref1= mRootRef.child("Users").child(UserID);

                            ref1.child("Name").setValue(fullname.getText().toString().trim());
                            ref1.child("UserName").setValue(name.getText().toString().trim());
                            ref1.child("Password").setValue(password.toString().trim());
                            ref1.child("Bio").setValue(bio.getText().toString().trim());
                            ref1.child("Email").setValue(user.getEmail());
                            ref1.child("Image_Url").setValue("Null");







                            Log.d("TESTING", "Sign up Successful" + task.isSuccessful());
                            Toast.makeText(getApplicationContext(), "Account Created ", Toast.LENGTH_SHORT).show();
                            Log.d("TESTING", "Created Account");
                        }
                    }
                });
    }



    //Set UserDisplay Name
    private void userProfile()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString().trim())
                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))  // here you can set image link also.
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile updated.");
                            }
                        }
                    });
        }
    }
    private void callsignin(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "sign In Successful:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TESTING", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "Check Your Credentials/No Network", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(i);
                        }
                    }
                });

    }


}

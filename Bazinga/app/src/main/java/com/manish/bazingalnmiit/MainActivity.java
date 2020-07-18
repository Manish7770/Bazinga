package com.manish.bazingalnmiit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.Model.UserApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView slogan,home,warning;
    private LinearLayout signin;
    private String email,name,uid;
    private SignInButton signInButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN=9001;

    private static int TIME_OUT = 2000;

    ProgressBar progressBar;

    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        home= findViewById(R.id.home);
        slogan= findViewById(R.id.slogan);
        progressBar=findViewById(R.id.progressmain);
        progressBar.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));
        }

        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        home.setTypeface(typeface);
        slogan.setTypeface(typeface);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mAuth=FirebaseAuth.getInstance();

        Commondata.firebaseAuth=mAuth;
        Commondata.googleSignInClient=mGoogleSignInClient;

        signInButton= findViewById(R.id.signinbutton);
        signin = findViewById(R.id.signin);
        signInButton.setOnClickListener(this);

        signInButton.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signinbutton:
                signIn();
                break;
        }

    }

    public void signOut() {
        Commondata.firebaseAuth.signOut();

        Commondata.googleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

    }

    private void signIn()
    {
        if (Commondata.isConnectedToInternet(getBaseContext())) {
            mdialog = new ProgressDialog(MainActivity.this);
            mdialog.setMessage("Loading...");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            mdialog.show();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else 
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                //Log.w("Googlesigninfailed","Google sign in failed", e);
                mdialog.dismiss();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
        {
            progressBar.setVisibility(View.VISIBLE);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = database.getReference("UserApp");

            databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, navigationhome.class);
                                Commondata.currentUID=currentUser.getUid();
                                Commondata.currentUser.setEmail(currentUser.getEmail());
                                Commondata.currentUser.setName(currentUser.getDisplayName());
                                if (dataSnapshot.child("address").exists()) {
                                    Commondata.currentUser.setAddress(dataSnapshot.child("address").getValue().toString());
                                }
                                else
                                {
                                    Commondata.currentUser.setAddress("");
                                }
                                if (dataSnapshot.child("phoneNumber").exists()) {
                                    Commondata.currentUser.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
                                }
                                else
                                {
                                    Commondata.currentUser.setPhoneNumber("");
                                }
                                startActivity(intent);
                                finish();
                            }
                        },TIME_OUT);
                    }
                    else
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        signInButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       // Log.d("firebaseauth", "firebaseAuthWithGoogle:" + acct.getId());
        if (Commondata.isConnectedToInternet(getBaseContext())) {

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = database.getReference("UserApp");
                            //DatabaseReference userweb=database.getReference("User");

                            if (task.isSuccessful()) {
                               // Log.d("successfull login", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                email = user.getEmail().toLowerCase();
                                if (email!=null&&(!email.isEmpty())) {
                                    email = user.getEmail().toLowerCase();
                                    name = user.getDisplayName();
                                    uid = user.getUid();

                                    databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {
                                                Commondata.currentUID=uid;
                                                Commondata.currentUser.setEmail(email);
                                                Commondata.currentUser.setName(name);
                                                if (dataSnapshot.child("address").exists()) {
                                                    Commondata.currentUser.setAddress(dataSnapshot.child("address").getValue().toString());
                                                }
                                                else
                                                {
                                                    Commondata.currentUser.setAddress("");
                                                }
                                                if (dataSnapshot.child("phoneNumber").exists()) {
                                                    Commondata.currentUser.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
                                                }
                                                else
                                                {
                                                    Commondata.currentUser.setPhoneNumber("");
                                                }

                                            } else if (!(dataSnapshot.exists())){
                                                UserApp user2 = new UserApp("",email, name,"");
                                                databaseReference.child(uid).setValue(user2);
                                                Commondata.currentUID=uid;
                                                Commondata.currentUser.setEmail(email);
                                                Commondata.currentUser.setName(name);
                                                Commondata.currentUser.setAddress("");
                                                Commondata.currentUser.setPhoneNumber("");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    Commondata.currentUID=uid;
                                    Commondata.currentUser.setEmail(email);
                                    Commondata.currentUser.setName(name);
                                    Intent intent = new Intent(MainActivity.this, navigationhome.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                }
                           } else {

                            }
                            mdialog.dismiss();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
            mdialog.dismiss();
        }
    }
}

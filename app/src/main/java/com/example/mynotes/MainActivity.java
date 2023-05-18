package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin, mgotosingnup;
    private TextView mgotoforgetpassword;

    public FirebaseAuth firebaseAuth;

    ProgressBar mprogressbarofmainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        mloginemail = findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        mgotoforgetpassword=findViewById(R.id.gotoforgetpassword);
        mgotosingnup=findViewById(R.id.gotosignup);
        mprogressbarofmainactivity=findViewById(R.id.progressbarofmainactivity);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, notesactivity.class));
        }


        mgotosingnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });

        mgotoforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgetpassword.class));
            }
        });


        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);


                    //Login the user
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                checkmailverfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Account Doesn't Exist",Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }


                        }
                    });
                }
            }
        });
    }

    private void checkmailverfication()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        if(firebaseUser.isEmailVerified())
        {
            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, notesactivity.class));
        }

        else
        {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify your Email First",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

}
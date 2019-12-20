package com.example.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Model.model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtUser, edtPassword; //username
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail; //new users

    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        database = FirebaseDatabase.getInstance();
        Users = database.getReference("Users");

        edtUser = (MaterialEditText)findViewById(R.id.edtUsername);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button)findViewById(R.id.btn_sign_up);
        btnSignIn = (Button)findViewById(R.id.btn_sign_in);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sign Up",Toast.LENGTH_SHORT).show();
                SignUpDialog();
        }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Sign In",Toast.LENGTH_SHORT).show();
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });

    }

    private void signIn(final String username, final String password) {
        Users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()){
                    if(!username.isEmpty()){
                        model login = dataSnapshot.child(username).getValue(model.class);
                        if (login.getPassword().equals(password)){
                            Intent homeActivity = new Intent(MainActivity.this,Home.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();
                            //Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please Enter Your Username", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "User Does not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "data login error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void SignUpDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Sign Up");
        alert.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);

        edtNewUser =  (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail =  (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword =  (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewPassword);

        alert.setView(sign_up_layout);
        alert.setIcon(R.drawable.ic_launcher_background);

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final model user = new model(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                Users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUsername()).exists()) {
                            Toast.makeText(MainActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            Users.child(user.getUsername())
                                    .setValue(user);
                            Toast.makeText(MainActivity.this, "User Registration Success", Toast.LENGTH_SHORT).show();
                        }
                        System.out.println("EAAAAAAAAAAAA!!!!!!!");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "data sign up error", Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
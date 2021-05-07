package com.example.myregistrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText name, password;
    Button Login;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        Login = findViewById(R.id.Login);
        register = findViewById(R.id.register);

        // Once clicked, goes to the SignUp screen

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });


    }
    private Boolean validateName(){
        String val = name.getText().toString();

        if (val.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }
        else {
            name.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = password.getText().toString().trim();

        if (val.isEmpty()){
            password.setError("Field Cannot be empty");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    private void SignIn() {

        if (!validateName() | !validatePassword()){
            return;
        }
        else {
            isUser();
    }

}

    private void isUser() {
        final String userEnteredUsername = name.getText().toString().trim();
        final String userEnteredPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("name").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    name.setError(null);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){

                        name.setError(null);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        password.setError("Wrong Password!");
                        password.requestFocus();
                    }
                }
                else {
                    name.setError("No such User exists");
                    name.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    }
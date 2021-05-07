package com.example.myregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    EditText regName, regPassword, regEmail, regPhone;  //creating variables
    Button regRegister;
    TextView regLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        regName = findViewById(R.id.regName);
        regPassword = findViewById(R.id.regPassword);
        regPhone = findViewById(R.id.regPhone);
        regEmail = findViewById(R.id.regEmail);
        regRegister = findViewById(R.id.regRegister);
        regLogin = findViewById(R.id.regLogin);

// Once clicked, it takes us back to the Login screen/MainActivity.....

        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rootNode = FirebaseDatabase.getInstance(); // Calling the rootNode in the database with all the elements..
                reference = rootNode.getReference("users"); // defines which reference we want to call

                //Get all the values

                String name = regName.getText().toString();
                String email = regEmail.getText().toString();
                String phone = regPhone.getText().toString();
                String password = regPassword.getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name,phone,email,password);
                reference.child(name).setValue(helperClass); //Helper class stores the data of our users

                registerUser();

            }
        });
    }

    private Boolean validateName(){
        String val = regName.getText().toString().trim();
        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }
        else {
            regName.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = regPhone.getText().toString().trim();
        if (val.isEmpty()){
            regPhone.setError("Field Cannot be empty");
            return false;
        }
        else {
            regPhone.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getText().toString().trim();
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            regEmail.setError("Field Cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            regEmail.setError("invalid email address");
            return false;
        }
        else {
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getText().toString().trim();
        String passwordVal = "^" +


                //"(?=.*[a-zA-Z])" +              //any letter
                "(?=.*[a-z])"+                  //at least one lower case
                "(?=.*[A-Z])"+                  //at least one upper case
                "(?=.*[0-9])"+                  //at least one digit
                "(?=.*[@#$%^&+=])" +            //at least one special character
                "(?=\\S+$)" +                   //no white spaces
                ".{5,}";                        //at least 5 characters

        if (val.isEmpty()){
            regPassword.setError("Field Cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        }

        else {
            regPassword.setError(null);
            return true;
        }
    }


//Save data in the Firebase once the register button is clicked...
    private void registerUser() {
        if(!validateName() |!validatePassword() | !validatePhoneNo() | !validateEmail())
        {
            return;
        }

        String name = regName.getText().toString();
        String email = regEmail.getText().toString();
        String phone = regPhone.getText().toString();
        String password = regPassword.getText().toString();

        UserHelperClass helperClass = new UserHelperClass(name,phone,email,password);
        reference.child(phone).setValue(helperClass);

        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();

    }
}
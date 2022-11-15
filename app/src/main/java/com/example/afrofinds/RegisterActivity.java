package com.example.afrofinds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
public class RegisterActivity extends AppCompatActivity {

    //create object of DatabaseReference class to access firebase Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance
            ("https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mydiet-c351f-default-rtdb.asia-southeast1.firebasedatabase.app");
    EditText name, pass, confirmPass, contact, email;
    Button signBtn;
    TextView loginTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        confirmPass = findViewById(R.id.confirmPass);
        contact = findViewById(R.id.contact);
        loginTxt = findViewById(R.id.loginTxt);
        signBtn = findViewById(R.id.signBtn);


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from editText into String variables
                String user = name.getText().toString();
                String phone = contact.getText().toString();
                String password = pass.getText().toString();
                String repass = pass.getText().toString();
                String mail = email.getText().toString();


                if (user.equals("") || phone.equals("") || mail.equals("") || password.equals("") || repass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter all the fields",
                            Toast.LENGTH_SHORT).show();
                }

                else if (!password.equals(repass)) {
                    Toast.makeText(RegisterActivity.this, "Passwords not matching",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String a = snapshot.toString();
                            Log.e("Fire DB", a );
                            //check email is not registered before
                            if(snapshot.hasChild(user)){
                                Toast.makeText(RegisterActivity.this, "User already registered",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //sending data to firebase realtime Database
                                //using email as the unique identity to every user
                                //all other details of user come under email
                                databaseReference.child("Users").child(user).child("username").setValue(user);
                                databaseReference.child("Users").child(user).child("password").setValue(password);
                                databaseReference.child("Users").child(user).child("email").setValue(mail);
                                databaseReference.child("Users").child(user).child("PhoneNumber").setValue(phone);

                                Intent i =  new Intent(RegisterActivity.this, LoginActivity.class);
                                i.putExtra("src", "registerActivity");
                                startActivity(i);

                                //show a success message then finish activity
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                //  finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // finish();
            }
        });
    }
}
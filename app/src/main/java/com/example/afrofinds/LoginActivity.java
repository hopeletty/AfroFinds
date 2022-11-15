package com.example.afrofinds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class LoginActivity extends AppCompatActivity {
    EditText loginName, password;;
    Button loginBtn;
    TextView createAccount;

    static DatabaseReference databaseReference = FirebaseDatabase.getInstance
            ("https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login);

        // username = findViewById(R.id.username);
        loginName = findViewById(R.id.loginName);
        password = findViewById(R.id.password);
        createAccount = findViewById(R.id.createAccount);
        loginBtn = findViewById(R.id.loginBtn);



        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = loginName.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check contact is not registered before
                            if (snapshot.hasChild(user)) {
                                //username exists in firebase
                                //get password of user from firebase and match it with user password entered
                                String getPassword = snapshot.child(user).child("password").getValue(String.class);

                                if (getPassword.equals(pass)) {

                                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("afrofinds_settings", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("signedIn", true);
                                    editor.putString("loginName", user );
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

                                    String src =  "";
                                    if (savedInstanceState == null) {
                                        Bundle extras = getIntent().getExtras();
                                        src = extras.getString("src");
                                    }

                                    //open MainActivity on success
                                    //or itemactivity
                                    Intent intent = getActIntent(src);

                                    startActivity(intent) ;
                                    //  finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                Toast.makeText(LoginActivity.this, "User not registered", Toast.LENGTH_SHORT).show();
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }

        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    Intent getActIntent(String src){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

        if("itemActivity".equals(src ))
            intent = new Intent(LoginActivity.this, ItemActivity.class);

        else if("registerActivity".equals(src ))
            intent = new Intent(LoginActivity.this, RegisterActivity.class);

        else if("profileFragment".equals(src ))
            intent = new Intent(LoginActivity.this, ProfileFragment.class);

        return intent;

    }
}

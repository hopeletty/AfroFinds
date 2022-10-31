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

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RegisterActivity extends AppCompatActivity {
    EditText name, pass, mail, contact;
    Button signBtn;
    TextView loginTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        mail = findViewById(R.id.mail);
        contact = findViewById(R.id.contact);
        loginTxt = findViewById(R.id.loginTxt);
        signBtn = findViewById(R.id.signBtn);

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, email, phonenumber;
                username = String.valueOf(name.getText());//get the value from editText
                password = String.valueOf(pass.getText());
                email = String.valueOf(mail.getText());
                phonenumber = String.valueOf(contact.getText());

                //check if there is a value in the editText
                if(!username.equals("")&& !email.equals("")&& !phonenumber.equals("")&& !password.equals("")){
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "username";
                            field[1] = "email";
                            field[2] = "phonenumber";
                            field[3] = "password";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = username;
                            data[1] = email;
                            data[2] = phonenumber;
                            data[3] = password;
                            //String url = "https://192.168.123.194/LoginRegister/signup.php";
                            String url = "http://10.0.2.2/LoginRegister/signup.php";
                            PutData putData = new PutData(url, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")) { //checking the result
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

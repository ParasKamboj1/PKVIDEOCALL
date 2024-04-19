package com.example.pkvideocall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    EditText email,password,username;
    Button signup,alreadyaccount;
    FirebaseDatabase database;

    DatabaseReference reference;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.passwordid);
        username = findViewById(R.id.usernameid);
        signup = findViewById(R.id.createanaccountid);
        alreadyaccount = findViewById(R.id.alreadyhaveanaccountid);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString();
                String pass = password.getText().toString();
                String use = username.getText().toString();

                if(em.isEmpty()){
                    email.setError("Fill the Email!");
                    Toast.makeText(signup.this, "Fill the Email!", Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty()){
                    password.setError("Fill the password!");
                    Toast.makeText(signup.this, "Fill the password!", Toast.LENGTH_SHORT).show();
                }
                else if(use.isEmpty()){
                    username.setError("Fill the username!");
                    Toast.makeText(signup.this, "Fill the username!", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6){
                    password.setError("Password must be contain atleast 6 characters!");
                    Toast.makeText(signup.this, "Password must be contain atleast 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                User user = new User(use,em,pass);
                                String userId = auth.getCurrentUser().getUid();
                                reference = database.getReference("users");
                                reference.child(userId).setValue(user);

                                Toast.makeText(signup.this, "Account is created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(signup.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    Button registerButton;
    TextView goToSignin;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String TAG = "REGISTER_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        goToSignin = findViewById(R.id.hasAnAccount);
        registerButton = findViewById(R.id.register_button);
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Register.this, Questionnaire.class);
            startActivity(intent);
            finish();
        }

        goToSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String pwd = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("Email input is required");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    passwordInput.setError("Password input is required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    nameInput.setError("Name input is required");
                    return;
                }

                createUserWithEmailAndPwd(name, email, pwd);
            }
        });
    }

    private void createUserWithEmailAndPwd(String name, String email, String pwd) {
        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserToFirestore(user.getUid(), name, email);
                    }
                    Toast.makeText(Register.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Questionnaire.class);
                    startActivity(intent);
                } else {
                    handleAuthError(task.getException());
                }
            }
        });
    }

    private void saveUserToFirestore(String userId, String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User added to firestore"))
                .addOnFailureListener(e -> Log.w(TAG, "error adding user to firestore", e));
    }

    private void handleAuthError(Exception e) {
        // If sign in fails, display a message to the user
        if (e != null) {
            Log.w("Register", "createUserWithEmail:failure", e);
            if (e instanceof FirebaseAuthUserCollisionException) {
                Log.e("Register", "User already exists");
                Toast.makeText(this, ((FirebaseAuthUserCollisionException) e).getEmail() + " User Already Exists.", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e("Register", "Invalid credentials");
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthWeakPasswordException) {
                Log.e("Register", "Weak password");
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthActionCodeException) {
                Log.e("Register", "Action code error");
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthRecentLoginRequiredException) {
                Log.e("Register", "Recent login required");
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Register", "Authentication failed: " + e.getMessage());
            }
        }
    }
}
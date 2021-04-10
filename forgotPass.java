package com.daat.productivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPass extends AppCompatActivity {

    private Button resetBtn;
    private EditText resetEmail;
    private ProgressBar progressBar;
    private TextView back;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        getSupportActionBar().hide();

        resetBtn = findViewById(R.id.resetbutton);
        resetEmail = findViewById(R.id.resetTextEmailAddress);
        progressBar = findViewById(R.id.resetProgress);
        back = findViewById(R.id.goBack);
        firebaseAuth = FirebaseAuth.getInstance();

        resetEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chkemail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgotPass.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBtn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(resetEmail.getText()
                        .toString()).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(forgotPass.this, "Link sent to your registered email check your inbox", Toast.LENGTH_LONG).show();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(forgotPass.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        resetBtn.setEnabled(true);
                    }
                });
            }
        });

    }

    private void chkemail() {
       if (TextUtils.isEmpty(resetEmail.getText().toString())) {
           resetBtn.setEnabled(false);
       }
       else{
           resetBtn.setEnabled(true);
           resetBtn.setTextColor(Color.parseColor("#FFFFFF"));
       }
    }

}

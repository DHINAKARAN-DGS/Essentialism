package com.daat.productivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class contactUsActivity extends AppCompatActivity {

    private EditText name, number, reason;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact us");

        name = findViewById(R.id.nameContact);
        number = findViewById(R.id.numberContact);
        reason = findViewById(R.id.discriptionContact);
        submit = findViewById(R.id.submitContact);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> data = new HashMap<>();
                data.put("Name", name.getText().toString());
                data.put("Number", number.getText().toString());
                data.put("Issue", reason.getText().toString());
                data.put("user ID", FirebaseAuth.getInstance().getUid().toString());

                FirebaseFirestore.getInstance()
                        .collection("CONTACT US")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(contactUsActivity.this, "Will soon get back to you", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(contactUsActivity.this, "Error", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
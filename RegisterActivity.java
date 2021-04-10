package com.daat.productivity;


import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class RegisterActivity extends AppCompatActivity {
    private FrameLayout regLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regLayout = findViewById(R.id.REGISTER_FRAMElAYOUT);
        setFragment(new SignInFragment());

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(regLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


}
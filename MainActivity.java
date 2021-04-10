package com.daat.productivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.annotation.Annotation;

import static maes.tech.intentanim.CustomIntent.customType;


public class MainActivity extends AppCompatActivity
{

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();


        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(1500);
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }

                finally
                {
                    if (firebaseAuth.getCurrentUser()!=null) {
                        startActivity(new Intent(MainActivity.this,DashboardActivity.class));
//                        customType(MainActivity.this,"fadein-to-fadeout");
                    }
                    else {
                        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                        customType(MainActivity.this,"bottom-to-up");
                    }
                }
            }
        };
        thread.start();

    }
             //*left-to-right
//*right-to-left
//*bottom-to-up
//*up-to-bottom
//*fadein-to-fadeout
//*rotateout-to-rotatein

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }
}
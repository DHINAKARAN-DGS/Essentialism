package com.daat.productivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DashboardActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    MeowBottomNavigation bottomNavigation;
    Button setTime;
    static NotificationManager mNotificationManager;
    boolean done = false;

    public static boolean alarmOn = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        bottomNavigation = findViewById(R.id.bottom_nav);

        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat2.format(Calendar.getInstance().getTime());

        DateFormat dateFormat5 = new SimpleDateFormat("hh:mm:ss");
        String ct = dateFormat5.format(Calendar.getInstance().getTime());


        setDnd();
        chkDb(strDate);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_signal_cellular_alt_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_account_circle_24));


        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()) {
                    case 1:
                        fragment = new ReportFragment();
                        break;
                    case 2:
                        fragment = new HomeFragment();
                        break;
                    case 3:
                        fragment = new AccountFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });
        bottomNavigation.show(2, true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.dash_frame_layout, fragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat2.format(Calendar.getInstance().getTime());

        DateFormat dateFormat5 = new SimpleDateFormat("hh:mm:ss");
        String ct = dateFormat5.format(Calendar.getInstance().getTime());
        cancelAlarm(strDate, ct);
        removeDnd(DashboardActivity.this);
    }

    private void setDnd() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
        }
        if (mNotificationManager.isNotificationPolicyAccessGranted()) {
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
        }
    }

    public void cancelAlarm(String strDate, String ct) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DashboardActivity.this, AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);

        if (alarmOn) {

            FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("HISTORY").document(strDate).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                long listsize = task.getResult().getLong("list_size");
                                String stime = task.getResult().getString("AlarmTime_" + listsize);
                                if (stime.compareTo(ct) < 0) {
                                    Map<String, Object> dateMap = new HashMap<>();
                                    dateMap.put("StoppetAt_" + listsize, ct);
                                    FirebaseFirestore.getInstance()
                                            .collection("USERS")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("HISTORY").document(strDate).update(dateMap);
                                }
                            }
                        }
                    });
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTime(c);
        startAlarm(c);
    }

    public void startAlarm(Calendar c) {
        alarmOn = true;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(DashboardActivity.this, AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String strTime = dateFormat.format(c.getTime());

        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat2.format(Calendar.getInstance().getTime());

        DateFormat dateFormat3 = new SimpleDateFormat("hh:mm:ss");
        String strTimeC = dateFormat3.format(Calendar.getInstance().getTime());

        DbConnect(strDate, strTimeC, strTime);
        System.out.println(strTime + "CT" + Calendar.getInstance().getTime());

    }

    private void chkDb(String strDate) {
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HISTORY").document(strDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.getResult().exists()) {
                    Map<String, Object> dateMap = new HashMap<>();
                    dateMap.put("list_size", 0);
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("HISTORY").document(strDate).set(dateMap);
                }
            }
        });
    }

    private void DbConnect(String strDate, String strTimeC, String strTime) {

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HISTORY").document(strDate).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    long list_size = task.getResult().getLong("list_size");
                    long nls = list_size + 1;
                    Map<String, Object> dateMap = new HashMap<>();
                    dateMap.put("CurrentTime_" + nls, strTimeC);
                    dateMap.put("AlarmTime_" + nls, strTime);
                    dateMap.put("StoppetAt_" + nls, "--");
                    dateMap.put("list_size", nls);
                    System.out.println(dateMap);
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("HISTORY").document(strDate).update(dateMap);

                }
            }
        });


    }

    public void updateTime(Calendar c) {
        String time = "Alarm set for: ";
        time += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        System.out.println(time);

    }


    public static void removeDnd(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
        Toast.makeText(context, "CCC", Toast.LENGTH_SHORT).show();
    }
}

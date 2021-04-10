package com.daat.productivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String[] itemsAll;
    private ListView msongsList;
    private Button setTime, cancel;
    private FragmentActivity myContext;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cancel = view.findViewById(R.id.buttonCancel);
        setTime = view.findViewById(R.id.buttonTime);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragManager = myContext.getSupportFragmentManager(); //If using fragments from support v4
                DialogFragment timeP = new TimePicker();
                timeP.show(fragManager, "Time");
                setDnd();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });


        return view;


    }

    @Override
    public void onPause() {
        super.onPause();
        cancelAlarm();
    }

    private void cancelAlarm() {
        Activity activity = (Activity) getActivity();
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat2.format(Calendar.getInstance().getTime());

        DateFormat dateFormat5 = new SimpleDateFormat("hh:mm:ss");
        String ct = dateFormat5.format(Calendar.getInstance().getTime());
        if (DashboardActivity.alarmOn) {

            FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("HISTORY").document(strDate).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                long listsize = task.getResult().getLong("list_size");
                                String stime = task.getResult().getString("AlarmTime_" + listsize);
                                if (stime.compareTo(ct) > 0) {
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

    private void setDnd() {
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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


    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

    }


}
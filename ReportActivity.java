package com.daat.productivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    String docId ;

    List<String> startTimeList = new ArrayList<>();
    List<String> endTimeList = new ArrayList<>();
    List<String> stoppedTimeList = new ArrayList<>();
    List<progressModel>progressModelList = new ArrayList<>();

    RecyclerView recyclerView;
    progressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        docId = getIntent().getStringExtra("doc");
        recyclerView = findViewById(R.id.reportRV);

        getSupportActionBar().setTitle("Report on "+docId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        adapter = new progressAdapter(progressModelList);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HISTORY").document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                long ls= task.getResult().getLong("list_size");
                for (int x=1;x<ls+1;x++){
                    String startTime = task.getResult().getString("CurrentTime_"+x);
                    String endTime = task.getResult().getString("AlarmTime_"+x);
                    String StoppedTime = task.getResult().getString("StoppetAt_"+x);
                    progressModelList.add(new progressModel(startTime,endTime,StoppedTime));
                    adapter.notifyDataSetChanged();
                }
            }
        });









    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
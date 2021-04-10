package com.daat.productivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class progressAdapter extends RecyclerView.Adapter<progressAdapter.ViewHolder> {

    private List<progressModel> progressModelList;


    public progressAdapter(List<progressModel> progressModelList) {
        this.progressModelList = progressModelList;
    }


    @NonNull
    @Override
    public progressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull progressAdapter.ViewHolder holder, int position) {
        String start = progressModelList.get(position).getStart();
        String end = progressModelList.get(position).getEnd();
        String stop = progressModelList.get(position).getMid();

        holder.setData(start, end, stop);
    }


    @Override
    public int getItemCount() {
        return progressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView s, e;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            s = itemView.findViewById(R.id.startT);
            e = itemView.findViewById(R.id.endT);
        }

        private void setData(String start, String end, String stop) {

            s.setText(start);
            e.setText(end);

            String[] ending = end.split(":");
            String e1 = ending[0];
            String e2 = ending[1];
            String e3 = ending[2];
            int endTime = Integer.parseInt(e1) + Integer.parseInt(e2) + Integer.parseInt(e3);
            int stopTime = 0;
            if (!stop.equals("--")) {
                String[] stoped = stop.split(":");
                String st1 = stoped[0];
                String st2 = stoped[1];
                String st3 = stoped[2];
                stopTime = Integer.parseInt(st1) + Integer.parseInt(st2) + Integer.parseInt(st3);
            }

//            String[] starting = stop.split(":");
//            String s1 = starting[0];
//            String s2 = starting[1];
//            String s3 = starting[2];
//            int startTime = Integer.parseInt(s1)+Integer.parseInt(s2)+Integer.parseInt(s3);


            Date startTimeD = null;
            Date stopTimeD = null;
            Date EndTimeD = null;
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
            try {
                startTimeD = parser.parse(start);
                stopTimeD = parser.parse(stop);
                EndTimeD = parser.parse(end);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }


            progressBar.setMax(100);


            if (stop.equals("--")) {
                progressBar.setProgress(100);
            } else if (stopTimeD.before(EndTimeD)) {
                progressBar.setProgress(45);
            } else if (stopTimeD.after(EndTimeD)) {
                progressBar.setProgress(100);
            }


        }
    }
}

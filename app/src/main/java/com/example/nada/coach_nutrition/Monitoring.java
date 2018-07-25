package com.example.nada.coach_nutrition;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Monitoring extends AppCompatActivity {


    private String authority = "com.example.nada.contentProvider";
    ContentResolver resolver;
    int today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime);
        today = Integer.parseInt(formattedDate);

        resolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("goal_table");
        Uri uri = builder.build();

        Cursor c = resolver.query(uri, null, "date<" + today, null, "date" + " DESC");

        int cpt=0;

        while (c.moveToNext() && cpt <7) {
            int total = c.getInt(c.getColumnIndex("total"));
            int minCal = c.getInt(c.getColumnIndex("min"));
            int maxCal = c.getInt(c.getColumnIndex("max"));
            int date = c.getInt(c.getColumnIndex("date"));

            cpt++;

            final TextView monitorTV = new TextView(this);
            monitorTV.setText(date+ ": Objectif: "+ minCal+"-"+maxCal+"kcal. Consommé: "+total+"kcal.");
            if (minCal<=total && maxCal>= total){
                monitorTV.setTextColor(Color.GREEN);
            }
            else {
                monitorTV.setTextColor(Color.RED);
            }
            LinearLayout monitLL =(LinearLayout)findViewById(R.id.monitoringLayout);
            monitLL.addView(monitorTV);
        }

        if (cpt==0) {
            final TextView monitorTV = new TextView(this);
            monitorTV.setText("Aucune donnée à afficher.");
            LinearLayout monitLL =(LinearLayout)findViewById(R.id.monitoringLayout);
            monitLL.addView(monitorTV);
        }

    }
}

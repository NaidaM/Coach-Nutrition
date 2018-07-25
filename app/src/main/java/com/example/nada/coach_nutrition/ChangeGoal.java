package com.example.nada.coach_nutrition;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChangeGoal extends AppCompatActivity {

    String min;
    String max;
    private String authority = "com.example.nada.contentProvider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_goal);
    }

    public void change (View view){
        EditText etmin = (EditText) findViewById(R.id.minCal);
        min = etmin.getText().toString();
        EditText etmax = (EditText) findViewById(R.id.maxCal);
        max = etmax.getText().toString();
        if (min =="" || max == "" ) Toast.makeText(this, "Valeurs incorrectes.", Toast.LENGTH_LONG).show();
        else {
            try {
                int minF = Integer.parseInt(min);
                int maxF = Integer.parseInt(max);
                if (minF < 0 || minF > 10000) {
                    Toast.makeText(this, "Valeur minimum incorrecte.", Toast.LENGTH_LONG).show();
                    etmin.setText("");
                }
                if (maxF < 0 || maxF > 10000) {
                    Toast.makeText(this, "Valeur maximum incorrecte.", Toast.LENGTH_LONG).show();
                    etmax.setText("");
                }

                if (minF > maxF) {
                    Toast.makeText(this, "Valeurs incorrectes.", Toast.LENGTH_LONG).show();
                } else if (minF <= maxF && minF >= 0 && maxF >= 0 && minF <= 10000 && maxF <= 10000) {
                    Intent intentOver = new Intent();
                    intentOver.putExtra("minCal", minF);
                    intentOver.putExtra("maxCal", maxF);
                    ContentResolver resolver = getContentResolver();

                    try {
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                        String formattedDate = df.format(currentTime);
                        int date = Integer.parseInt(formattedDate);

                        ContentValues values = new ContentValues();
                        values.put("min", minF);
                        values.put("max", maxF);

                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("content").authority(authority).appendPath("goal_table");
                        Uri uri = builder.build();

                        Cursor c = resolver.query(uri, null, "date="+date, null, null);

                        if (c.moveToFirst()){
                            long id =c.getLong(c.getColumnIndex("_id"));
                            ContentUris.appendId(builder, id);
                            Uri urii = builder.build();
                            int res = resolver.update(urii, values, null, null);
                        }

                        else {
                            values.put("date", date);
                            values.put("total", 0);
                            uri = resolver.insert(uri,values);
                        }
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }

                    setResult(RESULT_OK, intentOver);
                    finish();

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
}

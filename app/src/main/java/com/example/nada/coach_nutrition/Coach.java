package com.example.nada.coach_nutrition;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Coach extends AppCompatActivity {

    protected final int ADDMEAL_REQ_CODE = 0;
    protected final int CHGOAL_REQ_CODE = 1;
    private String authority = "com.example.nada.contentProvider";
    ContentResolver resolver;
    int today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime);
        today = Integer.parseInt(formattedDate);

        resolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("goal_table");
        Uri uri = builder.build();

        Cursor c = resolver.query(uri, null, "date=" + today, null, null);

        if (c.moveToFirst()) {
            int minCal = c.getInt(c.getColumnIndex("min"));
            int maxCal = c.getInt(c.getColumnIndex("max"));

            TextView obj = (TextView) findViewById(R.id.objectif);
            obj.setText(minCal + " - " + maxCal + " cal");
        }/*
            Cursor c2 = resolver.query(uri, null, "date=" + today, null, null);
            int totalMeal = 0;

            final TextView mealTextView = new TextView(this);

            while (c2.moveToNext()) {
                int cal_portion = c2.getInt(c2.getColumnIndex("calories"));
                int portion = c2.getInt(c2.getColumnIndex("portion"));
                int id_food = c2.getInt(c2.getColumnIndex("food"));
                String name_food = "?";
                totalMeal += cal_portion;

                Uri.Builder b2 = new Uri.Builder();
                b2.scheme("content").authority(authority).appendPath("food_table");
                Uri u2 = b2.build();

                Cursor c3 = resolver.query(u2, null, "_id=" + id_food, null, null);

                if (c3.moveToFirst()) {
                    name_food = c3.getString(c3.getColumnIndex("name"));
                }

                if (mealTextView.getText().toString().isEmpty()) {
                    mealTextView.setText("○ " + portion + "g " + name_food + " (" + cal_portion + "kcal)");
                } else {
                    mealTextView.setText(mealTextView.getText().toString() + ", " + portion + "g " + name_food + " (" + cal_portion + "kcal) ");
                }
            }

            mealTextView.setText(mealTextView.getText() + ". Total repas: " + totalMeal + "kcal.");
            LinearLayout mealsLL = (LinearLayout) findViewById(R.id.layoutMeals);
            mealsLL.addView(mealTextView);
        }*/
    }

    public void addMeal (View view){
        Intent intent = new Intent(this, AddMeal.class);
        startActivityForResult(intent, ADDMEAL_REQ_CODE);
    }

    public void chgGoal (View view){
        Intent intent = new Intent(this, ChangeGoal.class);
        startActivityForResult(intent, CHGOAL_REQ_CODE);
    }

    public void displayPastDays (View view){
        Intent intent = new Intent(this, Monitoring.class);
        startActivity(intent);
    }

    protected void onActivityResult(int reqCode, int resCode, Intent intOv){
        if (reqCode==CHGOAL_REQ_CODE && resCode==RESULT_OK){
            int min = intOv.getIntExtra("minCal",0);
            int max= intOv.getIntExtra("maxCal",0);
            TextView obj = (TextView) findViewById(R.id.objectif);
            obj.setText(min+" - "+max+" cal");
        }
        else if (reqCode==ADDMEAL_REQ_CODE && resCode==RESULT_OK){
            int num_meal = intOv.getIntExtra("num_meal",-1);

            if (num_meal!=-1){
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("content").authority(authority).appendPath("meal_table");
                Uri uri = builder.build();

                Cursor c = resolver.query(uri, null, "num=" + num_meal, null, null);
                int totalMeal=0;
                final TextView mealTextView = new TextView(this);

                while (c.moveToNext()) {
                    int cal_portion = c.getInt(c.getColumnIndex("calories"));
                    int portion = c.getInt(c.getColumnIndex("portion"));
                    int id_food = c.getInt(c.getColumnIndex("food"));
                    String name_food="?";
                    totalMeal+=cal_portion;

                    Uri.Builder b = new Uri.Builder();
                    b.scheme("content").authority(authority).appendPath("food_table");
                    Uri u = b.build();

                    Cursor c2 = resolver.query(u, null, "_id=" + id_food, null, null);

                   if (c2.moveToFirst()) {
                       name_food = c2.getString(c2.getColumnIndex("name"));
                    }

                    if (mealTextView.getText().toString().isEmpty()) {
                        mealTextView.setText("○ "+portion +"g "+name_food +" ("+cal_portion+"kcal)");
                    }
                    else {
                        mealTextView.setText(mealTextView.getText().toString()+", "+portion +"g "+name_food +" ("+cal_portion+"kcal) ");
                    }
                }

                Uri.Builder b = new Uri.Builder();
                b.scheme("content").authority(authority).appendPath("goal_table");
                Uri u = b.build();

                Cursor c3 = resolver.query(u, null, "date="+today, null, null);

                if (c3.moveToFirst()){
                    long id =c3.getLong(c3.getColumnIndex("_id"));
                    ContentUris.appendId(b, id);
                    Uri urii = b.build();

                    ContentValues values = new ContentValues();
                    values.put("total", totalMeal+c3.getInt(c3.getColumnIndex("total")));

                    TextView tvtotal = (TextView) findViewById(R.id.TVtotal);
                    int tmp= totalMeal+c3.getInt(c3.getColumnIndex("total"));
                    tvtotal.setText("Total: "+tmp+"kcal");

                    int res = resolver.update(urii, values, null, null);
                }

                else {
                    ContentValues values = new ContentValues();
                    values.put("date",today);
                    values.put("total", totalMeal);

                    TextView tvtotal = (TextView) findViewById(R.id.TVtotal);
                    tvtotal.setText("Total: "+totalMeal+"kcal");

                    u = resolver.insert(u,values);
                }
                mealTextView.setText(mealTextView.getText()+". Total repas: "+totalMeal+"kcal.");
                LinearLayout mealsLL =(LinearLayout)findViewById(R.id.layoutMeals);
                mealsLL.addView(mealTextView);
            }
            else {
                Toast.makeText(this,"Erreur",Toast.LENGTH_LONG).show();
            }
        }
    }
}

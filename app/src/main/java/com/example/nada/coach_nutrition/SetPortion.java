package com.example.nada.coach_nutrition;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetPortion extends AppCompatActivity {

    long selected_food;
    ContentResolver resolver;
    private String authority = "com.example.nada.contentProvider";
    Cursor c;
    String name;
    int cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_portion);

        Intent intent=getIntent();
        selected_food = intent.getLongExtra("id_food",-1);

        resolver = getContentResolver();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("food_table");
        Uri uri = builder.build();

        c = resolver.query(uri, null, "_id = "+ selected_food, null, null);

        if (c.moveToFirst()){
            cal = c.getInt(c.getColumnIndex("calories"));
            name = c.getString(c.getColumnIndex("name"));

            TextView nameTV = (TextView) findViewById(R.id.selectedFood);
            nameTV.setText(name);

            TextView infos = (TextView) findViewById(R.id.textInfo);
            infos.setText(cal+" calories pour 100 grammes");
        }

        else Toast.makeText(this, "erreur "+selected_food,Toast.LENGTH_LONG).show();

    }

    public void okPortion(View v){
        EditText portionET = (EditText) findViewById(R.id.portion);
        String p = portionET.getText().toString();
        if (!p.isEmpty()) {
            int intPortion = Integer.parseInt(p);
            if (intPortion<1 || intPortion > 5000) {
                Toast.makeText(this, "Veuillez entrer une valeur correcte.", Toast.LENGTH_LONG);
            }
            else {
                Intent intentOver = new Intent();
                intentOver.putExtra("id_food", selected_food);
                intentOver.putExtra("portion_food", intPortion);
                intentOver.putExtra("calories_food", cal);

                setResult(RESULT_OK, intentOver);
                finish();
            }
        }
        else {
            Toast.makeText(this, "Veuillez entrer une valeur.", Toast.LENGTH_LONG);
        }
    }
}

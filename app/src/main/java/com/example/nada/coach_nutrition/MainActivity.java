package com.example.nada.coach_nutrition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void start(View view) {
        Intent intent = new Intent(this, Coach.class);
        startActivity(intent);
    }

    void param(View view){
        Intent intent = new Intent(this, Parameters.class);
        startActivity(intent);
    }
}

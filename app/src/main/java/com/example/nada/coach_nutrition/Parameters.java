package com.example.nada.coach_nutrition;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class Parameters extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View ecran = (View) findViewById(R.id.ecran_accueil);
        SharedPreferences pref=getSharedPreferences("STYLE", Context.MODE_PRIVATE);
        String theme =pref.getString("theme", "");

        if (theme.equals("dark")) {
            setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
        }
        else if (theme.equals("light")) {
            setTheme(android.R.style.Theme_Material_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
    }

    protected void onStart() {
        super.onStart();
    }

    public void theme(View view) {
        SharedPreferences pref = getSharedPreferences("STYLE", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEdit=pref.edit();

        switch (view.getId()) {
            case R.id.dark:
                String theme = "dark";
                prefEdit.putString("theme", theme);
                prefEdit.apply();
                break;
            case R.id.light:
                String theme2 = "light";
                prefEdit.putString("theme", theme2);
                prefEdit.apply();
                break;
        }

    }
}



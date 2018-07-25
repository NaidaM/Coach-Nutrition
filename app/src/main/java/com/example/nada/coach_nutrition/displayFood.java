package com.example.nada.coach_nutrition;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class displayFood extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listFood;
    private String authority = "com.example.nada.contentProvider";
    private android.support.v4.widget.SimpleCursorAdapter adapter;
    ContentResolver resolver;
    int category_selected;
    final int TOTAL_BASE_FOOD = 24;
    protected final int MY_REQ_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        category_selected = intent.getIntExtra("id_cat",-1);

        resolver = getContentResolver();
        listFood = (ListView) findViewById(R.id.disFood);

        adapter = new SimpleCursorAdapter(displayFood.this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"name"}, new int[]{android.R.id.text1},
                0);
        listFood.setAdapter(adapter);

        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = listFood.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), SetPortion.class);
                intent.putExtra("id_food", id);
                startActivityForResult(intent,MY_REQ_CODE);
            }
        });
        listFood.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                if (id > TOTAL_BASE_FOOD)
                showDelPopup(id);
                return true;
            }
        });

        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);

    }

    public void addNewFood (String name, int calories){
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("calories",calories);
        values.put("category", category_selected);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("food_table");
        Uri urii = builder.build();
        urii = resolver.insert(urii,values);

        final Uri uri = builder.build();
        getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String where = "( category = "+category_selected+" )";
                return new CursorLoader(getApplicationContext(), uri, null, where, null, null );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteFood (long id){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("food_table");
        ContentUris.appendId(builder, id);
        Uri urii = builder.build();
        int res = resolver.delete(urii,null,null);

        getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri;
                Uri.Builder builder = new Uri.Builder();
                uri = builder.scheme("content").authority(authority).appendPath("food_table").build();
                String where = "( category = "+category_selected+" )";
                return new CursorLoader(getApplicationContext(), uri, new String[]{"_id", "name","category"}, where, null, null );
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                adapter.swapCursor(null);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNew:
                showAddPopup();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showAddPopup() {

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.displayFood_layout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        TextView title = (TextView) popupView.findViewById(R.id.addTitle);
        title.setText("Ajouter aliment");

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        LinearLayout layoutCal =(LinearLayout)popupView.findViewById(R.id.layoutFoodCal);
        layoutCal.setVisibility(View.VISIBLE);

        Button newFood = (Button)popupView.findViewById(R.id.create);
        newFood.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText n = (EditText) popupView.findViewById(R.id.newName);
                String nameFood = n.getText().toString();
                EditText c = (EditText) popupView.findViewById(R.id.newFoodCalories);
                if (!nameFood.isEmpty()&& !c.getText().toString().isEmpty()) {
                    int calFood = Integer.parseInt(c.getText().toString());
                    addNewFood(nameFood, calFood);
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showDelPopup(long id) {

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.displayFood_layout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_delete_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final long idToDelete = id;

        TextView title = (TextView) popupView.findViewById(R.id.delTitle);
        title.setText("Supprimer aliment");
        TextView msg = (TextView) popupView.findViewById(R.id.msg);
        msg.setText("Supprimer cet aliment ?");

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button delFood = (Button)popupView.findViewById(R.id.delBtn);
        delFood.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                deleteFood(idToDelete);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
        Uri.Builder builder = new Uri.Builder();
        uri = builder.scheme("content").authority(authority).appendPath("food_table").build();
        String where = "( category = "+category_selected+" )";
        return new CursorLoader(getApplicationContext(), uri, new String[]{"_id", "name","category"}, where, null, null );
    }

        @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }

    protected void onActivityResult(int reqCode, int resCode, Intent intOv){
        if (reqCode==MY_REQ_CODE && resCode==RESULT_OK){
            long id = intOv.getLongExtra("id_food",-1);
            int portion = intOv.getIntExtra("portion_food",0);
            int cal = intOv.getIntExtra("calories_food",0);

            Intent intentOver = new Intent();
            intentOver.putExtra("id_food", id);
            intentOver.putExtra("portion_food", portion);
            intentOver.putExtra("calories_food", cal);

            setResult(RESULT_OK, intentOver);
            finish();
        }
    }

}


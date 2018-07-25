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
import android.os.Bundle;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

public class AddMeal extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView listeAlim;
    private int meal_number;
    private int today;
    private String authority = "com.example.nada.contentProvider";
    private SimpleCursorAdapter adapter;
    ContentResolver resolver;

    protected final int MY_REQ_CODE = 0;
    final int TOTAL_BASE_CATEGORIES = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        meal_number=0;
        resolver = getContentResolver();
        listeAlim = (ListView) findViewById(R.id.listView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new SimpleCursorAdapter(AddMeal.this,
                android.R.layout.simple_list_item_1, null,
                new String[]{"nom"},
                new int[]{android.R.id.text1},
                0);
        listeAlim.setAdapter(adapter);

        listeAlim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), displayFood.class);
                intent.putExtra("id_cat", (int)id);
                startActivityForResult(intent,MY_REQ_CODE);
            }
        });

        listeAlim.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                if (id > TOTAL_BASE_CATEGORIES)
                    showDelPopup(id);
                return true;
            }
        });

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = df.format(currentTime);
        today = Integer.parseInt(formattedDate);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("meal_table");
        Uri uri = builder.build();

        Cursor c = resolver.query(uri, null, "date = "+ today, null, null);

        while (c.moveToNext()) {
            meal_number = c.getInt(c.getColumnIndex("num"));
        }

        meal_number++;
        LoaderManager manager = getLoaderManager();
        manager.initLoader(0, null, this);

    }


    public void addNewCategory (String name){

        ContentValues values = new ContentValues();
        values.put("nom",name);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("category_table");
        Uri urii = builder.build();
        urii = resolver.insert(urii,values);

        final Uri uri = builder.build();
        getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getBaseContext(),uri, null, null, null, null);
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

    public void deleteCategory (long id){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath("category_table");
        ContentUris.appendId(builder, id);
        Uri urii = builder.build();
        int res = resolver.delete(urii,null,null);

        getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                Uri uri;
                Uri.Builder builder = new Uri.Builder();
                uri = builder.scheme("content").authority(authority).appendPath("category_table").build();
                return new CursorLoader(getBaseContext(),uri, null, null, null, null);}

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
        getMenuInflater().inflate(R.menu.menu_add_meal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNew:
                showAddPopup();
                return true;
            case R.id.ok:
                Intent intentOver = new Intent();
                intentOver.putExtra("num_meal",meal_number);
                setResult(RESULT_OK, intentOver);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAddPopup() {

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.addMeal_layout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        TextView title = (TextView) popupView.findViewById(R.id.addTitle);
        title.setText("Ajouter catégorie");

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button newCat = (Button)popupView.findViewById(R.id.create);
        newCat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText e = (EditText) popupView.findViewById(R.id.newName);
                String nameCat = e.getText().toString();
                if (!nameCat.isEmpty()) {
                    addNewCategory(nameCat);
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

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.addMeal_layout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_delete_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final long idToDelete = id;

        TextView title = (TextView) popupView.findViewById(R.id.delTitle);
        title.setText("Supprimer catégorie");
        TextView msg = (TextView) popupView.findViewById(R.id.msg);
        msg.setText("Supprimer cette catégorie ?");

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button delFood = (Button)popupView.findViewById(R.id.delBtn);
        delFood.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                deleteCategory(idToDelete);
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
        uri = builder.scheme("content").authority(authority).appendPath("category_table").build();
        return new CursorLoader(getApplicationContext(), uri, new String[]{"_id", "nom"}, null, null, null);
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

            int cal_meal = cal*portion/100;

            if (id!=-1 && portion !=0 && cal != 0) {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("content").authority(authority).appendPath("meal_table");
                Uri uri = builder.build();

                ContentValues values = new ContentValues();

                values.put("num", meal_number);
                values.put("date", today);
                values.put("food", (int) id);
                values.put("portion", portion);
                values.put("calories", cal_meal);

                uri = resolver.insert(uri, values);
            }

            else {
                finish();
            }

        }
    }
}

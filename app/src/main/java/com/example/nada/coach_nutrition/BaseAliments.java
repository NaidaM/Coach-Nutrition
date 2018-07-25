package com.example.nada.coach_nutrition;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseAliments extends SQLiteOpenHelper {

    private static int VERSION = 22;
    private static BaseAliments instance;
    private static String FOODDB = "AlimentsDB";

    private static final String CATEGORY_TABLE = "category_table";
    private static final String FOOD_TABLE = "food_table";
    private static final String GOAL_TABLE = "goal_table";
    private static final String MEAL_TABLE = "meal_table";

    private static final String CATEGORY_KEY = "_id";
    private static final String CATEGORY_NAME = "nom";

    private static final String FOOD_KEY = "_id";
    private static final String FOOD_NAME = "name";
    private static final String FOOD_CATEGORY = "category";
    private static final String FOOD_CALORIES = "calories";

    private static final String GOAL_KEY = "_id";
    private static final String GOAL_DATE = "date";
    private static final String GOAL_MIN = "min";
    private static final String GOAL_MAX = "max";
    private static final String GOAL_TOTAL = "total";

    private static final String MEAL_KEY = "_id";
    private static final String MEAL_NUMBER = "num";
    private static final String MEAL_DATE = "date";
    private static final String MEAL_FOOD = "food";
    private static final String MEAL_PORTION = "portion";
    private static final String MEAL_CALORIES = "calories";


    private String category_table = "create table "+ CATEGORY_TABLE +"( " +
            CATEGORY_NAME + " varchar(30) not null," +
            CATEGORY_KEY + " integer primary key autoincrement);";

    private String food_table = "create table "+ FOOD_TABLE+" (" +
            FOOD_NAME + " varchar(30) not null," +
            FOOD_CALORIES + " int not null," +
            FOOD_CATEGORY + " int references "+ CATEGORY_TABLE+" ," +
            FOOD_KEY + " integer primary key autoincrement);";

    private String goal_table = "create table "+ GOAL_TABLE+" (" +
            GOAL_DATE + " int," +
            GOAL_MIN + " int," +
            GOAL_MAX + " int," +
            GOAL_TOTAL + " int," +
            GOAL_KEY + " integer primary key autoincrement);";

    private String meal_table = "create table "+ MEAL_TABLE+" (" +
            MEAL_NUMBER + " int," +
            MEAL_DATE + " int," +
            MEAL_FOOD + " int references " + FOOD_TABLE+ "," +
            MEAL_PORTION + " int," +
            MEAL_CALORIES + " int," +
            MEAL_KEY + " integer primary key autoincrement);";


    public static BaseAliments getInstance(Context context){
        if( instance == null ){
            instance = new BaseAliments(context);
        }
        return instance;
    }

    private BaseAliments(Context context) {
        super(context, FOODDB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(category_table);
        db.execSQL(food_table);
        db.execSQL(goal_table);
        db.execSQL(meal_table);

        //initialisation de la BD
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME,"Boissons");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME, "Matières grasses");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME, "Céréales, féculents");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME,"Fruits et légumes");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME, "Produits laitiers, oeufs");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME, "Viandes, poissons");
        db.insert(CATEGORY_TABLE, null, values);
        values.put(CATEGORY_NAME, "Sucres");
        db.insert(CATEGORY_TABLE, null, values);

        values = new ContentValues();
        values.put(FOOD_CALORIES,0);

        values.put(FOOD_CATEGORY,1);
        values.put(FOOD_NAME, "Sprite");
        values.put(FOOD_CALORIES, 39);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,1);
        values.put(FOOD_NAME, "Cola");
        values.put(FOOD_CALORIES, 44);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,1);
        values.put(FOOD_NAME, "Bière");
        values.put(FOOD_CALORIES, 43);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,2);
        values.put(FOOD_NAME, "Beurre");
        values.put(FOOD_CALORIES, 760);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,2);
        values.put(FOOD_NAME, "Huile d'olive");
        values.put(FOOD_CALORIES, 884);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,2);
        values.put(FOOD_NAME, "Margarine");
        values.put(FOOD_CALORIES, 755);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,3);
        values.put(FOOD_NAME, "Riz");
        values.put(FOOD_CALORIES, 130);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,3);
        values.put(FOOD_NAME, "Pates");
        values.put(FOOD_CALORIES, 131);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,3);
        values.put(FOOD_NAME, "Pommes de terre");
        values.put(FOOD_CALORIES, 77);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,4);
        values.put(FOOD_NAME, "Banane");
        values.put(FOOD_CALORIES, 89);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,4);
        values.put(FOOD_NAME, "Brocolis");
        values.put(FOOD_CALORIES, 34);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,4);
        values.put(FOOD_NAME, "Pomme");
        values.put(FOOD_CALORIES, 52);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,5);
        values.put(FOOD_NAME, "Oeuf");
        values.put(FOOD_CALORIES, 155);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,5);
        values.put(FOOD_NAME, "Lait");
        values.put(FOOD_CALORIES, 42);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,5);
        values.put(FOOD_NAME, "Roquefort");
        values.put(FOOD_CALORIES, 369);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Agneau");
        values.put(FOOD_CALORIES, 280);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Boeuf");
        values.put(FOOD_CALORIES, 250);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Porc");
        values.put(FOOD_CALORIES, 300);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Dinde");
        values.put(FOOD_CALORIES, 125);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Charcuterie");
        values.put(FOOD_CALORIES, 450);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,6);
        values.put(FOOD_NAME, "Saumon");
        values.put(FOOD_CALORIES, 200);
        db.insert(FOOD_TABLE, null, values);

        values.put(FOOD_CATEGORY,7);
        values.put(FOOD_NAME, "Biscuits");
        values.put(FOOD_CALORIES, 353);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,7);
        values.put(FOOD_NAME, "Caramel");
        values.put(FOOD_CALORIES, 382);
        db.insert(FOOD_TABLE, null, values);
        values.put(FOOD_CATEGORY,7);
        values.put(FOOD_NAME, "Bonbons");
        values.put(FOOD_CALORIES, 535);
        db.insert(FOOD_TABLE, null, values);

        values = new ContentValues();
        values.put(GOAL_DATE,20171218);
        values.put(GOAL_MAX, 2000);
        values.put(GOAL_MIN, 1700);
        values.put(GOAL_TOTAL, 1990);
        db.insert(GOAL_TABLE, null, values);
        values.put(GOAL_DATE,20171216);
        values.put(GOAL_MAX, 2100);
        values.put(GOAL_MIN, 1800);
        values.put(GOAL_TOTAL, 2100);
        db.insert(GOAL_TABLE, null, values);
        values.put(GOAL_DATE,20171215);
        values.put(GOAL_MAX, 2200);
        values.put(GOAL_MIN, 1900);
        values.put(GOAL_TOTAL, 2420);
        db.insert(GOAL_TABLE, null, values);
        values.put(GOAL_DATE,20171213);
        values.put(GOAL_MAX, 2100);
        values.put(GOAL_MIN, 2000);
        values.put(GOAL_TOTAL, 2640);
        db.insert(GOAL_TABLE, null, values);
        values.put(GOAL_DATE,20171210);
        values.put(GOAL_MAX, 3000);
        values.put(GOAL_MIN, 2000);
        values.put(GOAL_TOTAL, 2700);
        db.insert(GOAL_TABLE, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + CATEGORY_TABLE + " ;");
            db.execSQL("drop table if exists " + FOOD_TABLE +" ;");
            db.execSQL("drop table if exists " + GOAL_TABLE +" ;");
            db.execSQL("drop table if exists " + MEAL_TABLE +" ;");
            onCreate(db);
        }
    }
}


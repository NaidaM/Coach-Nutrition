package com.example.nada.coach_nutrition;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class BaseContentProvider extends ContentProvider {

    private BaseAliments helper;
    private static final String LOG = "BaseContentProvider";
    private String authority = "com.example.nada.contentProvider";

    private static final int FOOD = 1;
    private static final int CAT = 2;
    private static final int GOAL = 6;
    private static final int MEAL = 7;

    private static final int ONE_FOOD = 3;
    private static final int ONE_CAT = 4;
    private static final int ONE_GOAL = 5;

    private final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    {
        matcher.addURI(authority, "food_table", FOOD);
        matcher.addURI(authority, "category_table", CAT);
        matcher.addURI(authority, "goal_table", GOAL);
        matcher.addURI(authority, "meal_table", MEAL);

        matcher.addURI(authority, "food_table/#", ONE_FOOD);
        matcher.addURI(authority, "category_table/#", ONE_CAT);
        matcher.addURI(authority, "goal_table/#", ONE_GOAL);
    }

    @Override
    public boolean onCreate() {
        helper = BaseAliments.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int code = matcher.match(uri);
        Cursor cursor;
        switch (code) {
            case FOOD:
                cursor = db.query("food_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CAT:
                cursor = db.query("category_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case GOAL:
                cursor = db.query("goal_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MEAL:
                cursor = db.query("meal_table", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                Log.d("Uri provider =", uri.toString());
                throw new UnsupportedOperationException("this query is not yet implemented  " +
                        uri.toString());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        long id ;
        String path;
        switch (code) {
            case FOOD:
                id = db.insert("food_table", null, values);
                path = "food_table";
                break;
            case CAT:
                id = db.insert("category_table", null, values);
                path = "category_table";
                break;
            case GOAL:
                id = db.insert("goal_table", null, values);
                path = "goal_table";
                break;
            case MEAL:
                id = db.insert("meal_table", null, values);
                path = "meal_table";
                break;
            default:
                throw new UnsupportedOperationException("this insert not yet implemented");
        }
        Uri.Builder builder = (new Uri.Builder())
                .authority(authority)
                .appendPath(path);

        return ContentUris.appendId(builder, id).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        long id;
        switch (code) {
            case ONE_FOOD:
                id = ContentUris.parseId(uri);
                i = db.delete("food_table", "_id=" + id, null);
                break;
            case ONE_CAT:
                id = ContentUris.parseId(uri);
                i = db.delete("category_table", "_id=" + id, null);
                break;
            default:
                throw new UnsupportedOperationException("This delete not yet implemented");
        }
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        int i;
        long id;
        switch (code) {
            case ONE_FOOD:
                id = ContentUris.parseId(uri);
                i = db.update("food_table", values, "_id=" + id, null);
                break;
            case ONE_CAT:
                id = ContentUris.parseId(uri);
                i = db.update("category_table", values, "_id=" + id, null);
                break;
            case ONE_GOAL:
                id = ContentUris.parseId(uri);
                i = db.update("goal_table", values, "_id=" + id, null);
                break;
            default:
                throw new UnsupportedOperationException("This update not yet implemented");
        }
        return i;
    }
}

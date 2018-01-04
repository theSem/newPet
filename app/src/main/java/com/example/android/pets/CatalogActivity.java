package com.example.android.pets;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDBHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private PetDBHelper petDBHelper = new PetDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    @SuppressLint("SetTextI18n")
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        // Create and/or open a database to read from it
        SQLiteDatabase db = petDBHelper.getReadableDatabase();

        String[] projection = {PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT};

        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        String del = " - ";

        StringBuilder sb = new StringBuilder();
        sb.append(PetEntry._ID)
                .append(del)
                .append(PetEntry.COLUMN_PET_NAME)
                .append(del)
                .append(PetEntry.COLUMN_PET_BREED)
                .append(del)
                .append(PetEntry.COLUMN_PET_GENDER)
                .append(del)
                .append(PetEntry.COLUMN_PET_WEIGHT)
                .append("\n");

        while (cursor.moveToNext()){
            int currentID = cursor.getInt(cursor.getColumnIndex(PetEntry._ID));
            String currentName = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
            String currentBreed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));
            String currentGender = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER));
            String currentWeight = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));
            sb.append(currentID)
                    .append(del)
                    .append(currentName)
                    .append(del)
                    .append(currentBreed)
                    .append(del)
                    .append(currentGender)
                    .append(del)
                    .append(currentWeight)
                    .append("\n");
        }
        TextView textView = findViewById(R.id.text_view_pet);
        textView.setText(sb.toString());
    }

    @Override
    protected void onStart() {
        displayDatabaseInfo();
        super.onStart();
    }

    public long insertData(){

        SQLiteDatabase db = petDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Sophie");
        values.put(PetEntry.COLUMN_PET_BREED, "Spaniel");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 10);

        return db.insert(PetEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                if (insertData() == -1){
                    Log.e("Ooph", "goof");
                }
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                petDBHelper.getWritableDatabase().delete(PetEntry.TABLE_NAME, null, null);
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
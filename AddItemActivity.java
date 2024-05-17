package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class AddItemActivity extends AppCompatActivity {

    private EditText editTextItemName, editTextPhone, editTextDescription, editTextDate, editTextLocation;
    private Button buttonSave;
    private RadioGroup radioGroupPostType;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextItemName = findViewById(R.id.editTextItemName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonSave = findViewById(R.id.buttonSave);
        radioGroupPostType = findViewById(R.id.radioGroupPostType);

        dbHelper = new DataBaseHelper(this);

        buttonSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String itemName = editTextItemName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        boolean isFound = radioGroupPostType.getCheckedRadioButtonId() == R.id.radioFound;

        if (itemName.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(date)) {
            Toast.makeText(this, "Please enter a valid date (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_ITEM_NAME, itemName);
        values.put(DataBaseHelper.COLUMN_PHONE, phone);
        values.put(DataBaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DataBaseHelper.COLUMN_DATE, date);
        values.put(DataBaseHelper.COLUMN_LOCATION, location);
        values.put(DataBaseHelper.COLUMN_IS_FOUND, isFound ? 1 : 0);

        try {
            long newRowId = db.insert(DataBaseHelper.TABLE_ITEMS, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving item", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDate(String date) {
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
        return Pattern.matches(datePattern, date);
    }
}

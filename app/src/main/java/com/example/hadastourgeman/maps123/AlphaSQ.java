package com.example.hadastourgeman.maps123;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlphaSQ extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    ListView listView;
    DatabaseReference ref;

    List<String> dates=new ArrayList<>();
    List<String> values;
    String[] fruit = {"Apple", "Banana", "Orange", "Grapes"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_sq);

        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);

        ref = FirebaseDatabase.getInstance().getReference("dataRec");

        dates = new ArrayList<>();
        values = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String date = ds.getKey();
                    Toast.makeText(AlphaSQ.this, date, Toast.LENGTH_LONG).show();
                    dates.add(date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Toast.makeText(this, ""+dates.get(1), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dates);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, ""+position, Toast.LENGTH_LONG).show();
/*        ref.child(dates.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                values.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getValue(String.class);
                    values.add(value);
                }
                ArrayAdapter<String> listAdapter2 = new ArrayAdapter(AlphaSQ.this,
                        android.R.layout.simple_spinner_item, fruit);
                listView.setAdapter(listAdapter2);
                ArrayAdapter<String> listAdapter = new ArrayAdapter(AlphaSQ.this,
                        android.R.layout.simple_spinner_item, values);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
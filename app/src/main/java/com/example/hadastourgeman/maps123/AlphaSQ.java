package com.example.hadastourgeman.maps123;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AlphaSQ extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    EditText ed;
    Button button;
    String x="400";
    Button up;

    Button de;
    int i;
    String st;
    ListView lv;
    ArrayAdapter adp;
    ArrayList<String> tbl=new ArrayList<>();

    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_sq);

        ed=(EditText) findViewById(R.id.ed);
        button=(Button) findViewById(R.id.button);
        lv=(ListView)findViewById(R.id.lv);
        de=(Button)findViewById(R.id.de);
        up=(Button)findViewById(R.id.up);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

    }

    public void bbb(View view) {

        st=ed.getText().toString();
        i=Integer.parseInt(st);
        cv=new ContentValues();
        cv.put(DATA.DIS,i);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.insert(DATA.TABLE_DATA,null,cv);
        db.close();

        hlp=new HelperDB(this);
        db=hlp.getWritableDatabase();

        tbl=new ArrayList<>();
        c=db.query(DATA.TABLE_DATA,null,null,null,null,null,null);
        int ciz=c.getColumnIndex("_id");
        int cdis=c.getColumnIndex("dis");
        int cgps=c.getColumnIndex("gps");
        int cdir=c.getColumnIndex("direction");
        c.moveToFirst();

        while (!c.isAfterLast()){
            int d=c.getInt(cdis);
            int gps=c.getInt(cgps);
            int dir=c.getInt(cdir);
            String temp=d+","+gps+","+dir;
            tbl.add(temp);
            c.moveToNext();
        }
        c.close();
        adp=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tbl);
        lv.setAdapter(adp);

        db.close();
    }

    public void delete(View view) {
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();

        db.delete(DATA.TABLE_DATA,"dis=?",new String[]{"700"});

        tbl=new ArrayList<>();
        c=db.query(DATA.TABLE_DATA,null,null,null,null,null,null);
        int ciz=c.getColumnIndex("_id");
        int cdis=c.getColumnIndex("dis");
        int cgps=c.getColumnIndex("gps");
        int cdir=c.getColumnIndex("direction");
        c.moveToFirst();
        while (!c.isAfterLast()){
            int d=c.getInt(cdis);
            int gps=c.getInt(cgps);
            int dir=c.getInt(cdir);
            String temp=d+","+gps+","+dir;
            tbl.add(temp);
            c.moveToNext();
        }
        c.close();
        adp=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tbl);
        lv.setAdapter(adp);

        db.close();
    }

    public void toblue(View view) {
        Intent n=new Intent(this,BluetoothAlpha.class);
        startActivity(n);
    }
 /*
    public void update(View view) {
        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        cv=new ContentValues();
        cv.put(DATA.TABLE_DATA,"300");
        db.update(DATA.TABLE_DATA,cv,"dis=?",new String[]{"300"});
        db.close();
    }

    */
}

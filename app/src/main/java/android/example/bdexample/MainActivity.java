package android.example.bdexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnDel, btnUpd, btnDelAll, btnRead;
    TextView tvOut;
    EditText edName, edSurname, edAge, edId;
    DBHelper dbHelper;
    Intent intent;
    //String nameDB = "testDB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        btnDel = findViewById(R.id.btnDel);
        btnUpd = findViewById(R.id.btnUpd);
        btnDelAll = findViewById(R.id.btnDelAll);
        btnRead = findViewById(R.id.btnRead);

        btnAdd.setOnClickListener(btnListener);
        btnDel.setOnClickListener(btnListener);
        btnUpd.setOnClickListener(btnListener);
        btnRead.setOnClickListener(btnListener);
        btnDelAll.setOnClickListener(btnListener);


        tvOut = findViewById(R.id.tvOut);

        edName = findViewById(R.id.edName);
        edSurname = findViewById(R.id.edSurname);
        edAge = findViewById(R.id.edAge);
        edId = findViewById(R.id.edId);

        intent = new Intent(this, Activity_watchBD.class);

        dbHelper = new DBHelper(this);


    }

    View.OnClickListener btnListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ContentValues cv = new ContentValues();

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if(view.getId() == R.id.btnAdd){

                String name = edName.getText().toString();
                String surname = edSurname.getText().toString();
                Integer age = Integer.parseInt(edAge.getText().toString());
                Integer id = Integer.parseInt(edId.getText().toString());

                //cv.put("id", id);
                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("age", age);

                db.insert("testTable", null, cv);

                tvOut.append("Row added to database" + "\n");
            }
            if(view.getId() == R.id.btnDel){
                Integer id = Integer.parseInt(edId.getText().toString());
                deleteFromDB(db, id);
                tvOut.append("Row[" + id + "] deleted from database" + "\n");
            }
            if(view.getId() == R.id.btnUpd){
                String name = edName.getText().toString();
                String surname = edSurname.getText().toString();
                Integer age = Integer.parseInt(edAge.getText().toString());
                Integer id = Integer.parseInt(edId.getText().toString());
                updateDB(db, name, surname, age, id);
                tvOut.append("Row [" + id + "] updated in database"+ "\n");
            }
            if(view.getId() == R.id.btnDelAll){
                db.delete("testTable", null,null);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'testTable'");
                tvOut.append("Database cleared" + "\n");
            }
            if(view.getId() == R.id.btnRead){
                //outDB(db);
                startActivity(intent);
                tvOut.append("Database read" + "\n");
            }

            dbHelper.close();
        }
    };
    public void updateDB(SQLiteDatabase db, String name, String surname, int age, int id){
        ContentValues cv = new ContentValues();
        Cursor c = db.query("testTable", null,null, null,
                null, null, null);

        int j = 0;
        if(c.moveToFirst()) {
            do{
                if(j == id){
                    cv.put("id", j);
                    cv.put("name", name);
                    cv.put("age", age);
                    cv.put("surname", surname);
                    db.update("testTable", cv,"id = " + id, null);
                }
                j++;
            }while(c.moveToNext());
        }
        c.close();

    }
    public void deleteFromDB(SQLiteDatabase db, Integer id){
        ContentValues cv = new ContentValues();
        ArrayList<Calendar> arrCalend = new ArrayList<>();
        ArrayList<String> arrName = new ArrayList<>();
        ArrayList<String> arrSurname = new ArrayList<>();
        ArrayList<Integer> arrAge = new ArrayList<>();
        Calendar tempCalend = Calendar.getInstance();
        Cursor c = db.query("testTable", null,null, null,
                null, null, null);
        int j = 0;
        if(c.moveToFirst()) {
            do{
                arrName.add(j, c.getString(c.getColumnIndex("name")));
                arrSurname.add(j,  c.getString(c.getColumnIndex("surname")));
                arrAge.add(j,  c.getInt(c.getColumnIndex("age")));
                j++;
            }while(c.moveToNext());
        }
        c.moveToFirst();
        for(int i = id; i < j-1; i++){
            arrName.set(i, arrName.get(i+1));
            arrSurname.set(i, arrSurname.get(i+1));
            arrAge.set(i, arrAge.get(i+1));
        }
        db.delete("testTable", null,null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'testTable'");
        for(int i = 0; i < j-1; i++){
            //cv.put("id", i);
            cv.put("name", arrName.get(i));
            cv.put("surname", arrSurname.get(i));
            cv.put("age", arrAge.get(i));
            db.insert("testTable", null, cv);
        }
        c.close();
    }
    public void outDB(SQLiteDatabase db){
        tvOut.setText("Out DB:" + "\n");
        Cursor c = db.query("testTable", null,null, null,
                null, null, null);
        if(c.moveToFirst()) {
            do{
                tvOut.append("ID = " + c.getInt(c.getColumnIndex("id")) + "\n");
                tvOut.append("name = " + c.getInt(c.getColumnIndex("name")) + "\n" );
                tvOut.append("surname = " + c.getInt(c.getColumnIndex("surname")) + "\n");
                tvOut.append("age = " + c.getInt(c.getColumnIndex("age")) + "\n");
            }while(c.moveToNext());
        }
        else
            System.out.println("EMPTYYYYYYY" + c.getCount());
        c.close();
        }


}

class DBHelper extends SQLiteOpenHelper {

    String nameDB = "testDB";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "testDB", null, 1);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table testTable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "surname text,"
                + "age integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

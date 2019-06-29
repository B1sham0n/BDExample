package android.example.bdexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_watchBD extends AppCompatActivity {

    DBHelper dbHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_bd);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        View childView;
        Context mContext = getApplicationContext();
        LinearLayout scrollLayout = findViewById(R.id.scroll_layout);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Cursor c = db.query("testTable", null,null, null,
                null, null, null);

        //далее в цикле создаем childView до тех пор, пока не закончится бд
        //при этом каждый раз перемещаем курсор и берем новые значения строки
        if(c.moveToFirst()) {
            do{
                childView = inflater.inflate(R.layout.my_db, null);

                TextView tvId = childView.findViewById(R.id.children_id);
                TextView tvName = childView.findViewById(R.id.children_name);
                TextView tvSurname = childView.findViewById(R.id.children_surname);
                TextView tvAge = childView.findViewById(R.id.children_age);

                tvId.setText("id = " + c.getInt(c.getColumnIndex("id")));
                tvName.setText("Name = " + c.getString(c.getColumnIndex("name")));
                tvSurname.setText("Surname = " + c.getString(c.getColumnIndex("surname")));
                tvAge.setText("Age = " + c.getInt(c.getColumnIndex("age")));

                scrollLayout.addView(childView);

            }while(c.moveToNext());
        }
        else
            System.out.println("EMPTY");
        c.close();


    }
}

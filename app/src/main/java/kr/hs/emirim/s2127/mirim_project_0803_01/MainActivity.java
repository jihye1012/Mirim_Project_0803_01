package kr.hs.emirim.s2127.mirim_project_0803_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText editName, editCount, editResultName, editResultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editResultName = findViewById(R.id.edit_result_name);
        editResultCount = findViewById(R.id.edit_result_count);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInsert = findViewById(R.id.btn_insert);
        Button btnSelect = findViewById(R.id.btn_select);
        btnInit.setOnClickListener(btnListener);
        btnInsert.setOnClickListener(btnListener);
        btnSelect.setOnClickListener(btnListener);

        dbHelper = new DBHelper(this);
        //setTitle("가수 그룸 관리 DB");
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        SQLiteDatabase db;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_init:
                    db = dbHelper.getWritableDatabase();
                    dbHelper.onUpgrade(db, 1, 2);
                    db.close();
                    break;
                case R.id.btn_insert:
                    db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into idolTbl values('" + editName.getText().toString() + "'," + editCount.getText().toString() + ");");
                    db.close();
                    Toast.makeText(getApplicationContext(), "새로운 idol 정보가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editCount.setText("");
                    break;
                case R.id.btn_select:
                    db = dbHelper.getReadableDatabase();
                    Cursor c = db.rawQuery("select * from idolTbl;", null);

                    String strName = "아이돌명\r\n_________\r\n";
                    String strCnt = "인원수\r\n_________\r\n";

                    while (c.moveToNext()) {
                        strName += c.getString(0) + "\r\n";
                        strCnt += c.getString(1) + "\r\n";
                    }
                    editResultName.setText(strName);
                    editResultCount.setText(strCnt);
                    c.close();
                    db.close();
                    break;
            }

        }
    };

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "idolDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table idolTbl(name char(30) primary key," + "cnt integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists idolTbl");
            onCreate(db);
        }
    }
}
package com.bignerdranch.android.recruitmi;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 이예린 on 2018-06-18.
 */

public class BoardShareEditActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String dbname = "myDB";
    String tablename = "share";
    String sql;

    public EditText textTitle;
    public EditText textContent;
    public Button insertButton;

    String str_input1, str_input2, str_input3;
    String name ="yerin";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_board);

        textTitle = findViewById(R.id.board_title);
        textContent = findViewById(R.id.board_content);

        insertButton = (Button) findViewById(R.id.board_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //레코드 삽입
                try{
                    str_input1 = textTitle.getText().toString();
                    str_input2 = textContent.getText().toString();

                    sql = "insert into "+tablename+"(name, title, content) values('"+name+"', '"+str_input1+"', '"+str_input2+"');";
                    db.execSQL(sql);
                    System.out.println("insert ok");
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("db error : " + e);
                }

            }
        });
    }
}

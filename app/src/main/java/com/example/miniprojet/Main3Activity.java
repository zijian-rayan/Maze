package com.example.miniprojet;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity {

    ListView listScore;
    String strScore[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listScore=(ListView)findViewById(R.id.ScoreRe);
        //Print
        strScore = new String[]
                {
                "第一回合:             "+MainActivity.name[0]+"   "+MainActivity.scoreStr[0],
                "第二回合:             "+MainActivity.name[1]+"   "+MainActivity.scoreStr[1],
                "第三回合:             "+MainActivity.name[2]+"   "+MainActivity.scoreStr[2],
                "第四回合:             "+MainActivity.name[3]+"   "+MainActivity.scoreStr[3],
                "第五回合:             "+MainActivity.name[4]+"   "+MainActivity.scoreStr[4]
                };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strScore);
        listScore.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "有任何疑问请联系@ https://wuzijian.cf", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}

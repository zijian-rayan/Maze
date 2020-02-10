package com.example.miniprojet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener {
    static EditText Name;
    RadioGroup radiogroup;
    public int Player=1;
    //static int score[]= {0,0,0,0,0};
    static String scoreStr[]=new String[5];
    static String name[]=new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        radiogroup=(RadioGroup)findViewById(R.id.rbtgroup_maze);
        radiogroup.setOnCheckedChangeListener(this);
        Name=(EditText)findViewById(R.id.Name);
        Name.setText("Your name?");
        Name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Name.setText("Player 1");
                Name.selectAll();
            }
        });
        ((Button)findViewById(R.id.Play)).setOnClickListener(this);
        ((Button)findViewById(R.id.Score)).setOnClickListener(this);
        //for(int i=0;i<5;i++) {
        //    score[i]=getPreferences(MODE_PRIVATE).getInt(String.format("Score"+"%d",i), score[i]);
        //}
        for(int i=0;i<5;i++) {
            scoreStr[i]=getPreferences(MODE_PRIVATE).getString(String.format("Time"+"%d",i), scoreStr[i]);
        }
        for(int i=0;i<5;i++) {
            name[i]=getPreferences(MODE_PRIVATE).getString(String.format("Name"+"%d",i), name[i]);
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "欢迎报告bug，本人email: wuzijian@wuzijian.cf", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this,"欢迎来到迷宫游戏，我最尊贵的客人！",Toast.LENGTH_LONG).show();
                break;
            case R.id.rndname:
                Toast.makeText(this,"自动生成玩家名字.",Toast.LENGTH_LONG).show();
                Name.setText("Player"+(int)(1+Math.random()*(50+1)));
                break;

            default:
                break;
        }

        return true;
    }
    @Override
    protected void onPause() {
        //for(int i=0;i<5;i++) {
         //   SharedPreferences.Editor ed=getPreferences(MODE_PRIVATE).edit();
         //   ed.putInt(String.format("Score"+"%d", i), score[i]);
         //   ed.commit();
        //}
        for(int i=0;i<5;i++) {
            SharedPreferences.Editor ed=getPreferences(MODE_PRIVATE).edit();
            ed.putString(String.format("Time"+"%d", i), scoreStr[i]);
            ed.commit();
        }
        for(int i=0;i<5;i++) {
            SharedPreferences.Editor ed=getPreferences(MODE_PRIVATE).edit();
            ed.putString(String.format("Name"+"%d", i), name[i]);
            ed.commit();
        }
        super.onPause();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(group.getCheckedRadioButtonId()){
            case R.id.round1: Player=1; break;
            case R.id.round2: Player=2; break;
            case R.id.round3: Player=3; break;
            case R.id.round4: Player=4; break;
            case R.id.round5: Player=5; break;
            default: return;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.Play:
                if(!Name.getText().toString().equals("")){
                    if(Player!=0){
                        //Name.setText("player");
                        Intent intentMaze=new Intent(this, Main2Activity.class);
                        intentMaze.putExtra("Player", Player);
                        startActivityForResult(intentMaze,1);
                    }
                }
                else{
                    new AlertDialog.Builder(this).setMessage("请输入玩家名字!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {} }).show();
                }
                break;
            case R.id.Score:
                Intent intentScore=new Intent(this,Main3Activity.class);
                startActivityForResult(intentScore,2);
                break;
            default: return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this).setMessage("真的要退出吗 ?").setNegativeButton("NO", (DialogInterface.OnClickListener) this).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }


}

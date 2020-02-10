package com.example.miniprojet;

import java.io.IOException;
import java.io.InputStream;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class Main2Activity extends Activity {

    MazeView mazeView;
    static int Player;
    static int width;
    static int height;
    static Chronometer Timer;
    static SensorManager sensorManager;
    static Sensor sensor;
    static MediaPlayer mediaPlayer;
    static boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra=getIntent().getExtras();
        if(extra != null){
            Player=extra.getInt("Player",1);
        }
        //Remove notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        // Get width and height
        Resources resources=getResources();
        DisplayMetrics met=resources.getDisplayMetrics();
        width=met.widthPixels;
        height=met.heightPixels;
        LinearLayout reLayout=findViewById(R.id.relayout);
        reLayout.addView(mazeView=new MazeView(this),new LayoutParams(width*6/7,height));
        mediaPlayer=new MediaPlayer();
        Timer=(Chronometer)findViewById(R.id.timer);
        Timer.setBase(SystemClock.elapsedRealtime());
        Timer.start();

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public void onBackPressed() {
        flag=true;
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onBackPressed();
    }

    private static class MazeView extends View implements SensorEventListener {
        static final int MazeDimX = 72;
        static final int MazeDimY = 48;
        byte maze[] = new byte[MazeDimX * MazeDimY];
        int mazeColor[] = new int[MazeDimX * MazeDimY];
        int redX, redY, failTimes, initX, initY;
        Bitmap bitmapMaze;
        Paint p_red,p_black;
        private Context mazeContext;

        public void LoadMaze(int idx) {
            Resources res = getResources();
            InputStream Iptstr;
            switch(idx){
                case 1:Iptstr = res.openRawResource(R.raw.lab1); break;
                case 2:Iptstr = res.openRawResource(R.raw.lab2); break;
                case 3:Iptstr = res.openRawResource(R.raw.lab3); break;
                case 4:Iptstr = res.openRawResource(R.raw.lab4); break;
                case 5:Iptstr = res.openRawResource(R.raw.lab5); break;
                default:return;
            }

            try {
                Iptstr.read(maze, 0, MazeDimX * MazeDimY);
            } catch (IOException e) {
                return;
            }
        }




        public MazeView(Context context) {
            super(context);
            mazeContext=context;
            LoadMaze(Player);
            for(int i=0;i<MazeDimX * MazeDimY;i++){
                switch(maze[i]){
                    case 0x00:mazeColor[i]=0xFFFFFFFF; break;  //white
                    case 0x01:mazeColor[i]=0xFF000000; break;  //black
                    case 0x02: //red->white
                        mazeColor[i]=0xFFFFFFFF;
                        redX=i%MazeDimX;
                        redY=i/MazeDimX;
                        initX=redX;
                        initY=redY;
                        break;
                    case 0x03:
                        mazeColor[i]=0xFF00FF00;
                        break;  //green
                    case 0x04:
                        mazeColor[i]=0xFFFF9000;
                        break;  //orange
                    default: mazeColor[i]=0xFFFFFFFF;
                }
            }
            bitmapMaze=Bitmap.createBitmap(mazeColor, MazeDimX, MazeDimY, Bitmap.Config.ARGB_8888);
            p_red=new Paint();
            p_black=new Paint();
            flag=false;
            sensorManager=(SensorManager)mazeContext.getSystemService(SENSOR_SERVICE);
            sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.scale(getWidth()/MazeDimX,getHeight()/MazeDimY);
            Log.d("getwidth", String.valueOf(getWidth())+"  "+getHeight());
            canvas.drawBitmap(bitmapMaze, 0, 0, null);
            p_red.setColor(Color.RED);
            //p_black.setColor(Color.BLACK);
            //p_black.setTextSize(5);
            if(mazeColor[redY*MazeDimX+redX]==0xFF00FF00){
                mediaPlayer.reset();
                mediaPlayer=MediaPlayer.create(mazeContext, R.raw.tada);
                mediaPlayer.start();
                String scoreTemp=Timer.getText().toString();
                String minute=scoreTemp.substring(0,2);
                String second=scoreTemp.substring(3,5);
                //int minuteInt=Integer.parseInt(minute);
                //int secondInt=Integer.parseInt(second);
                //int scoreInt=minuteInt*60+secondInt;
                //if(scoreInt<MainActivity.score[Player-1]) {
                    //MainActivity.score[Player-1]=scoreInt;
                    MainActivity.scoreStr[Player-1]=scoreTemp;
                    MainActivity.name[Player-1]=MainActivity.Name.getText().toString();
                    MainActivity.Name.setText("Player ??");
                //}
                flag=true;
                new AlertDialog.Builder(mazeContext).setMessage("Congratulations!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)mazeContext).finish();
                    }
                }).show();
                Timer.stop();
            }
            if(mazeColor[redY*MazeDimX+redX]==0xFFFF9000){
                failTimes++;
                if(failTimes<3){
                    redX=initX;
                    redY=initY;
                    mediaPlayer.reset();
                    mediaPlayer=MediaPlayer.create(mazeContext, R.raw.cash);
                    mediaPlayer.start();
                }
                else{
                    flag=true;
                    new AlertDialog.Builder(mazeContext).setMessage("Game over!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity)mazeContext).finish();
                        }
                    }).show();
                }
            }
            canvas.drawRect(redX, redY, redX+1, redY+1, p_red);
            super.onDraw(canvas);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(flag==false) {
                float x =  event.values[0];
                float y =  event.values[1];
                if(x<-0) {
                    for(int i=0;i<-x/2;i++){
                        if(mazeColor[(redY-1)*MazeDimX+redX]!=0xFF000000){
                            redY-=1;
                        }
                        else{
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(mazeContext, R.raw.click);
                            mediaPlayer.start();
                            break;
                        }
                    }
                }
                if(x>0) {
                    for(int i=0;i<x/2;i++){
                        if(mazeColor[(redY+1)*MazeDimX+redX]!=0xFF000000){
                            redY+=1;
                        }
                        else{
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(mazeContext, R.raw.click);
                            mediaPlayer.start();
                            break;
                        }
                    }
                }
                if(y<-0) {
                    for(int i=0;i<-y/2;i++){
                        if(mazeColor[redY*MazeDimX+redX-1]!=0xFF000000){
                            redX-=1;
                        }
                        else{
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(mazeContext, R.raw.click);
                            mediaPlayer.start();
                            break;
                        }
                    }
                }
                if(y>0) {
                    for(int i=0;i<y/2;i++){
                        if(mazeColor[redY*MazeDimX+redX+1]!=0xFF000000){
                            redX+=1;
                        }
                        else{
                            mediaPlayer.reset();
                            mediaPlayer=MediaPlayer.create(mazeContext, R.raw.click);
                            mediaPlayer.start();
                            break;
                        }
                    }
                }
                invalidate();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onPause() {
        flag=true;
        super.onPause();
    }

}

package cn.edu.cqu.mytestapplication;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈圳林 on 2017/3/22.
 */

public class MyMusicPlayControl {
    void Play(MediaPlayer mediaPlayer01,TextView textView01){
        try{
            if(mediaPlayer01!=null){
                mediaPlayer01.stop();
            }
            //mediaPlayer01.setDataSource("test.mp3");
            mediaPlayer01.prepare();
            mediaPlayer01.start();
            textView01.setText("Start");
        }catch (Exception e){
            textView01.setText(e.toString());
            e.printStackTrace();
        }
    }
    void Stop(MediaPlayer mediaPlayer01,TextView textView01){
        try{
            if(mediaPlayer01!=null){
                mediaPlayer01.stop();
                textView01.setText("Stop");
            }

        }catch (Exception e){
            textView01.setText(e.toString());
            e.printStackTrace();
        }
    }
    void Next(MediaPlayer mediaPlayer01, TextView textView01, boolean blsPause, ArrayList PathList, String CurPath) {

    }
    void Completion(MediaPlayer mediaPlayer01,TextView textView01){
        try {
            mediaPlayer01.release();
            textView01.setText("Completion");
        }catch (Exception e){
            textView01.setText(e.toString());
            e.printStackTrace();
        }
    }
    void Error(MediaPlayer mediaPlayer01,TextView textView01){
        try{
            mediaPlayer01.release();
            textView01.setText("Error");
        }catch (Exception e){
            textView01.setText(e.toString());
            e.printStackTrace();
        }
    }
    void SeekBarControl(SeekBar seekBar){

    }
}

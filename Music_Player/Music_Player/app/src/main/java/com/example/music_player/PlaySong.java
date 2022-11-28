package com.example.music_player;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play;
    ImageView previous;
    ImageView next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
String textContent;
Thread updateSeek;
int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play=findViewById(R.id.pause);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songList");
        textContent=intent.getStringExtra("currentSong");
textView.setText(textContent);
textView.setSelected(true);
position=intent.getIntExtra("position", 0);
        Uri uri=Uri.parse(songs.get(position).toString());
        mediaPlayer=MediaPlayer.create(this, uri);
        mediaPlayer.start();
     seekBar.setMax(mediaPlayer.getDuration());
seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
mediaPlayer.seekTo(seekBar.getProgress());
    }
});
updateSeek=new Thread(){
    @Override
    public void run() {
int currentposition=0;
try {
    while(currentposition<mediaPlayer.getDuration()){
        currentposition=mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentposition);
        sleep(1000);

    }
}catch (Exception e){
    e.printStackTrace();
}
    }
};
updateSeek.start();



play.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(mediaPlayer.isPlaying()){
            play.setImageResource(R.drawable.play);
mediaPlayer.pause();
        }
        else{
            play.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
    }
});
//<---------------->
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

               if(position!=0){
                   position=position-1;
               }
               else{
                   position=songs.size()-1;
               }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

                play.setImageResource(R.drawable.pause);
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
//        <--------------------->
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri=Uri.parse(songs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

                play.setImageResource(R.drawable.pause);
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);



            }
        });
    }
}
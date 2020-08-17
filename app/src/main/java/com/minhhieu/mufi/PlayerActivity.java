package com.minhhieu.mufi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.minhhieu.mufi.AlbumDetailsAdapter.albumFiles;
import static com.minhhieu.mufi.MenuActivity.musicFiles;
import static com.minhhieu.mufi.MenuActivity.repeatBoolean;
import static com.minhhieu.mufi.MenuActivity.shuffleBoolean;
import static com.minhhieu.mufi.MusicAdapter.mFiles;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
    TextView song_name, artist_name, duration_player, duration_total;
    ImageView cover_art, nextBtn, prevBtn, backBtn, ShuffleBtn, repeatBtn;
    FloatingActionButton PlaypauseBtn;
    SeekBar seekBar;
    int position = -1;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser)
                {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(mCurrentPosition);
                    duration_player.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000); // khắc phục lỗi chậm trễ khi vuốt seekbar
            }
        });
            ShuffleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(shuffleBoolean)
                    {
                        shuffleBoolean = false;
                        ShuffleBtn.setImageResource(R.drawable.ic_shuffle_off);
                    }
                    else
                    {
                        shuffleBoolean = true;
                        ShuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                    }
                }
            });
            repeatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(repeatBoolean)
                    {
                        repeatBoolean = false;
                        repeatBtn.setImageResource(R.drawable.ic_repeat_off);
                    }
                    else
                    {
                        repeatBoolean = true;
                        repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                    }
                }
            });


    }

    @Override
    protected void onResume() {
        playThreadBtn();
        nextThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void prevThreadBtn() {
        prevThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnCliked();
                    }
                });
            }
        };
        prevThread.start();

    }

    private void prevBtnCliked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            // dữ kiện nút shuffle khi prev song
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listSongs.size() -1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) <0 ? (listSongs.size() - 1):(position - 1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            PlaypauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listSongs.size() -1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) <0 ? (listSongs.size() - 1):(position - 1));
            }

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            PlaypauseBtn.setBackgroundResource(R.drawable.ic_play);
        }

    }


    private void nextThreadBtn() {
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnCliked();
                    }
                });
            }
        };
        nextThread.start();

    }

    private void nextBtnCliked() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            // dữ kiện nút shuffle khi next song
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listSongs.size() -1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }
            // else position will be position ..

            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            PlaypauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listSongs.size() -1);
            }
            else if(!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSongs.size());
            }
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            PlaypauseBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i+1);
    }


    private void playThreadBtn() {
        playThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                PlaypauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlaypauseBtnCliked();
                    }
                });
            }
        };
        playThread.start();
        
    }

    private void PlaypauseBtnCliked() {

        if (mediaPlayer.isPlaying()) {
            PlaypauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
        }
        else
        {
            PlaypauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);

                    }
                    handler.postDelayed(this, 1000); // khắc phục lỗi chậm chễ khi vuốt seekbar
                }
            });
        }
    }


    // chuyển thời lượng một bài hát vào tv
    private String formattedTime(int mCurrentPosition) {
        String totalout = "";
        String totalnew = "";
        String second = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalout = minutes + ":" + second;
        totalnew = minutes + ":"+"0"+second;
        if(second.length() == 1)
        {
            return totalnew;
        }
        else{
            return totalout;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        String sender = getIntent().getStringExtra("sender");
        if(sender != null && sender.equals("albumDetails"))
        {
            listSongs = albumFiles;
        }
        else
        {
            listSongs = mFiles;
        }
        if(listSongs != null){
            PlaypauseBtn.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);


    }

    private void initView() {
        song_name = (TextView) findViewById(R.id.textTitle);
        artist_name = (TextView) findViewById(R.id.textArtist);
        duration_player = (TextView) findViewById(R.id.textCurrentTime);
        duration_total = (TextView) findViewById(R.id.textTotalTime);
        cover_art = (ImageView) findViewById(R.id.imageAlbum);
        nextBtn = (ImageView) findViewById(R.id.btn_next);
        prevBtn = (ImageView) findViewById(R.id.btn_pre);
        ShuffleBtn = (ImageView) findViewById(R.id.buttonShuffle);
        repeatBtn = (ImageView) findViewById(R.id.buttonRepeat);
        PlaypauseBtn = (FloatingActionButton) findViewById(R.id.butonPlay);
        seekBar = (SeekBar) findViewById(R.id.playerSeekBar);
        //backBtn = (ImageView) findViewById(R.id.btn_back);
        
    }
    // xử lí thời lượng bài hát, cover_art trong playerActivity
    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        duration_total.setText(formattedTime(durationTotal));
        byte[] art = retriever.getEmbeddedPicture();
        if(art != null)
        {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);

        }else
        {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.album_art)
                    .into(cover_art);
        }

    }
    public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnCliked();
        if(mediaPlayer != null)
        {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}
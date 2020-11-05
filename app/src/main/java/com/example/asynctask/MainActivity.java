package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    ImageView gif_view;
    String FILE_NAME = "font.zip";
    String LINK_ADDRESS ="https://dl.dafont.com/dl/?f=soulwave";
    String FILE = "/data/data/com.example.asynctask/databases/";
    Button btn_download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        playGif();

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if file doesnt exist in the storage
                File file = new File(FILE + FILE_NAME);
                if (!file.exists()) {

                    //download file using asynctask and show percentage progressbar
                    myasynctask async = new myasynctask(MainActivity.this, FILE_NAME);
                    async.execute(LINK_ADDRESS, FILE);

                } else {

                    Toast.makeText(MainActivity.this, "File exist", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void init() {
        gif_view = findViewById(R.id.gif_view);
        btn_download = findViewById(R.id.btn_download);
    }

    private void playGif() {
        Glide.with(this)
                .load("https://media.giphy.com/media/U9MXYXKnLAqQIRROY2/giphy.gif")
                .into(gif_view);
    }


}
package com.example.datlichkham;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BangTinActivity extends AppCompatActivity {
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_tin);
        Anhxa();
        ActionViewFlipper();
    }

    private void Anhxa() {
        viewFlipper = findViewById(R.id.viewflipper);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangthongtin = new ArrayList<>();
        mangthongtin.add("https://media.dalatcity.org//Images/TNH/superadminportal.tnh/icon-hospital.png_636645912463414113_636867118068924292.png");
        mangthongtin.add("https://vtv1.mediacdn.vn/thumb_w/800/2019/7/18/benh-vien-k4-1563436692089305556965.jpg");
        mangthongtin.add("https://img.lovepik.com/photo/40012/1602.jpg_wh860.jpg");
        mangthongtin.add("https://lh3.googleusercontent.com/proxy/5gFg_ceTLDAlAp4OsV-Bd7JkEXHWULrEsUi-2UDArIfm9iuhohlLSJnzahBTuQPonjWLtqcq_6wHreK3cTuW88IMV_R3H3IlmjSD_Mehpsc0AGKdhLMpB_hTqWP2XDiUIVEgxLbf4iMer_IibeEb7F0OryBAsJkEXju8");
        mangthongtin.add("https://lh3.googleusercontent.com/6w5L1vBwQzxUlTZB4zXWLYhjiCcbCxfgUSS3w5lzFoPdy-DKNhEv9dn3_JNxZM3fU2QK=w200");
        for (int i=0;i<mangthongtin.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangthongtin.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);

        }
        viewFlipper.setFlipInterval(2000);

        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.silde_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);

    }
}
package com.project_moviles2.shop_homesweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class screenSplash extends AppCompatActivity {

    TextView jtvTitulo,jtvSlogan;
    ImageView imagen,sol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_splash);

        jtvSlogan=findViewById(R.id.tvSlogan);
        jtvTitulo=findViewById(R.id.tvTitulo);
        imagen=findViewById(R.id.imagen);
        sol=findViewById(R.id.sol);

        Animation animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.desplazamiento_arriba);
        Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.desplazamiento_abajo);
        Animation animation3= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.opaco);

        jtvTitulo.setAnimation(animation1);
        jtvSlogan.setAnimation(animation2);
        imagen.setAnimation(animation3);
        sol.setAnimation(animation3);
        sol.setAnimation(animation2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        },4000);
    }
}
package com.example.wee_turnbasedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView fc_hp, fc_mana, sc_hp, dialogue;
    ImageView fc, sc, nBtn, rBtn, cBtn, hBtn;
    int FChp, FCmp, FCbase_hp, FCbase_mana, FCmin_dmg, FCmax_dmg, SChp, SCbase_hp, SCmin_dmg, SCmax_dmg, turnCounter, dmg;
    Random rand;
    MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        enableFullscreen();

        // txt views to edit
        fc_hp = findViewById(R.id.fc_hp);
        fc_mana = findViewById(R.id.fc_mp);
        sc_hp = findViewById(R.id.sc_hp);
        dialogue = findViewById(R.id.dialogue);

        // img views to edit
        fc = findViewById(R.id.FC);
        sc = findViewById(R.id.SC);
        nBtn = findViewById(R.id.n_btn);
        rBtn = findViewById(R.id.reset_btn);
        cBtn = findViewById(R.id.crit_btn);
        hBtn = findViewById(R.id.heal_btn);

        // onclick listeners
        nBtn.setOnClickListener(this);
        rBtn.setOnClickListener(this);
        cBtn.setOnClickListener(this);
        hBtn.setOnClickListener(this);

        // FC values
        FChp = 100;
        FCmp = 20;
        FCbase_hp = 100;
        FCbase_mana =20;
        FCmin_dmg = 15;
        FCmax_dmg =50;

        // SC values
        SChp =999;
        SCbase_hp= 999;
        SCmin_dmg = 5;
        SCmax_dmg = 20;

        // System values
        turnCounter = 1;
        rand = new Random();
        rBtn.setVisibility(View.INVISIBLE);
        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.bgm);
        bgm.setLooping(true);
        bgm.start();
        displayChanges();
    }
    private void enableFullscreen() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.n_btn:
            nextTurn();
            break;
        case R.id.reset_btn:
            resetGame();
            break;
        case R.id.crit_btn:
            crit();
            break;
        case R.id.heal_btn:
            heal();
            break;
    }
    if (FChp <= 0) { 
        FChp=0; 
        dialogue.setText("Defeat!");
        fc.setVisibility(View.INVISIBLE);
        rBtn.setVisibility(View.VISIBLE);
        enemyturn();
        nBtn.setVisibility(View.INVISIBLE);
    }
    else if(SChp <= 0) { 
        SChp=0;
        dialogue.setText("Victory!");
        sc.setVisibility(View.INVISIBLE);
        rBtn.setVisibility(View.VISIBLE);
        enemyturn();
        nBtn.setVisibility(View.INVISIBLE);
    }
    displayChanges();
    }

    private void displayChanges() {
        fc_hp.setText("HP:"+ FChp);
        fc_mana.setText("HP:"+ FCmp);
        sc_hp.setText("HP:"+ SChp);
    }

    private void nextTurn() {
        switch(turnCounter % 2){
            case 1:
                dmg = rand.nextInt(FCmax_dmg- FCmin_dmg)+ FCmin_dmg;
                SChp -= dmg;
                dialogue.setText("Alghrid did " + dmg+" to Err.orcrypt!");
                enemyturn();
                FCmp +=1;
                break;
            case 0:
                dmg = rand.nextInt(SCmax_dmg- SCmin_dmg)+ SCmin_dmg;
                FChp -= dmg;
                dialogue.setText("Err.orcrypt did " + dmg+" to Alghrid!");
                heroturn();
                break;
        }
        turnCounter++;
    }

    private void crit() {
        if (FCmp<3){
            dialogue.setText("You don't have enough mana!");
            cBtn.setVisibility(View.INVISIBLE);
        } else {
            dmg = rand.nextInt(FCmax_dmg) + FCmax_dmg + 50;
            SChp -= dmg;
            FCmp -= 3;
            dialogue.setText("Alghrid did " + dmg + " to Err.orcrypt!");
            turnCounter++;
            enemyturn();
        }
    }

    private void heal() {
        if (FCmp<5){
            dialogue.setText("You don't have enough mana!");
            hBtn.setVisibility(View.INVISIBLE);
        } else {
            FChp += 50;
            FCmp -= 5;
            if (FChp > 100) {
                FChp = 100;
            }
            dialogue.setText("Alghrid healed himself by 50 hp!");
            turnCounter++;
            enemyturn();
        }
    }

    private void enemyturn() {
        cBtn.setVisibility(View.INVISIBLE);
        hBtn.setVisibility(View.INVISIBLE);
    }

    private void heroturn() {
        hBtn.setVisibility(View.VISIBLE);
        cBtn.setVisibility(View.VISIBLE);
    }
    private void resetGame() {
        FChp = FCbase_hp;
        FCmp = FCbase_mana;
        SChp = SCbase_hp;
        fc.setVisibility(View.VISIBLE);
        sc.setVisibility(View.VISIBLE);
        rBtn.setVisibility(View.INVISIBLE);
        heroturn();
        nBtn.setVisibility(View.VISIBLE);
        dialogue.setText("Begin!");
        turnCounter=1;
    }
}
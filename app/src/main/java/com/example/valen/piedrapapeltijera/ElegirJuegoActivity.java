package com.example.valen.piedrapapeltijera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ElegirJuegoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_juego);
    }
    public void jugarCPU(View v){
        startActivity(new Intent(ElegirJuegoActivity.this, jugarActivity.class));
    }
    public void jugarAmigo(View v){
        startActivity(new Intent(ElegirJuegoActivity.this, JugarAmigoActivity.class));
    }
    public void jugarRandom(View v){
        startActivity(new Intent(ElegirJuegoActivity.this, InvitadoActivity.class));
    }

}

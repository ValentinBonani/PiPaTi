package com.example.valen.piedrapapeltijera;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.valen.piedrapapeltijera.jugarActivity.GANASTE;
import static com.example.valen.piedrapapeltijera.jugarActivity.PERDISTE;

public class Historial extends AppCompatActivity {
    TextView barraResultado;
    static final int CANTIDAD_JUGADAS_A_MOSTRAR = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        barraResultado = findViewById(R.id.barraResultado);
        Bundle datos = getIntent().getExtras();
        int resultadoPartida = datos.getInt("resultado partida");
        if(resultadoPartida == GANASTE){
            barraResultado.setText("GANASTE");
            barraResultado.setBackgroundColor(getResources().getColor(R.color.gano));
        }
        else if(resultadoPartida == PERDISTE){
            barraResultado.setText("PERDISTE");
            barraResultado.setBackgroundColor(getResources().getColor(R.color.pierdo));
        }
        hacerHistorial();

        //flecha para volver hacia atras desde el historial;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /*  PROCEDIMIENTO PARA BORRAR EL HISTORIAL */

    /*    public void borrarHistorial(View v){
        SharedPreferences preferences = getSharedPreferences("Historial", 0);
        preferences.edit().clear().commit();
        borrarTextView(); (No implementado)
        hacerHistorial();
    }*/


    /* Toma los datos del historial y muestra los ultimos 10 en pantalla */
    private void hacerHistorial() {
        SharedPreferences preferencias = getSharedPreferences("Historial", Context.MODE_PRIVATE);
        String arregloString = preferencias.getString("partidas", " ");
        try {
            JSONArray arregloPartidas = new JSONArray(arregloString);
            int tamanoArreglo = arregloPartidas.length();
            int i = tamanoArreglo-1;
            while ((i >= 0) && (i >= tamanoArreglo-CANTIDAD_JUGADAS_A_MOSTRAR)) {
                JSONObject partida = (JSONObject) arregloPartidas.get(i);
                Boolean gano = partida.getBoolean("gano");
                int numeroJugada = partida.getInt("numero de jugada");
                crearTextview(gano,numeroJugada);
                i--;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Crea los TextView en tiempo de ejecucion */
    private void crearTextview(boolean gano, int numeroJugada){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView textView = new TextView(this);
        String cadena;
        if (gano){
            cadena = "GANASTE EN LA JUGADA " + (numeroJugada);
        }
        else{
            cadena = "PERDISTE EN LA JUGADA " + (numeroJugada);
        }
        textView.setText(cadena);
        textView.setGravity(1); // 1 es Center
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        linearLayout.addView(textView);

    }

    /* Flecha para volver hacia atras desde el historial */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

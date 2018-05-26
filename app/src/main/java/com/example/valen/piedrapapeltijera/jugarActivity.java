package com.example.valen.piedrapapeltijera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class jugarActivity extends AppCompatActivity {
    //hola mundo!
    //valem paki
    ImageView imgJugador;
    ImageView imgCPU;
    ImageView simbolo;
    ImageView tijera;
    ImageView papel;
    ImageView piedra;
    TextView jugada;
    TextView txtJugador;
    TextView txtCPU;
    TextView ganador;
    Button verHistorial;

    static final int PIEDRA = 0;
    static final int PAPEL = 1;
    static final int TIJERA = 2;
    static final boolean GANO = true;
    static final boolean PERDIO = false;
    static final int NOJUGO = -1;
    static final int NOELIGIO = -1;
    static final int GANASTE = 2;
    static final int PERDISTE = 1;
    static final int EMPATASTE = 0;

    int puntajeJugador = 0;
    int puntajeCPU = 0;
    int numeroJuagada;
    int queElegi = NOELIGIO;
    //xdxd

    String historial[];
    private int[] imagenes = {R.drawable.piedra, R.drawable.papel, R.drawable.tijera};
    int[] arregloSimbolo = {R.drawable.empate, R.drawable.pierde, R.drawable.gana};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        imgJugador = findViewById(R.id.eleccionJugador);
        imgCPU = findViewById(R.id.eleccionCPU);
        simbolo = findViewById(R.id.simboloJugada);
        jugada = findViewById(R.id.titulo);
        txtJugador = findViewById(R.id.puntajeTextoJugador);
        txtCPU = findViewById(R.id.puntajeTextoCPU);
        piedra = findViewById(R.id.piedra);
        papel = findViewById(R.id.papel);
        tijera = findViewById(R.id.tijera);
        ganador = findViewById(R.id.quienGano);
        verHistorial = findViewById(R.id.historial);


    }
    /* Funcion que devuelva la jugada de la maquina */
    private int jugadaMaquina() {
        Random r = new Random();
        return r.nextInt(3);
    }

    /* Procedimienta para saber quien gano en cada jugada */
    private void quienGana(int jugador, int cpu) {
        numeroJuagada++;
        jugada.setText("Jugada " + String.valueOf(numeroJuagada));
        if ((cpu == PIEDRA && jugador == PAPEL) || (cpu == PAPEL && jugador == TIJERA) || (cpu == TIJERA && jugador == PIEDRA)) {
            gana();
        } else if (cpu == jugador) {
            empate();
        } else {
            pierde();
        }
    }

    /* Cuando se presiona la imagen de piedra */
    public void clickPiedra(View view) {
        queElegi = PIEDRA;
        imgJugador.setImageResource(imagenes[PIEDRA]);
        int cpu = jugadaMaquina();
        imgCPU.setImageResource(imagenes[cpu]);
        queEligioCPU = cpu;
        quienGana(PIEDRA, cpu);
        imgCPU.setVisibility(View.VISIBLE);
        imgJugador.setVisibility(View.VISIBLE);
    }

    /* Cuando se presiona la imagen de papel */
    public void clickPapel(View view) {
        queElegi = PAPEL;
        imgJugador.setImageResource(imagenes[PAPEL]);
        int cpu = jugadaMaquina();
        queEligioCPU = cpu;
        imgCPU.setImageResource(imagenes[cpu]);
        quienGana(PAPEL, cpu);
        imgCPU.setVisibility(View.VISIBLE);
        imgJugador.setVisibility(View.VISIBLE);
    }

    /* Cuando se presiona la imgande de tijera */
    public void clickTijera(View view) {
        queElegi = TIJERA;
        imgJugador.setImageResource(imagenes[TIJERA]);
        int cpu = jugadaMaquina();
        queEligioCPU = cpu;
        imgCPU.setImageResource(imagenes[cpu]);
        quienGana(TIJERA, cpu);
        imgCPU.setVisibility(View.VISIBLE);
        imgJugador.setVisibility(View.VISIBLE);
    }

    /* Cuando se presiona el boton de reiniciar partida */
    public void clickReset(View view) {
        puntajeJugador = 0;
        puntajeCPU = 0;
        numeroJuagada = 1;
        queElegi = -1;
        queEligioCPU = -1;
        resultadoJugado = NOJUGO;
        resultadoPartida = NOJUGO;
        txtJugador.setText(String.valueOf(puntajeJugador));
        txtCPU.setText(String.valueOf(puntajeCPU));
        jugada.setText("Jugada 1");

        piedra.setVisibility(View.VISIBLE);
        papel.setVisibility(View.VISIBLE);
        tijera.setVisibility(View.VISIBLE);
        verHistorial.setVisibility(View.INVISIBLE);
        ganador.setVisibility(View.INVISIBLE);

        imgCPU.setVisibility(View.INVISIBLE);
        imgJugador.setVisibility(View.INVISIBLE);
        simbolo.setVisibility(View.INVISIBLE);
    }

    /* Si en la jugada se empato */
    private void empate() {
        resultadoJugado = EMPATASTE;
        simbolo.setImageResource(arregloSimbolo[EMPATASTE]);
        simbolo.setVisibility(View.VISIBLE);

    }

    /* Si en la jugada se gano */
    private void gana() {
        resultadoJugado = GANASTE;
        simbolo.setImageResource(arregloSimbolo[GANASTE]);
        puntajeJugador++;
        txtJugador.setText(String.valueOf(puntajeJugador));
        simbolo.setVisibility(View.VISIBLE);
        if (puntajeJugador >= 2) {
            pantallaDeGanador(GANO);
            guardarHistorial(GANO);
        }
    }

    /* Si en la jugada se perdio */
    private void pierde() {
        resultadoJugado = PERDISTE;
        simbolo.setImageResource(arregloSimbolo[PERDISTE]);
        puntajeCPU++;
        txtCPU.setText(String.valueOf(puntajeCPU));
        simbolo.setVisibility(View.VISIBLE);
        if (puntajeCPU >= 2) {
            pantallaDeGanador(PERDIO);
            guardarHistorial(PERDIO);
        }
    }

    /* Guarda en formato JSON cada partida sea ganada o perdida y el numero de jugada en que termino */
    private void guardarHistorial(boolean gano) {
        SharedPreferences preferencias = getSharedPreferences("Historial", Context.MODE_PRIVATE);

        try {
            String partidasString = preferencias.getString("partidas", "[]");
            JSONArray partidas = new JSONArray(partidasString);

            JSONObject object = new JSONObject();
            object.put("gano", gano);
            object.put("numero de jugada", numeroJuagada);
            partidas.put(object);

            preferencias.edit().putString("partidas", partidas.toString()).apply();
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }

    /* Boton que accede a una nueva activity para ver el historial */
    public void verHistorial(View view) {
        Intent i = new Intent(this, Historial.class);
        i.putExtra("resultado partida", resultadoPartida);
        startActivity(i);
    }

    /* Procedimiento que muestra si ganaste o perdiste y hace visible el boton ver historial */
    public void pantallaDeGanador(boolean gano) {
        numeroJuagada--;
        jugada.setText("Jugada " + String.valueOf(numeroJuagada));
        piedra.setVisibility(View.INVISIBLE);
        papel.setVisibility(View.INVISIBLE);
        tijera.setVisibility(View.INVISIBLE);
        verHistorial.setVisibility(View.VISIBLE);
        ganador.setVisibility(View.VISIBLE);

        if (gano) {
            ganador.setBackgroundColor(getResources().getColor(R.color.gano));
            ganador.setText("GANASTE");
            resultadoPartida = GANASTE;
        } else {
            ganador.setBackgroundColor(getResources().getColor(R.color.pierdo));
            ganador.setText("PERDISTE");
            resultadoPartida = PERDISTE;
        }
    }

    protected void onSaveInstanceState(Bundle guardar) {
        super.onSaveInstanceState(guardar);
        guardar.putInt("puntaje jugador", puntajeJugador);
        guardar.putInt("puntaje cpu", puntajeCPU);
        guardar.putInt("eleccion jugador", queElegi);
        guardar.putInt("eleccion cpu", queEligioCPU);
        guardar.putInt("simbolo jugado", resultadoJugado);
        guardar.putInt("resultado partida", resultadoPartida);
        guardar.putInt("numero de jugada", numeroJuagada);
    }

    protected void onRestoreInstanceState(Bundle cargar) {
        super.onRestoreInstanceState(cargar);
        puntajeJugador = cargar.getInt("puntaje jugador");
        puntajeCPU = cargar.getInt("puntaje cpu");
        queElegi = cargar.getInt("eleccion jugador");
        queEligioCPU = cargar.getInt("eleccion cpu");
        resultadoJugado = cargar.getInt("simbolo jugado");
        resultadoPartida = cargar.getInt("resultado partida");
        numeroJuagada = cargar.getInt("numero de jugada");

        txtJugador.setText(String.valueOf(puntajeJugador));
        txtCPU.setText(String.valueOf(puntajeCPU));
        jugada.setText("Jugada " + String.valueOf(numeroJuagada));

        if (!(resultadoJugado == NOJUGO)) {
            simbolo.setVisibility(View.VISIBLE);
            imgJugador.setImageResource(imagenes[queElegi]);
            imgCPU.setImageResource(imagenes[queEligioCPU]);
            simbolo.setImageResource(arregloSimbolo[resultadoJugado]);
        }
        else {
            imgJugador.setVisibility(View.INVISIBLE);
            imgCPU.setVisibility(View.INVISIBLE);
            simbolo.setVisibility(View.INVISIBLE);
        }

        if (!(resultadoPartida == NOJUGO)) {

            if (resultadoPartida == GANASTE) {
                pantallaDeGanador(GANO);
            } else {
                pantallaDeGanador(PERDIO);
            }
        }
    }
}
package com.example.cubomgicoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer timer;
    private TimerTask tarefa;
    private Boolean cronometoParado;
    private Boolean usuarioPressionouTela;
    private TextView cronometroText;
    private static int miliSegundos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        cronometroText = findViewById(R.id.text_cronometro);
        cronometoParado = Boolean.TRUE;
        usuarioPressionouTela = Boolean.FALSE;

        miliSegundos = 0;

        ConstraintLayout telaPrincipal = findViewById(R.id.main);

        telaPrincipal.setOnLongClickListener(view -> {
            if (cronometoParado) {
                usuarioPressionouTela = Boolean.TRUE;
                cronometroText.setTextColor(Color.GREEN);
                cronometroText.setText("0.00");
            }
            return false;
        });

        telaPrincipal.setOnClickListener( view -> {
            if (cronometoParado){

                if (usuarioPressionouTela){
                    iniciarCronometro();
            }else {
                    cronometroText.setTextColor(Color.RED);
                }
            }else {
            usuarioPressionouTela = Boolean.FALSE;
                pararCronometro();
            }

            //Após um tempo ele volta o texto para a cor branca
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    cronometroText.setTextColor(Color.WHITE);
                }
            }, 100);
        });
    }

    public void iniciarCronometro(){
        miliSegundos = 0;
        cronometoParado = Boolean.FALSE;
        timer = new Timer();
        tarefa = new TimerTask() {

            @Override
            public void run() {
                miliSegundos++;

                runOnUiThread(() -> {
                    imprimirTempo(miliSegundos);
                });
            }
        };
        // Agenda a tarefa para iniciar imediatamente e repetir a cada 1000ms (1 segundo)
        timer.schedule(tarefa, 0, 10);
    }

    public void pararCronometro(){
        if (timer != null) {
                cronometoParado = Boolean.TRUE;
                timer.cancel();
                timer = null;

                imprimirTempo(miliSegundos);
            }
    }

    public void imprimirTempo(int miliSegundos){
        int miliSeg = miliSegundos % 100;
        int seg = (miliSegundos / 100) % 60;
        int min =  miliSegundos / 6000;

        miliSeg = (cronometoParado) ? miliSeg : miliSeg/10; //Se o cronometo está parado ele mostrará 0.00 se não sera mostrado 0.0

        if (min < 1) {
            cronometroText.setText(String.format("%01d.%01d", seg, miliSeg));
        } else {
            cronometroText.setText(String.format("%01d.%01d.%01d", min, seg, miliSeg));
        }
    }


}
package com.example.cubomgicoapp;

import android.graphics.Color;
import android.os.Bundle;
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
        miliSegundos = 0;

        ConstraintLayout telaPrincipal = findViewById(R.id.main);

        telaPrincipal.setOnLongClickListener(view -> {
            cronometroText.setTextColor(Color.GREEN);
            cronometroText.setText(String.format("00:0"));
            pararCronometro();
            return false;
        });

        telaPrincipal.setOnClickListener( view -> {
            if (cronometoParado) {
                iniciarCronometro();
            }else {
                pararCronometro();
            }
            cronometroText.setTextColor(Color.WHITE);
        });
    }

    public void iniciarCronometro(){
        cronometoParado = Boolean.FALSE;
        timer = new Timer();
        tarefa = new TimerTask() {

            @Override
            public void run() {
                miliSegundos++;
                int miliSeg = miliSegundos % 100;
                int seg = (miliSegundos / 100) % 60;
                int min =  miliSegundos / 6000;

                runOnUiThread(() -> {
                    if (min < 1) {
                        cronometroText.setText(String.format("%02d:%01d", seg, miliSeg / 10));
                    } else {
                        cronometroText.setText(String.format("%02d:%02d:%01d", min, seg, miliSeg / 10));
                    }
                });
            }
        };
        // Agenda a tarefa para iniciar imediatamente e repetir a cada 1000ms (1 segundo)
        timer.schedule(tarefa, 0, 10);
    }

    public void pararCronometro(){
        cronometoParado = Boolean.TRUE;
        miliSegundos = 0;
        if (timer != null) {
                timer.cancel();
                timer = null;
            }
    }


}
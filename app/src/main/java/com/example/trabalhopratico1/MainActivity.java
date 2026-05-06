package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCadastroDemanda, btnListarChamados;

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

        btnCadastroDemanda = findViewById(R.id.btnCadastroDemanda);
        btnListarChamados = findViewById(R.id.btnListarChamados);

        btnCadastroDemanda.setOnClickListener(this);
        btnListarChamados.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCadastroDemanda) {
            startActivity(new Intent(this, CadastroDemanda.class));
        } else if (view.getId() == R.id.btnListarChamados) {
            startActivity(new Intent(this, ListagemChamados.class));
        }
    }
}
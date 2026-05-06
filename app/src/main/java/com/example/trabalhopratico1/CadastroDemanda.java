package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroDemanda extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txtTitulo, txtDescricao, txtLocal;
    private RadioGroup rgTipo;
    private BD bd;
    private Button btnCadastrar, btnCancelar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_demanda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTitulo = findViewById(R.id.inputTituloDoProblema);
        txtDescricao = findViewById(R.id.inputDescricao);
        txtLocal = findViewById(R.id.inputLocal);
        rgTipo = findViewById(R.id.rgTipo);

        btnCadastrar = findViewById(R.id.btnSalvarCadastro);
        btnCancelar = findViewById(R.id.btnCancelarCadastro);

        btnCadastrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        bd = new BD(this, "bancoChamados", null, 1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSalvarCadastro) {

            String titulo = txtTitulo.getText().toString();
            String descricao = txtDescricao.getText().toString();
            String local = txtLocal.getText().toString();

            int selectedTipo = rgTipo.getCheckedRadioButtonId();
            String tipo = (selectedTipo == R.id.infra) ? "Infraestrutura" : "TI";

            String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            String status = "Aberto";

            Chamado chamado = new Chamado(0, titulo, descricao, local, tipo, dataAtual, status, "");
            bd.inserirChamado(chamado);

            Toast.makeText(this, "Chamado registrado com sucesso em " + dataAtual + "!", Toast.LENGTH_LONG).show();
            finish();

        } else if (view.getId() == R.id.btnCancelarCadastro) {
            finish();
        }
    }
}
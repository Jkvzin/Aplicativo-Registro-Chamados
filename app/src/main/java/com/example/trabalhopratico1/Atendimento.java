package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Atendimento extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText txtTitulo, txtData, txtDescricao, txtLocal, txtTipo, txtSolucao;

    private RadioGroup rgStatus;
    private RadioButton rbAberto, rbEmAtendimento, rbConcluido;

    private Button btnSalvar, btnCancelar;
    private BD bd;
    private int chamadoId = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atendimento);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTitulo = findViewById(R.id.inputTituloDoProblema2);
        txtData = findViewById(R.id.inputData2);
        txtDescricao = findViewById(R.id.inputDescricao2);
        txtLocal = findViewById(R.id.inputLocal2);
        txtTipo = findViewById(R.id.inputTipo2);

        txtSolucao = findViewById(R.id.inputSolucao);

        rgStatus = findViewById(R.id.rgStatus2);
        rbAberto = findViewById(R.id.aberto2);
        rbEmAtendimento = findViewById(R.id.emAtendimento2);
        rbConcluido = findViewById(R.id.concluido2);

        btnSalvar = findViewById(R.id.btnSalvarAtendimento);
        btnCancelar = findViewById(R.id.btnCancelarAtendimento);
        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        bd = new BD(this, "bancoChamados", null, 1);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CHAMADO_ID")) {
            chamadoId = intent.getIntExtra("CHAMADO_ID", -1);
            carregarDadosDoChamado(chamadoId);
        } else {
            Toast.makeText(this, "Erro ao carregar o chamado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void carregarDadosDoChamado(int id) {
        Chamado c = bd.getChamado(id);

        if (c != null) {
            txtTitulo.setText(c.getTitulo());
            txtData.setText(c.getData());
            txtDescricao.setText(c.getDescricao());
            txtLocal.setText(c.getLocal());
            txtTipo.setText(c.getTipo());

            if (c.getStatus().equals("Aberto")) {
                rbAberto.setChecked(true);
            } else if (c.getStatus().equals("Em Atendimento")) {
                rbEmAtendimento.setChecked(true);
            } else {
                rbConcluido.setChecked(true);
            }

            txtSolucao.setText(c.getSolucao());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSalvarAtendimento) {

            String novoStatus = "Aberto";
            int statusSelecionado = rgStatus.getCheckedRadioButtonId();
            if (statusSelecionado == rbEmAtendimento.getId()) {
                novoStatus = "Em Atendimento";
            } else if (statusSelecionado == rbConcluido.getId()) {
                novoStatus = "Concluído";
            }

            String novaSolucao = txtSolucao.getText().toString();

            bd.atualizarAtendimento(chamadoId, novoStatus, novaSolucao);

            Toast.makeText(this, "Atendimento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();

        } else if (view.getId() == R.id.btnCancelarAtendimento) {
            finish();
        }
    }
}
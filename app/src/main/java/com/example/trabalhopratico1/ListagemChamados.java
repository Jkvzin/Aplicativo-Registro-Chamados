package com.example.trabalhopratico1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListagemChamados extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerChamados;
    private BD bd;
    private ChamadoAdapter adaptador;
    private Button voltar;
    private CardView painelFiltros;
    private Button btnToggleFiltro, btnLimparFiltro, btnAplicarFiltro;
    private Spinner spinnerStatus;
    private EditText inputFiltroDataInicio, inputFiltroDataFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listagem_chamados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        voltar = findViewById(R.id.voltar);
        recyclerChamados = findViewById(R.id.recyclerChamados);
        recyclerChamados.setLayoutManager(new LinearLayoutManager(this));

        voltar.setOnClickListener(this);

        painelFiltros = findViewById(R.id.painelFiltros);
        btnToggleFiltro = findViewById(R.id.btnToggleFiltro);
        btnLimparFiltro = findViewById(R.id.btnLimparFiltro);
        btnAplicarFiltro = findViewById(R.id.btnAplicarFiltro);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        inputFiltroDataInicio = findViewById(R.id.inputFiltroDataInicio);
        inputFiltroDataFim = findViewById(R.id.inputFiltroDataFim);

        String[] opcoesStatus = {"Todos", "Aberto", "Em Atendimento", "Concluído"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opcoesStatus);
        spinnerStatus.setAdapter(adapterSpinner);

        btnToggleFiltro.setOnClickListener(v -> {
            if (painelFiltros.getVisibility() == View.GONE) {
                painelFiltros.setVisibility(View.VISIBLE);
                btnToggleFiltro.setText("- Ocultar");
            } else {
                painelFiltros.setVisibility(View.GONE);
                btnToggleFiltro.setText("+ Filtros");
            }
        });

        aplicarMascaraData(inputFiltroDataInicio);
        aplicarMascaraData(inputFiltroDataFim);

        btnAplicarFiltro.setOnClickListener(v -> filtrarChamados());
        btnLimparFiltro.setOnClickListener(v -> limparFiltros());

        bd = new BD(this, "bancoChamados", null, 1);
        carregarListaCompleta();
    }

    private void carregarListaCompleta() {
        ArrayList<Chamado> listaDeChamados = bd.getLista();
        atualizarAdapter(listaDeChamados);
    }

    private void atualizarAdapter(ArrayList<Chamado> lista) {
        adaptador = new ChamadoAdapter(lista, this, chamadoClicado -> {
            Intent intent = new Intent(ListagemChamados.this, Atendimento.class);
            intent.putExtra("CHAMADO_ID", chamadoClicado.getId());
            startActivity(intent);
        });
        recyclerChamados.setAdapter(adaptador);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bd != null) {
            limparFiltros();
        }
    }

    private void limparFiltros() {
        spinnerStatus.setSelection(0);
        inputFiltroDataInicio.setText("");
        inputFiltroDataFim.setText("");
        carregarListaCompleta();
    }

    private void filtrarChamados() {
        String statusSelecionado = spinnerStatus.getSelectedItem().toString();
        String dataInicio = inputFiltroDataInicio.getText().toString();
        String dataFim = inputFiltroDataFim.getText().toString();

        ArrayList<Chamado> todosOsChamados = bd.getLista();
        ArrayList<Chamado> chamadosFiltrados = new ArrayList<>();

        for (Chamado c : todosOsChamados) {
            boolean passaStatus = statusSelecionado.equals("Todos") || c.getStatus().equals(statusSelecionado);

            boolean passaData = verificarIntervaloData(c.getData(), dataInicio, dataFim);

            if (passaStatus && passaData) {
                chamadosFiltrados.add(c);
            }
        }

        atualizarAdapter(chamadosFiltrados);

        painelFiltros.setVisibility(View.GONE);
        btnToggleFiltro.setText("+ Filtros");
    }

    private boolean verificarIntervaloData(String dataChamadoStr, String dataInicioStr, String dataFimStr) {
        if (dataInicioStr.length() < 10 && dataFimStr.length() < 10) return true;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dataChamado = sdf.parse(dataChamadoStr);

            if (dataInicioStr.length() == 10) {
                Date dataInicio = sdf.parse(dataInicioStr);
                if (dataChamado.before(dataInicio)) return false;
            }

            if (dataFimStr.length() == 10) {
                Date dataFim = sdf.parse(dataFimStr);
                if (dataChamado.after(dataFim)) return false;
            }

            return true;
        } catch (ParseException e) {
            return true;
        }
    }

    private void aplicarMascaraData(EditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                String numeros = s.toString().replaceAll("[^\\d]", "");
                String mascara = "";

                if (numeros.length() > 8) {
                    numeros = numeros.substring(0, 8);
                }

                if (numeros.length() <= 2) {
                    mascara = numeros;
                } else if (numeros.length() <= 4) {
                    mascara = numeros.substring(0, 2) + "/" + numeros.substring(2);
                } else {
                    mascara = numeros.substring(0, 2) + "/" + numeros.substring(2, 4) + "/" + numeros.substring(4);
                }

                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.voltar) {
            finish();
        }
    }
}
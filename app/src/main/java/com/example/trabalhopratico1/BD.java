package com.example.trabalhopratico1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BD extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bancoChamados";

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS chamado (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT," +
                        "descricao TEXT," +
                        "local TEXT," +
                        "tipo TEXT," +
                        "data TEXT," +
                        "status TEXT," +
                        "solucao TEXT" +
                        ")"
        );
        Log.i("##", "Tabela chamado criada com sucesso");
    }

    public void deletarRegistro(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("chamado", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Chamado deletado com sucesso");
    }

    public void inserirChamado(Chamado c){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", c.getTitulo());
        valores.put("descricao", c.getDescricao());
        valores.put("local", c.getLocal());
        valores.put("tipo", c.getTipo());
        valores.put("data", c.getData());
        valores.put("status", c.getStatus());
        valores.put("solucao", c.getSolucao());
        db.insert("chamado", null, valores);
        db.close();
        Log.i("##", "Chamado inserido com sucesso");
    }

    public ArrayList<Chamado> getLista(){
        ArrayList<Chamado> lista = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("chamado", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));

                Chamado c = new Chamado(id, titulo, descricao, local, tipo, data, status, solucao);
                lista.add(c);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        for(int i = 0; i < lista.size(); i++){
            Log.i("##", "Listando Chamado: " + lista.get(i).getTitulo() + " | Status: " + lista.get(i).getStatus());
        }
        return lista;
    }

    public Chamado getChamado(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("chamado", null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Chamado c = null;
        if(cursor.moveToFirst()){
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
            String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));

            c = new Chamado(id, titulo, descricao, local, tipo, data, status, solucao);
        }
        cursor.close();
        db.close();
        return c;
    }

    public void atualizarAtendimento(int id, String status, String solucao){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("status", status);
        valores.put("solucao", solucao);

        db.update("chamado", valores, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Atendimento atualizado com sucesso");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
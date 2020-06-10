package com.example.listview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.listview.database.entity.EventoEntity;
import com.example.listview.model.Evento;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    private DBGateway dbGateway;

    public EventoDAO(Context context){
        dbGateway = DBGateway.getInstance(context);
    }

    public boolean salvar(Evento evento){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventoEntity.COLUMN_NAME_NOME,evento.getNomeDoEvento());
        contentValues.put(EventoEntity.COLUMN_NAME_LOCAL,evento.getLocalDoEvento());
        contentValues.put(EventoEntity.COLUMN_NAME_DATA,evento.getDataDoEvento());

        if(evento.getId()>0){
            return dbGateway.getDataBase().update(EventoEntity.TABLE_NAME,
                    contentValues,
                    EventoEntity._ID + "=?",
                    new String[]{String.valueOf(evento.getId())}) >0;
        }

        return dbGateway.getDataBase().insert(EventoEntity.TABLE_NAME,null,contentValues) > 0;
    }

    public boolean excluirEvento(Evento evento){
        return dbGateway.getDataBase().delete(
                EventoEntity.TABLE_NAME,
                EventoEntity._ID + "=?",
                new String[]{String.valueOf(evento.getId())}) == 1;
    }

    public List<Evento> ordenaEventos(@Nullable String pesquisa, boolean ordem_decrescente) {
        /*Se a pesquisa for nula, ele apenas busca todos os Eventos na ordem indicada.
        Se a pesquisa não for nula, busca a string*/

        //Alterada a forma de inicialização do cursor para evitar copia e cola de código.
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor;

        String ordem = "ASC"; //Se ordem_decrescente não for verdadeiro, retorna sempre ascendente.
        if (ordem_decrescente) ordem = "DESC"; //Se for verdadeiro, muda o valor da variável ordem.

        /* Adicionado "COLLATE NOCASE" na query para que eventos com letras iniciais minúsculas e maiúsuclas
        sejam ordenados conjuntamente, ao invés de primeiro os maiúsculos e depois os minúsculos.
        Ordem crescente antes: Aaa Bbb Ddd ccc
        Ordem crescente agora: Aaa Bbb ccc Ddd
        */

        if (pesquisa != null) { //Busca a string em ordem.
            cursor = dbGateway.getDataBase().rawQuery("select distinct * from " + EventoEntity.TABLE_NAME + " where " + EventoEntity.COLUMN_NAME_NOME + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_LOCAL + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_DATA + " LIKE \"%" + pesquisa + "%\"" + " ORDER BY " + EventoEntity.COLUMN_NAME_NOME + " COLLATE NOCASE " + ordem, null);
        } else { //Busca * em ordem.
            cursor = dbGateway.getDataBase().query(EventoEntity.TABLE_NAME, new String[]{"*"}, "", null, "", "", EventoEntity.COLUMN_NAME_NOME + " COLLATE NOCASE " + ordem);
        }

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(EventoEntity._ID));
            String nome = cursor.getString(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_NOME));
            String local = cursor.getString(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_LOCAL));
            String data = cursor.getString(cursor.getColumnIndex(EventoEntity.COLUMN_NAME_DATA));
            eventos.add(new Evento(id,nome,local,data));
        }
        cursor.close();
        return eventos;
    }

}
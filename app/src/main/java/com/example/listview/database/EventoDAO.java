package com.example.listview.database;

// Excluídos imports desnecessários. - Johann
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
    private final String SQL_LISTAR_TODOS = "SELECT * FROM " + EventoEntity.TABLE_NAME;
    private final String SQL_LISTAR_EVENTOS_FILTRADOS = "SELECT * FROM " + EventoEntity.TABLE_NAME +
            " WHERE " + EventoEntity.COLUMN_NAME_NOME  + " LIKE ? INNER JOIN "
            + "SELECT * FROM " + EventoEntity.TABLE_NAME +
            " WHERE " + EventoEntity.COLUMN_NAME_LOCAL  + " LIKE ? INNER JOIN"
            + "SELECT * FROM " + EventoEntity.TABLE_NAME +
            " WHERE " + EventoEntity.COLUMN_NAME_DATA  + " LIKE ?";

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

    /* public List<Evento> listar(){
    DEPRECADO
    SUBSTITUÍDO PELO ordenaEventos() - Johann
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = dbGateway.getDataBase().rawQuery(SQL_LISTAR_TODOS,null);

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

    public List<Evento> pesquisarEventos(String pesquisa, String order){
    DEPRECADO
    Substituído por ordenaEventos().
        List<Evento> eventos = new ArrayList<>();
        pesquisa = pesquisa.trim();
        Cursor cursor;
        //Alterada a forma de iniciar o cursor para evitar dupla inicialização. - Johann
        //Adicionado COLLATE NOCASE na pesquisa ordenada para juntar minúsculos e maiúsculos - Johann.
        if (!order.equals("")) {
            cursor = dbGateway.getDataBase().rawQuery("select distinct * from " + EventoEntity.TABLE_NAME + " where " + EventoEntity.COLUMN_NAME_NOME + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_LOCAL + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_DATA + " LIKE \"%" + pesquisa + "%\"" + " ORDER BY " + EventoEntity.COLUMN_NAME_NOME + " COLLATE NOCASE " + order, null);
        } else {
            cursor = dbGateway.getDataBase().rawQuery("select distinct * from " + EventoEntity.TABLE_NAME + " where " + EventoEntity.COLUMN_NAME_NOME + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_LOCAL + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_DATA + " LIKE \"%" + pesquisa + "%\"", null);
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
    - Johann */

    public List<Evento> ordenaEventos(@Nullable String pesquisa, boolean ordem_decrescente) {
        /* ordenaEventos substitui agora também o pesquisaeventos().
        Se a pesquisa for nula, ele apenas busca todos os Eventos na ordem indicada.
        Se a pesquisa não for nula, busca a string
        - Johann */

        //Alterada a forma de inicialização do cursor para evitar copia e cola de código. - Johann
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor;

        String ordem = "ASC"; //Se ordem_decrescente não for verdadeiro, retorna sempre ascendente. - Johann
        if (ordem_decrescente) ordem = "DESC"; //Se for verdadeiro, muda o valor da variável ordem. - Johann

        /* Adicionado "COLLATE NOCASE" na query para que eventos com letras iniciais minúsculas e maiúsuclas
        sejam ordenados conjuntamente, ao invés de primeiro os maiúsculos e depois os minúsculos.
        Ordem crescente antes: Aaa Bbb Ddd ccc
        Ordem crescente agora: Aaa Bbb ccc Ddd
        - Johann */

        if (pesquisa != null) { //Busca a string em ordem. - Johann
            cursor = dbGateway.getDataBase().rawQuery("select distinct * from " + EventoEntity.TABLE_NAME + " where " + EventoEntity.COLUMN_NAME_NOME + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_LOCAL + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_DATA + " LIKE \"%" + pesquisa + "%\"" + " ORDER BY " + EventoEntity.COLUMN_NAME_NOME + " COLLATE NOCASE " + ordem, null);
        } else { //Busca * em ordem. - Johann
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

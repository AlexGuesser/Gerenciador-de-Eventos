package com.example.listview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

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

    public boolean exluirEvento(Evento evento){

        return dbGateway.getDataBase().delete(
                EventoEntity.TABLE_NAME,
                EventoEntity._ID + "=?",
                new String[]{String.valueOf(evento.getId())}) == 1;

    }

    public List<Evento> listar(){

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

    public List<Evento> pesquisarEventos(String pesquisa,String order){

        List<Evento> eventos = new ArrayList<>();
        pesquisa = pesquisa.trim();

        Cursor cursor = dbGateway.getDataBase().rawQuery("select distinct * from "+EventoEntity.TABLE_NAME+" where "+EventoEntity.COLUMN_NAME_NOME+" LIKE \"%"+pesquisa+"%\" or "+EventoEntity.COLUMN_NAME_LOCAL+" LIKE \"%"+pesquisa+"%\"  or " + EventoEntity.COLUMN_NAME_DATA+" LIKE \"%"+pesquisa+"%\"", null);

        if(!order.equals("")) {
            cursor = dbGateway.getDataBase().rawQuery("select distinct * from " + EventoEntity.TABLE_NAME + " where " + EventoEntity.COLUMN_NAME_NOME + " LIKE \"%" + pesquisa + "%\" or " + EventoEntity.COLUMN_NAME_LOCAL + " LIKE \"%" + pesquisa + "%\"  or " + EventoEntity.COLUMN_NAME_DATA + " LIKE \"%" + pesquisa + "%\"" + " ORDER BY " + EventoEntity.COLUMN_NAME_NOME + " " + order, null);
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

    public List<Evento> ordenaEventosDeFormaCrescente(){

        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = dbGateway.getDataBase().query(EventoEntity.TABLE_NAME,new String[]{"*"},
                "", null, "","",
                EventoEntity.COLUMN_NAME_NOME +" ASC ");
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

    public List<Evento> ordenaEventosDeFormaDecrescente(){

        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = dbGateway.getDataBase().query(EventoEntity.TABLE_NAME,new String[]{"*"},
                "", null, "","",
                EventoEntity.COLUMN_NAME_NOME +" DESC ");
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

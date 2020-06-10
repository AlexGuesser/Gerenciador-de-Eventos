package com.example.listview.database.contract;

//Removido import desnecessário - Johann
import com.example.listview.database.entity.EventoEntity;

public final class EventoContract {

    private EventoContract(){

    }

    public static final String criarTabela(){

        return "CREATE TABLE " + EventoEntity.TABLE_NAME + "(" +
                EventoEntity._ID + " INTEGER PRIMARY KEY," +
                EventoEntity.COLUMN_NAME_NOME + " TEXT NOT NULL," +
                EventoEntity.COLUMN_NAME_LOCAL + " TEXT NOT NULL," +
                EventoEntity.COLUMN_NAME_DATA + " TEXT NOT NULL)";
    }

    public static final String removerTabela(){

        return "DROP TABLE IF EXISTS " + EventoEntity.TABLE_NAME;

    }

}

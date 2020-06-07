package com.example.listview.database.entity;

import android.provider.BaseColumns;

public class EventoEntity  implements BaseColumns {

    private EventoEntity(){

    }

    public static final String TABLE_NAME = "eventos";
    public static final String COLUMN_NAME_NOME = "nome";
    public static final String COLUMN_NAME_LOCAL = "local";
    public static final String COLUMN_NAME_DATA = "data";

}
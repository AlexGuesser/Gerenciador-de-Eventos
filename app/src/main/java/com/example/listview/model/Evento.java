package com.example.listview.model;

import java.io.Serializable;

public class Evento implements Serializable {

    private String nomeDoEvento;
    private String localDoEvento;
    private String dataDoEvento;

    public Evento() {
    }

    public Evento(String nomeDoEvento, String localDoEvento, String dataDoEvento) {
        this.nomeDoEvento = nomeDoEvento;
        this.localDoEvento = localDoEvento;
        this.dataDoEvento = dataDoEvento;
    }

    public String getNomeDoEvento() {
        return nomeDoEvento;
    }

    public void setNomeDoEvento(String nomeDoEvento) {
        this.nomeDoEvento = nomeDoEvento;
    }

    public String getLocalDoEvento() {
        return localDoEvento;
    }

    public void setLocalDoEvento(String localDoEvento) {
        this.localDoEvento = localDoEvento;
    }

    public String getDataDoEvento() {
        return dataDoEvento;
    }

    public void setDataDoEvento(String dataDoEvento) {
        this.dataDoEvento = dataDoEvento;
    }
}

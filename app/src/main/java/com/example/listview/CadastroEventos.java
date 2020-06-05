package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listview.database.EventoDAO;
import com.example.listview.model.Evento;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadastroEventos extends AppCompatActivity{

    private EditText editTextNomeEvento;
    private EditText editTextLocalEvento;
    private TextView textViewDataEvento;

    private int id = 0;

    static final int DATE_DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos);

        setTitle("Cadastro de eventos");

        //Configurações iniciais
        editTextNomeEvento = findViewById(R.id.editTextNomeEvento);
        textViewDataEvento = findViewById(R.id.textViewDataSelecionada);
        editTextLocalEvento= findViewById(R.id.editTextLocalEvento);

        Intent intent = getIntent();
        if(intent!=null && intent.getExtras()!= null && intent.getExtras().get("eventoEdicao") != null){

            Evento evento = (Evento) intent.getExtras().get("eventoEdicao");
            editTextNomeEvento.setText(evento.getNomeDoEvento());
            editTextLocalEvento.setText(evento.getLocalDoEvento());
            textViewDataEvento.setText(evento.getDataDoEvento());
            id = evento.getId();

        }
    }

    public void voltar(View v){

        finish();

    }

    public void salvarEvento(View view){

        String nome = editTextNomeEvento.getText().toString();
        String local = editTextLocalEvento.getText().toString();
        String data = textViewDataEvento.getText().toString();


        if(nome == null  || nome.equals("")){
            Toast.makeText(this,"Adicione um nome para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }

        if(local == null  || local.equals("")){
            Toast.makeText(this,"Adicione um local para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }

        if(data == null  || data.equals("DD/MM/AAAA")){
            Toast.makeText(this,"Adicione uma data para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }


        Evento evento = new Evento(id,nome, local,data);
        EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
        boolean salvou = eventoDAO.salvar(evento);
        if(salvou){
            finish();
        }else{
            Toast.makeText(CadastroEventos.this,"Erro ao salvar produto",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();


        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        mDateSetListener,
                        ano, mes, dia);


                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    String data = String.valueOf(dayOfMonth) + "/"
                            + String.valueOf(monthOfYear+1) + "/" + String.valueOf(year);


                    textViewDataEvento.setText(data);
                    //Toast.makeText(getApplicationContext(),
                            //"DATA = " + data, Toast.LENGTH_SHORT)
                            //.show();
                }
            };


    public void mostraCalendario(View v) {
            showDialog(DATE_DIALOG_ID);
    }

}

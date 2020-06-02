package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listview.model.Evento;

public class CadastroEventos extends AppCompatActivity {

    private EditText editTextNomeEvento;
    private EditText editTextLocalEvento;
    private EditText editTextDataEvento;

    private final int RESULT_COD_NOVO_EVENTO = 10;
    private final int RESULT_COD_EVENTO_EDITADO = 11;

    private boolean edicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos);

        setTitle("Cadastro de eventos");

        //Configurações iniciais
        editTextNomeEvento = findViewById(R.id.editTextNomeEvento);
        editTextDataEvento = findViewById(R.id.textViewDataEvento);
        editTextLocalEvento= findViewById(R.id.editTextLocalEvento);

        Intent intent = getIntent();
        if(intent!=null && intent.getExtras()!= null && intent.getExtras().get("eventoEdicao") != null){

            edicao = true;
            Evento evento = (Evento) intent.getExtras().get("eventoEdicao");
            editTextNomeEvento.setText(evento.getNomeDoEvento());
            editTextLocalEvento.setText(evento.getLocalDoEvento());
            editTextDataEvento.setText(evento.getDataDoEvento());

        }
    }

    public void voltar(View v){

        finish();

    }

    public void salvarEvento(View view){

        String nome = editTextNomeEvento.getText().toString();
        String local = editTextLocalEvento.getText().toString();
        String data = editTextDataEvento.getText().toString();

        Log.i("teste",nome+local+data);

        if(nome == null  || nome.equals("")){
            Toast.makeText(this,"Adicione um nome para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }

        if(local == null  || local.equals("")){
            Toast.makeText(this,"Adicione um local para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }

        if(data == null  || data.equals("")){
            Toast.makeText(this,"Adicione uma data para seu evento!",Toast.LENGTH_LONG).show();
            return;
        }


        Evento evento = new Evento(nome, local,data);


        if(edicao){

            Intent intent = new Intent();
            intent.putExtra("eventoEditado",evento);
            setResult(RESULT_COD_EVENTO_EDITADO,intent);

        }else {

            Intent intent = new Intent();
            intent.putExtra("evento", evento);
            setResult(RESULT_COD_NOVO_EVENTO, intent);
        }

        finish();





    }
}

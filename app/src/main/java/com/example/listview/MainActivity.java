package com.example.listview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listview.adapter.EventosAdapter;
import com.example.listview.database.EventoDAO;
import com.example.listview.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int posicao;
    private ListView listaViewEventos;
    private ArrayAdapter<Evento> adapterEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eventos");

        listaViewEventos = findViewById(R.id.listViewEventos);

        defineOnClickListener();

        listaViewEventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Excluir Evento");
                alertDialog.setMessage("Você tem certeza que deseja excluir este evento?");
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Evento eventoASerRemovido = adapterEventos.getItem(position);
                        EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
                        eventoDAO.exluirEvento(eventoASerRemovido);
                        Toast.makeText(MainActivity.this,"Evento Excluído", Toast.LENGTH_SHORT).show();
                        recuperaListaDeEventos();
                    }
                });

                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Exlusão cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();
                return true;
            }
        });


    }


    private void defineOnClickListener(){

        listaViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Evento eventoClicado = adapterEventos.getItem(position);
                Intent intent = new Intent(MainActivity.this, CadastroEventos.class);
                intent.putExtra("eventoEdicao",eventoClicado);
                posicao = position;
                startActivity(intent);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        recuperaListaDeEventos();
    }

    private void recuperaListaDeEventos() {
        EventoDAO eventoDAO = new EventoDAO(getBaseContext());
        ArrayList<Evento> eventos = new ArrayList<>(eventoDAO.listar());
        adapterEventos = new EventosAdapter(
                MainActivity.this,
                eventos);
        listaViewEventos.setAdapter(adapterEventos);
    }

    public void abreCadastroProdutos(View view){

        Intent i = new Intent(this, CadastroEventos.class);
        startActivity(i);

    }

}

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
import com.example.listview.model.Evento;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_COD_NOVO_EVENTO = 1;
    private final int REQUEST_COD_EDITAR_EVENTO = 2;
    private final int RESULT_COD_NOVO_EVENTO = 10;
    private final int RESULT_COD_EVENTO_EDITADO = 11;

    private int posicao;
    private ListView listaViewEventos;
    private ArrayAdapter<Evento> adapterEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Eventos");

        listaViewEventos = findViewById(R.id.listViewEventos);
        ArrayList<Evento> eventos = criaListaEventos();

        adapterEventos = new EventosAdapter(MainActivity.this, eventos);
        listaViewEventos.setAdapter(adapterEventos);


        listaViewEventos.setAdapter(adapterEventos);

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
                        adapterEventos.remove(eventoASerRemovido);
                        Toast.makeText(MainActivity.this,"Evento Excluído", Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent,REQUEST_COD_EDITAR_EVENTO);

            }
        });

    }

    private ArrayList<Evento> criaListaEventos(){

        ArrayList<Evento> eventos = new ArrayList<>();
        eventos.add(new Evento("Formatura Eng. Mecânica","Casa Rosa","20/10/2020"));
        eventos.add(new Evento("Formatura Eng. Civil","Casa Rosa","27/10/2020"));
        eventos.add(new Evento("Formatura Eng. Química","Casa Rosa","04/11/2020"));


        return eventos;
    }

    public void abreCadastroProdutos(View view){

        Intent i = new Intent(this, CadastroEventos.class);
        startActivityForResult(i,REQUEST_COD_NOVO_EVENTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_COD_NOVO_EVENTO){

            Evento evento = (Evento) data.getExtras().getSerializable("evento");
            adapterEventos.add(evento);

        }else if(resultCode == RESULT_COD_EVENTO_EDITADO){

            Evento eventoEditado = (Evento) data.getExtras().getSerializable("eventoEditado");
            Evento eventoASerEditado =  adapterEventos.getItem(posicao);
            adapterEventos.remove(eventoASerEditado);
            adapterEventos.insert(eventoEditado,posicao);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

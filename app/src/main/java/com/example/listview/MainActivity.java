package com.example.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.listview.adapter.EventosAdapter;
import com.example.listview.database.EventoDAO;
import com.example.listview.model.Evento;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int posicao;
    private ListView listaViewEventos;
    private ArrayAdapter<Evento> adapterEventos;
    private MaterialSearchView searchView;
    private LinearLayout linearLayoutBotoes;

    private RadioButton radioButtonCrescente;
    private RadioButton radioButtonDecrescente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("EVENTOS");
        setSupportActionBar(toolbar);

        //Configurando Radio Buttons
        radioButtonCrescente = findViewById(R.id.radioButtonCrescente);
        radioButtonDecrescente = findViewById(R.id.radioButtonDecrescente);
        linearLayoutBotoes = findViewById(R.id.LinearLayoutBotoes);

        //Confirando o searchView
        searchView =  findViewById(R.id.materialSearchPrincipal);


        //
        radioButtonCrescente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
                ArrayList<Evento> eventos = new ArrayList<>(eventoDAO.ordenaEventosDeFormaCrescente());
                adapterEventos = new EventosAdapter(
                        MainActivity.this,
                        eventos);
                listaViewEventos.setAdapter(adapterEventos);
            }
        });

        radioButtonDecrescente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
                ArrayList<Evento> eventos = new ArrayList<>(eventoDAO.ordenaEventosDeFormaDecrescente());
                adapterEventos = new EventosAdapter(
                        MainActivity.this,
                        eventos);
                listaViewEventos.setAdapter(adapterEventos);
            }
        });

        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {



                linearLayoutBotoes.setVisibility(View.GONE);


            }

            @Override
            public void onSearchViewClosed() {

                EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
                ArrayList<Evento> eventos = new ArrayList<>();

                if(radioButtonCrescente.isChecked()){
                    eventos = (ArrayList<Evento>) eventoDAO.ordenaEventosDeFormaCrescente();
                    adapterEventos = new EventosAdapter(
                            MainActivity.this,
                            eventos);
                    listaViewEventos.setAdapter(adapterEventos);
                }
                else if(radioButtonDecrescente.isChecked()) {
                    eventos = (ArrayList<Evento>) eventoDAO.ordenaEventosDeFormaDecrescente();
                    adapterEventos = new EventosAdapter(
                            MainActivity.this,
                            eventos);
                    listaViewEventos.setAdapter(adapterEventos);
                }else{
                    recuperaListaDeTodosEventos();
                }

                linearLayoutBotoes.setVisibility(View.VISIBLE);

            }
        });

        //Listener para a caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                View mSearchLayout = findViewById(R.id.search_layout);
                ImageButton mEmptyBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_empty_btn);
                mEmptyBtn.setVisibility(View.GONE);

                String order = "";

                if(radioButtonCrescente.isChecked()){
                    order = "ASC";
                }
                if(radioButtonDecrescente.isChecked()) {
                    order = "DESC";
                }


                EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
                if(newText != null) {
                    ArrayList<Evento> eventos = new ArrayList<>(eventoDAO.pesquisarEventos(newText, order));
                    adapterEventos = new EventosAdapter(
                            MainActivity.this,
                            eventos);
                    listaViewEventos.setAdapter(adapterEventos);
                }

                return true;
            }
        });



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
                        recuperaListaDeTodosEventos();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        //Configurando botão de pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
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

        EventoDAO eventoDAO = new EventoDAO(getApplicationContext());
        ArrayList<Evento> eventos = new ArrayList<>();

        if(radioButtonCrescente.isChecked()){
            eventos = (ArrayList<Evento>) eventoDAO.ordenaEventosDeFormaCrescente();
            adapterEventos = new EventosAdapter(
                    MainActivity.this,
                    eventos);
            listaViewEventos.setAdapter(adapterEventos);
        }
        else if(radioButtonDecrescente.isChecked()) {
            eventos = (ArrayList<Evento>) eventoDAO.ordenaEventosDeFormaDecrescente();
            adapterEventos = new EventosAdapter(
                    MainActivity.this,
                    eventos);
            listaViewEventos.setAdapter(adapterEventos);
        }else{
            recuperaListaDeTodosEventos();
        }





        super.onResume();

    }

    private void recuperaListaDeTodosEventos() {
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

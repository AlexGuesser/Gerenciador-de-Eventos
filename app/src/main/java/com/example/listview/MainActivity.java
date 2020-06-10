package com.example.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.listview.adapter.EventosAdapter;
import com.example.listview.database.EventoDAO;
import com.example.listview.model.Evento;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ListView listaViewEventos;
    private ArrayAdapter<Evento> adapterEventos;
    private MaterialSearchView searchView;

    private RadioButton radioButtonCrescente;
    private RadioButton radioButtonDecrescente;
    private EventoDAO eventoDAO;
    private ArrayList<Evento> eventos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configurando Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("EVENTOS");
        setSupportActionBar(toolbar);

        //Configurando Radio Buttons e ConstraintLayout
        radioButtonCrescente = findViewById(R.id.radioButtonCrescente);
        radioButtonDecrescente = findViewById(R.id.radioButtonDecrescente);
        final ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutBotoes);

        //Confirando o searchView
        searchView =  findViewById(R.id.materialSearchPrincipal);

        //Configurando o eventoDAO
        eventoDAO = new EventoDAO(getBaseContext());

        //Configura o ListView
        listaViewEventos = findViewById(R.id.listViewEventos);


        //Listener do radioButtonCrescente que apresenta todos os eventos de forma alfabética crescente
        radioButtonCrescente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventos = (ArrayList<Evento>) eventoDAO.ordenaEventos(null,checkOrderRadios());
                adapterEventos = new EventosAdapter(
                        MainActivity.this,
                        eventos);
                listaViewEventos.setAdapter(adapterEventos);
            }
        });

        //Listener do radioButtonCrescente que apresenta todos os eventos de forma alfabética decrescente
        radioButtonDecrescente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventos = (ArrayList<Evento>) eventoDAO.ordenaEventos(null,checkOrderRadios());
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
                //Coloca a visibilidade do ConstraintLayout da parte inferior da página como invisível
                constraintLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {

                recuperaListaDeTodosEventos();
                //Coloca a visibilidade do ConstraintLayout como visível
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });

        //Listener para a caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String pesquisa) {

                //Coloca a visibilidade do botão [x] da caixa de pesquisa como invisível
                View mSearchLayout = findViewById(R.id.search_layout);
                ImageButton mEmptyBtn = (ImageButton) mSearchLayout.findViewById(R.id.action_empty_btn);
                mEmptyBtn.setVisibility(View.GONE);

                if (pesquisa != null) {
                    //Realiza a pesquisa através do eventoDAO usando como para parâmetros pesquisa e order
                    eventos = (ArrayList<Evento>) eventoDAO.ordenaEventos(pesquisa, checkOrderRadios());
                    adapterEventos = new EventosAdapter(
                            MainActivity.this,
                            eventos);
                    listaViewEventos.setAdapter(adapterEventos);
                }
                return true;
            }
        });

        //Define o evento de clique para os itens do ListView(evento). Abre a cadastro activity para possíveis edições no evento selecionado
        defineOnClickListener();

        //Define evento de longClick para os itens do ListView. Com um longClick é aberto um alertDiolog para possível exclussão
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
                        eventoDAO.excluirEvento(eventoASerRemovido);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { return super.onOptionsItemSelected(item); }

    //Método chamdo quando ocorre cliques nos itens da ListView. Abre CadastroEventos para edições no item clicado.
    private void defineOnClickListener(){

        listaViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento eventoClicado = adapterEventos.getItem(position);
                Intent intent = new Intent(MainActivity.this, CadastroEventos.class);
                intent.putExtra("eventoEdicao",eventoClicado);
                startActivity(intent);
            }
        });
    }


    //onResume apresenta todos os eventos de forma crescente,decrescente ou natural
    @Override
    protected void onResume() {
        recuperaListaDeTodosEventos();
        super.onResume();
    }

    private void recuperaListaDeTodosEventos() {
        //Verifica os Radio buttons, recupera todos eventos na ordem determinada.
        //Retornará por padrão ordem crescente
        eventos = (ArrayList<Evento>) eventoDAO.ordenaEventos(null, checkOrderRadios());
        adapterEventos = new EventosAdapter(
                MainActivity.this,
                eventos);
        listaViewEventos.setAdapter(adapterEventos);
    }

    //Abre CadastroEventos
    public void abreCadastroEventos(View view){
        Intent i = new Intent(this, CadastroEventos.class);
        startActivity(i);
    }

    private boolean checkOrderRadios() {
        //Verifica qual o estado dos Radios de ordem crescente e decrescente.
        boolean ordem_descrescente;
        if(radioButtonCrescente.isChecked()){
            ordem_descrescente = false;
        }
        // Se o radio button decrescente estiver selecionado, retorna todos os eventos de forma decrescente
        else if(radioButtonDecrescente.isChecked()) {
            ordem_descrescente = true;
        }
        // Quando nenhum dos RadioButton está selecionado, retorna todos os eventos em ordem crescente
        else {
            ordem_descrescente = false;
        }
        return ordem_descrescente;
    }

}
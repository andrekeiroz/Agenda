package br.com.agenda.agenda;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.jar.Manifest;

import br.com.agenda.agenda.adapter.AlunosAdapter;
import br.com.agenda.agenda.converter.AlunosConverter;
import br.com.agenda.agenda.dao.AlunoDAO;
import br.com.agenda.agenda.modelo.Aluno;
import br.com.agenda.agenda.modelo.Prova;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);

                Intent vaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                vaiProFormulario.putExtra("aluno", aluno);
                startActivity(vaiProFormulario);
            }
        });

        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(vaiProFormulario);
            }
        });

        registerForContextMenu(listaAlunos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.enviar_notas:
                new EnviaAlunosTask(this).execute();
                break;

            case R.id.baixar_provas:
                Intent vaiParaProvas = new Intent(this, ProvasActivity.class);
                startActivity(vaiParaProvas);
                break;

            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(this, MapaActivity.class);
                startActivity(vaiParaMapa);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        final MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 123);
                }else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:"+aluno.getTelefone()));
                    itemLigar.setIntent(intentLigar);
                }
                return false;
            }
        });


        MenuItem itemSms = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSms.setIntent(intentSMS);


        MenuItem itemMapa = menu.add("Visualizar no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);


        MenuItem itemSite = menu.add("Visitar site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String site = aluno.getSite();
        if(!site.startsWith("http://")){
            site = "http://" + site;
        }

        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);




        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.delete(aluno);
                dao.close();

                carregaLista();
                return false;
            }
        });

    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();


        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        carregaLista();
        super.onResume();
    }

}

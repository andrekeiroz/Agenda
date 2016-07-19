package br.com.agenda.agenda;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.agenda.agenda.modelo.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();

        trans.replace(R.id.frame_principal, new ListaProvasFragment());
        if(estaModoPaisagem()) {
            trans.replace(R.id.frame_secundario, new DetalhesProvaFragment());
        }
        trans.commit();
    }

    private boolean estaModoPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionaProva(Prova prova) {
        FragmentManager manager = getSupportFragmentManager();
        if(!estaModoPaisagem()){
            FragmentTransaction trans = manager.beginTransaction();
            DetalhesProvaFragment detalhesProvaFragment = new DetalhesProvaFragment();
            Bundle parametros = new Bundle();
            parametros.putSerializable("prova", prova);
            detalhesProvaFragment.setArguments(parametros);

            trans.replace(R.id.frame_principal, detalhesProvaFragment);
            trans.addToBackStack(null);
            trans.commit();
        }else{
            DetalhesProvaFragment detalhesProvaFragment = (DetalhesProvaFragment) manager.findFragmentById(R.id.frame_secundario);
            detalhesProvaFragment.populaCampos(prova);
        }
    }
}

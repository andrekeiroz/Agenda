package br.com.agenda.agenda;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.agenda.agenda.FormularioActivity;
import br.com.agenda.agenda.R;
import br.com.agenda.agenda.modelo.Aluno;

/**
 * Created by andre on 11/07/16.
 */
public class FormularioHelper {
    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoEmail;
    private RatingBar campoNota;


    public FormularioHelper(FormularioActivity activity){
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoEmail = (EditText) activity.findViewById(R.id.formulario_email);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
    }

    public Aluno pegaAluno(){
        Aluno aluno = new Aluno();
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoEmail.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        return aluno;
    }
}

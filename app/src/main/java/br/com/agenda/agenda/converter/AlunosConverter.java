package br.com.agenda.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.agenda.agenda.modelo.Aluno;

/**
 * Created by andre on 17/07/16.
 */
public class AlunosConverter {
    public String converteParaJson(List<Aluno> alunos){
        JSONStringer json = new JSONStringer();

        try {
            json.object().key("list").array().object().key("aluno").array();
            for(Aluno aluno : alunos){
                json.object();
                json.key("nome").value(aluno.getNome());
                json.key("nota").value(aluno.getNota());
                json.endObject();
            }
            json.endArray().endObject().endArray().endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}

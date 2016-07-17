package br.com.agenda.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.agenda.agenda.converter.AlunosConverter;
import br.com.agenda.agenda.dao.AlunoDAO;
import br.com.agenda.agenda.modelo.Aluno;

/**
 * Created by andre on 17/07/16.
 */
public class EnviaAlunosTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog dialog;
    private Context context;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando alunos ...", true, true);
    }

    @Override
    protected String doInBackground(Void... params) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        AlunosConverter converter = new AlunosConverter();
        String json = converter.converteParaJson(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);
        return resposta;
    }
}

package br.com.agenda.agenda;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.agenda.agenda.dao.AlunoDAO;
import br.com.agenda.agenda.modelo.Aluno;

/**
 * Created by andre on 19/07/16.
 */
public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicaoEscola = pegaCoordenadodasDoEndereço("Avenida jose candido vieira 1761, Iporá - GO");
        if(posicaoEscola != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoEscola, 17);
            googleMap.moveCamera(update);
        }

        AlunoDAO dao = new AlunoDAO(getContext());
        for(Aluno aluno : dao.buscaAlunos()){
            LatLng coordenada = pegaCoordenadodasDoEndereço(aluno.getEndereco());
            if(coordenada != null){
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(coordenada);
                marcador.title(aluno.getNome());
                marcador.snippet(String.valueOf(aluno.getNota()));
                googleMap.addMarker(marcador);
            }
        }

        new Localizador(getContext(), googleMap);
    }

    private LatLng pegaCoordenadodasDoEndereço(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if(!resultados.isEmpty()){
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

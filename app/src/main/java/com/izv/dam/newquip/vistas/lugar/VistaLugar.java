package com.izv.dam.newquip.vistas.lugar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Lugar;

import java.util.ArrayList;

/**
 * Created by dam on 07/12/2016.
 */

public class VistaLugar extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private ArrayList<Lugar> listaLugares;
    private GoogleMap mapa;
    private GoogleApiClient googleApi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApi == null) {
            googleApi = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Bundle b = getIntent().getExtras();
        listaLugares = b.getParcelableArrayList("lugar");
        int cont = 0;
        for (Lugar i : listaLugares){
            LatLng sitio = new LatLng(i.getLat(), i.getLon());
            if(cont == 0){
                mapa.addMarker(new MarkerOptions().position(sitio).title("Creación, Fecha: "+String.valueOf(i.getFecha())));
            }else{
                mapa.addMarker(new MarkerOptions().position(sitio).title("Modificación: "+cont+", Fecha: "+String.valueOf(i.getFecha())));
            }
            mapa.moveCamera(CameraUpdateFactory.newLatLng(sitio));
            cont++;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApi.disconnect();
        Toast.makeText(this, "Conexión suspendida: "+i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApi.disconnect();
        Toast.makeText(this, "Fallo en la conexión: "+connectionResult, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
    }

    protected void onStart() {
        googleApi.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApi.disconnect();
        super.onStop();
    }
}

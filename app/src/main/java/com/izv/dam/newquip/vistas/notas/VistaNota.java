package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteORM;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.gestion.GestionLugar;
import com.izv.dam.newquip.pojo.Lugar;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilFecha;
import com.izv.dam.newquip.vistas.lugar.VistaLugar;

import java.util.ArrayList;
import java.util.Date;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private GestionLugar gestionLugar;
    private Context contexto = this;
    private FloatingActionButton fabMap;
    private GoogleApiClient googleApi;
    private Location locationSaved;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);
        gestionLugar = new GestionLugar();

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        fabMap = (FloatingActionButton) findViewById(R.id.fabMap);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
            }
        }
        mostrarNota(nota);

        //Mapa
        initMapa();
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLugares(saveNota());
            }
        });
    }

    private void initMapa() {
        if (googleApi == null) {
            googleApi = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onPause() {
        saveNota();
        saveLastLugar();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
    }

    private long saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());
        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);
        }else{
            r = nota.getId();
        }
        return r;
    }

    private int saveLugar(Location location) {
        String fecha = UtilFecha.formatDate(new Date());
        return gestionLugar.insertLugar(this, nota.getId(), location.getLongitude(), location.getLatitude(), fecha);
    }

    public void saveLastLugar() {
        if (ContextCompat.checkSelfPermission(VistaNota.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(VistaNota.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(VistaNota.this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(VistaNota.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(VistaNota.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApi);
        if (locationSaved != null) {
            saveLugar(locationSaved);
        } else if (mLastLocation != null) {
            saveLugar(mLastLocation);
        } else {
            Toast.makeText(this, "No se ha podido guardar la última localización", Toast.LENGTH_SHORT);
        }
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void getLugares(long nota) {
        ArrayList<Lugar> listaLugares = gestionLugar.getLugares(this, nota);
        if (listaLugares == null) {
            saveLastLugar();
            listaLugares = gestionLugar.getLugares(this, nota);
        }
        Intent i = new Intent(contexto, VistaLugar.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("lugar", listaLugares);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }

    protected void onStart() {
        googleApi.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApi.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("On connected");
        //saveLastLugar();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Conexion suspendida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Conexion fallida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        locationSaved = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

}
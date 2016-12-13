package com.izv.dam.newquip.gestion;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.basedatos.AyudanteORM;
import com.izv.dam.newquip.pojo.Lugar;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by dam on 01/12/2016.
 */

public class GestionLugar {
    private AyudanteORM ayudanteORM;

    private AyudanteORM getHelper(Context context) {
        if (ayudanteORM == null) {
            ayudanteORM = OpenHelperManager.getHelper(context, AyudanteORM.class);
        }
        return ayudanteORM;
    }

    public int insertLugar(Context context, long nota, double lon, double lat, String fecha){
        Dao dao;
        try {
            dao = this.getHelper(context).getLugarDao();
            Lugar lugar = new Lugar(nota, lon, lat, fecha);
            return dao.create(lugar);
        } catch (SQLException e) {
            Log.e(TAG, "Error creando lugar");
            return 0;
        }
    }

    public ArrayList<Lugar> getLugares(Context context, long nota) {
        Dao dao;
        try {
            dao = getHelper(context).getLugarDao();
            QueryBuilder<Lugar,Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.setWhere(queryBuilder.where().eq(Lugar.NOTA, nota));
            ArrayList lugares = (ArrayList<Lugar>)dao.query(queryBuilder.prepare());
            if (lugares.isEmpty()) {
                Log.d(TAG, "No se encontraron lugares en la nota");
            } else {
                return lugares;
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error buscando lugares");
            return null;
        }
        return null;
    }

    public int deleteLugares(Context context, long nota){
        Dao dao;
        try {
            dao = getHelper(context).getLugarDao();
            DeleteBuilder<Lugar, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(Lugar.NOTA, nota);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            Log.e(TAG, "Error borrando lugares");
            return 0;
        }
    }
}

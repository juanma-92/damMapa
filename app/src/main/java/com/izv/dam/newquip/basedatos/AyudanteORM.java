package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.izv.dam.newquip.pojo.Lugar;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by dam on 01/12/2016.
 */

public class AyudanteORM extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "androcode_ormlite.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Lugar, Integer> lugarDao;
    private RuntimeExceptionDao<Lugar,Integer> simpleLugarDao = null;

    public AyudanteORM(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Lugar.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }

    public Dao<Lugar, Integer> getLugarDao() throws SQLException, java.sql.SQLException {
        if (lugarDao == null) {
            lugarDao = getDao(Lugar.class);
        }
        return lugarDao;
    }


    public RuntimeExceptionDao<Lugar, Integer> getSimpleRunTimeDao() {
        if (simpleLugarDao==null){
            simpleLugarDao=getRuntimeExceptionDao(Lugar.class);
        }
        return simpleLugarDao;
    }

    public void close() {
        super.close();
        lugarDao = null;
        simpleLugarDao = null;
    }
}

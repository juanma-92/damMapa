package com.izv.dam.newquip.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by dam on 01/12/2016.
 */

@DatabaseTable
public class Lugar implements Parcelable {
    public static final String NOTA = "_nota";
    public static final String LON = "_lon";
    public static final String LAT = "_lat";
    public static final String FECHA = "_fecha";

    @DatabaseField(columnName = NOTA)
    private long nota;
    @DatabaseField(columnName = LON)
    private double lon;
    @DatabaseField(columnName = LAT)
    private double lat;
    @DatabaseField(columnName = FECHA)
    private String fecha;

    public Lugar(){
    }

    public Lugar(long nota, double lon, double lat, String fecha) {
        this.nota = nota;
        this.lon = lon;
        this.lat = lat;
        this.fecha = fecha;
    }

    protected Lugar(Parcel in) {
        nota = in.readLong();
        lon = in.readDouble();
        lat = in.readDouble();
        fecha = in.readString();
    }

    public static final Creator<Lugar> CREATOR = new Creator<Lugar>() {
        @Override
        public Lugar createFromParcel(Parcel in) {
            return new Lugar(in);
        }

        @Override
        public Lugar[] newArray(int size) {
            return new Lugar[size];
        }
    };

    public static String getNOTA() {
        return NOTA;
    }

    public static String getLON() {
        return LON;
    }

    public static String getLAT() {
        return LAT;
    }

    public static String getFECHA() {
        return FECHA;
    }

    public long getNota() {
        return nota;
    }

    public void setNota(long nota) {
        this.nota = nota;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public static Creator<Lugar> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "lugar{" +
                "nota=" + nota +
                ", lon=" + lon +
                ", lat=" + lat +
                ", fecha=" + fecha +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(nota);
        parcel.writeDouble(lon);
        parcel.writeDouble(lat);
        parcel.writeString(fecha);

    }
}

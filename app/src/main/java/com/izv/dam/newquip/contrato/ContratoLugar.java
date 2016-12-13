package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Lugar;

/**
 * Created by dam on 01/12/2016.
 */

public interface ContratoLugar {
    public interface ContratoNota {

        interface InterfaceModelo {

            void close();

            Lugar getNota(long id);

            long saveNota(Lugar n);

        }

        interface InterfacePresentador {

            void onPause();

            void onResume();

            long onSaveNota(Lugar n);

        }

        interface InterfaceVista {

            void mostrarNota(Lugar n);

        }
    }
}

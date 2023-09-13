package com.simonsoft.ul_practico6;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

public class Lector extends Service {
    private Thread hilo;
    private boolean bandera = true;
    private int contador = 0;
    public Lector() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        obtener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bandera = false;
    }

    public void obtener() {
        Uri llamada = Uri.parse("content://sms/inbox");
        ContentResolver cr = this.getContentResolver();
        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (bandera) {

                        Cursor cursor = cr.query(llamada, null, null, null, null);

                        String fechaSMS = null;
                        String textoSMS = null;
                        String contactoSMS = null;

                        StringBuilder respuesta = new StringBuilder();
                        if (cursor.getCount() > 0) {

                            while (cursor.moveToNext()) {
                                int fecha = cursor.getColumnIndex(Telephony.Sms.DATE);
                                int mensaje = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                                int contacto = cursor.getColumnIndex(Telephony.Sms.BODY);

                                fechaSMS = cursor.getString(fecha);
                                textoSMS = cursor.getString(mensaje);
                                contactoSMS = cursor.getString(contacto);
                                respuesta.append("fecha :" + fechaSMS + "mensaje :" + textoSMS + "contacto :" + contactoSMS);
                            }
                        }
                        Log.d("salida", respuesta.toString());
                        Log.d("contador","numero:"+contador);
                        contador++;
                        Thread.sleep(9000);
                    }

                } catch (Exception e) {

                }
            }
        });
        hilo.start();

    }
}




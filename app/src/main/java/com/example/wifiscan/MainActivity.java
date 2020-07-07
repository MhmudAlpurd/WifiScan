package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.DBManager.DBStrings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Wifi gestore;
    private ArrayList<Rete> dati;
    private Button buttonScan;
    private Button btn;

    private DBManager database;
    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DBManager(MainActivity.this.getApplicationContext());

        // inizializzazione della ListView
        listView = findViewById(R.id.view_scan);

        // inizializzazione dei dati
        dati = new ArrayList<Rete>();

        // preso riferimento dei bottoni
        buttonScan = findViewById(R.id.btn_scan);
        btn = findViewById(R.id.button);

        // prende il riferimento della view attuale
        View actualView = getWindow().getDecorView().findViewById(android.R.id.content);

        // inizializzazione del gestore del wifi e della posizione
        gestore = new Wifi(MainActivity.this,  actualView, dati);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabilita i bottoni
                btn.setEnabled(false);
                buttonScan.setEnabled(false);

                // elimina le righie vecchie per evitare di poter far casino con i bottoni
                listView.setAdapter(null);

                // avvia la scnazione del wifi
                gestore.scanWifi();
            }
        });

        // Bottone Temporaneo per dubg
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Rete elem : dati) {
                    Log.d("DATI", elem.toString());
                    boolean result = database.save(elem.getSSID(), elem.getDettagli(), Integer.parseInt(elem.getLevel()), elem.getPassword(), elem.getLat(), elem.getLon());
                }

                /* Per vedere il contenuto del database

                Cursor a = database.query();

                while (a.moveToNext()) {
                    Log.d("DATA_QUERY", a.getString(a.getColumnIndex(DBStrings.FIELD_SSID)) + " " + a.getString(a.getColumnIndex(DBStrings.FIELD_Password)));
                }

                a.close();
                */

            }
        });
    }
}
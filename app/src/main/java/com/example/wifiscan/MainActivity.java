package com.example.wifiscan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Wifi gestore;
    private LocationManager gps;
    private LocationHandler locationHandler;
    private ArrayList<Rete> dati;

    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inizializzazione della ListView
        listView = findViewById(R.id.view_scan);

        dati = new ArrayList<Rete>();

        // inizializzazione del gestore del wifi e della posizione
        gestore = new Wifi(MainActivity.this, listView, dati);
        locationHandler = new LocationHandler(this, dati);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        final Button buttonScan = findViewById(R.id.btn_scan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonScan.setEnabled(false);

                gestore.scanWifi();
                locationHandler.requestUpdate();

                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                buttonScan.setEnabled(true);
                            }
                        }, 10000    //Specific time in milliseconds
                );
            }
        });

        // Bottone Temporaneo per vedere password
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Rete elem : dati) {
                    Log.d("DATI", elem.toString());
                }

            }
        });
    }
}
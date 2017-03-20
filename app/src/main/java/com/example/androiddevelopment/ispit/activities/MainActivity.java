package com.example.androiddevelopment.ispit.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androiddevelopment.ispit.R;
import com.example.androiddevelopment.ispit.db.DatabaseHelper;
import com.example.androiddevelopment.ispit.db.Kontakt;
import com.example.androiddevelopment.ispit.dialogs.AboutDialog;
import com.example.androiddevelopment.ispit.preferences.Podesavanja;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    private SharedPreferences prefs;

    public static String KONTAKT_KEY = "KONTAKT_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";

    // Container Activity must implement this interface
    public interface OnKontaktSelectedListener {
        void onKontaktSelected(int id);
    }

    OnKontaktSelectedListener listener;
    ListAdapter adapter;

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            //actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();

            adapter = new ArrayAdapter<Kontakt>(this, R.layout.list_item, list);

            final ListView listView = (ListView)this.findViewById(R.id.listaKontakti);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Posto radimo sa bazom podataka, svaki element ima jedinstven id
                    // pa je potrebno da vidimo na koji tacno element smo kliknuli.
                    // To mozemo uraditi tako sto izvucemo proizvod iz liste i dobijemo njegov id
                    Kontakt p = (Kontakt) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("position", p.getmId());
                    startActivity(intent);

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void addKontakt() throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_kontakt);

        final EditText kontaktIme = (EditText) dialog.findViewById(R.id.kontakt_ime);
        final EditText kontaktPrezime = (EditText) dialog.findViewById(R.id.kontakt_prezime);

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = kontaktIme.getText().toString();
                String prezime = kontaktPrezime.getText().toString();

                Kontakt glumac = new Kontakt();
                glumac.setmIme(ime);
                glumac.setmPrezime(prezime);

                try {
                    getDatabaseHelper().getKontaktDao().create(glumac);
                    refresh();

                    showStatusMesage("Dodat novi kontakt");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.listaKontakti);

        if (listview != null){
            ArrayAdapter<Kontakt> adapter = (ArrayAdapter<Kontakt>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Kontakt> list = getDatabaseHelper().getKontaktDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notifikacija);
        mBuilder.setContentTitle("Pripremni test");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
    private void showMessage(String message){
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status){
            showStatusMesage(message);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_add:
                Toast.makeText(this, "Action Dodaj executed.", Toast.LENGTH_SHORT).show();
                try {
                    addKontakt();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_about:

                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.action_preferences:
                startActivity(new Intent(MainActivity.this, Podesavanja.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    // onCreateOptionsMenu method initialize the contents of the Activity's Toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}

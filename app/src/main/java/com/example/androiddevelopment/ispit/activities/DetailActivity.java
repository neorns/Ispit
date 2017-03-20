package com.example.androiddevelopment.ispit.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiddevelopment.ispit.R;
import com.example.androiddevelopment.ispit.db.DatabaseHelper;
import com.example.androiddevelopment.ispit.db.Kontakt;
import com.example.androiddevelopment.ispit.db.Telefon;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;


import static com.example.androiddevelopment.ispit.activities.MainActivity.NOTIF_TOAST;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class DetailActivity  extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private Kontakt kontakt;
    private EditText ime;
    private EditText prezime;
    private EditText adresa;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

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
            kontakt = getDatabaseHelper().getKontaktDao().queryForId(getIntent().getIntExtra("position",-1));


            //TextView ime = (TextView) this.findViewById(R.id.tv_ime);
            ime =(EditText) this.findViewById(R.id.et_ime);
            ime.setText(kontakt.getmIme());
            prezime =(EditText) this.findViewById(R.id.et_prezime);
            prezime.setText(kontakt.getmPrezime());
            adresa =(EditText) this.findViewById(R.id.et_adresa);
            adresa.setText(kontakt.getmAdresa());


        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.listaTelefoni);

        try {
            List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder()
                    .where()
                    .eq(Telefon.FIELD_NAME_KONTAKT, kontakt.getmId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.telefon_list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Telefon m = (Telefon) listView.getItemAtPosition(position);
                    //Toast.makeText(DetailActivity.this, m.getmName()+" "+m.getmGenre()+" "+m.getmYear(), Toast.LENGTH_SHORT).show();

                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notifikacija);
        mBuilder.setContentTitle("Ispitni zadatak");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void showMessage(String message){

        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);


        if (toast){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            showStatusMesage(message);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_add:
                //dodavanje telefona
                try {
                    addTelefon();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                showMessage("Dodavanje telefona");
                break;
            case R.id.action_detail_delete:
                try {
                    getDatabaseHelper().getKontaktDao().deleteById(kontakt.getmId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                showMessage("Brisanje kontakta");
                break;
            case R.id.action_detail_edit:

                //POKUPITE INFORMACIJE SA EDIT POLJA
                kontakt.setmIme(ime.getText().toString());
                kontakt.setmPrezime(prezime.getText().toString());
                kontakt.setmAdresa(adresa.getText().toString());

                try {
                    getDatabaseHelper().getKontaktDao().update(kontakt);

                    showMessage("Izmena kontakta");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.listaTelefoni);

        if (listview != null){
            ArrayAdapter<Telefon> adapter = (ArrayAdapter<Telefon>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Telefon> list = getDatabaseHelper().getTelefonDao().queryBuilder()
                            .where()
                            .eq(Telefon.FIELD_NAME_KONTAKT, kontakt.getmId())
                            .query();;

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void addTelefon() throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_telefon);

        final Spinner telefonTip = (Spinner) dialog.findViewById(R.id.telefon_tip);
        final EditText telefonBroj = (EditText) dialog.findViewById(R.id.telefon_broj);

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = String.valueOf(telefonTip.getSelectedItem());
                String broj = telefonBroj.getText().toString();

                Telefon telefon = new Telefon();
                telefon.setKontakt(kontakt);
                telefon.setmTip(tip);
                telefon.setmBroj(broj);

                try {
                    getDatabaseHelper().getTelefonDao().create(telefon);
                    refresh();

                    showStatusMesage("Dodat novi telefon");

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
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


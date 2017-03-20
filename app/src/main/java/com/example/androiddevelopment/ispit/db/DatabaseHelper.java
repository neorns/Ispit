package com.example.androiddevelopment.ispit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    //Dajemo ime bazi
    private static final String DATABASE_NAME    = "kontakti.db";

    //i pocetnu verziju baze. Obicno krece od 1
    private static final int    DATABASE_VERSION = 1;

    private Dao<Telefon, Integer> mTelefonDao = null;
    private Dao<Kontakt, Integer> mKontaktDao = null;

    //Potrebno je dodati konstruktor zbog pravilne inicijalizacije biblioteke
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Prilikom kreiranja baze potrebno je da pozovemo odgovarajuce metode biblioteke
    //prilikom kreiranja moramo pozvati TableUtils.createTable za svaku tabelu koju imamo
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Telefon.class);
            TableUtils.createTable(connectionSource, Kontakt.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //kada zelimo da izmenomo tabele, moramo pozvati TableUtils.dropTable za sve tabele koje imamo
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Kontakt.class, true);
            TableUtils.dropTable(connectionSource, Telefon.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //jedan Dao objekat sa kojim komuniciramo. Ukoliko imamo vise tabela
    //potrebno je napraviti Dao objekat za svaku tabelu
    public Dao<Telefon, Integer> getTelefonDao() throws SQLException {
        if (mTelefonDao == null) {
            mTelefonDao = getDao(Telefon.class);
        }

        return mTelefonDao;
    }
    public Dao<Kontakt, Integer> getKontaktDao() throws SQLException {
        if (mKontaktDao == null) {
            mKontaktDao = getDao(Kontakt.class);
        }

        return mKontaktDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mTelefonDao = null;
        mKontaktDao = null;

        super.close();
    }
}



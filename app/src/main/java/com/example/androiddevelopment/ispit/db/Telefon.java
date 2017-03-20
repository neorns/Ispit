package com.example.androiddevelopment.ispit.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by androiddevelopment on 20.3.17..
 */

@DatabaseTable(tableName = Telefon.TABLE_NAME_TELEFON)
public class Telefon {
    public static final String TABLE_NAME_TELEFON = "telefoni";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_TIP = "tip";
    public static final String FIELD_NAME_BROJ = "broj";
    public static final String FIELD_NAME_KONTAKT = "kontakt";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_TIP)
    private String mTip;
    @DatabaseField(columnName = FIELD_NAME_BROJ)
    private String mBroj;

    @DatabaseField(columnName = FIELD_NAME_KONTAKT, foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Kontakt kontakt;

    public Telefon() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTip() {
        return mTip;
    }

    public void setmTip(String mTip) {
        this.mTip = mTip;
    }

    public String getmBroj() {
        return mBroj;
    }

    public void setmBroj(String mBroj) {
        this.mBroj = mBroj;
    }

    public Kontakt getKontakt() {
        return kontakt;
    }

    public void setKontakt(Kontakt kontakt) {
        this.kontakt = kontakt;
    }

    @Override
    public String toString() {
        return mTip + ": " + mBroj;
    }
}
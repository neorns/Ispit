package com.example.androiddevelopment.ispit.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by androiddevelopment on 20.3.17..
 */

public class Kontakt {

    public static final String TABLE_NAME_KONTAKT = "kontakti";

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_IME = "ime";
    public static final String FIELD_NAME_PREZIME = "prezime";
    public static final String FIELD_NAME_ADRESA = "adresa";
    public static final String FIELD_NAME_SLIKA = "slika";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_IME)
    private String mIme;
    @DatabaseField(columnName = FIELD_NAME_PREZIME)
    private String mPrezime;
    @DatabaseField(columnName = FIELD_NAME_ADRESA)
    private String mAdresa;
    @DatabaseField(columnName = FIELD_NAME_SLIKA)
    private String mSlika;

    @ForeignCollectionField(foreignFieldName = "kontakt")
    private ForeignCollection<Telefon> telefoni ;

    public Kontakt() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmPrezime() {
        return mPrezime;
    }

    public void setmPrezime(String mPrezime) {
        this.mPrezime = mPrezime;
    }

    public String getmAdresa() {
        return mAdresa;
    }

    public void setmAdresa(String mAdresa) {
        this.mAdresa = mAdresa;
    }

    public String getmSlika() {
        return mSlika;
    }

    public void setmSlika(String mSlika) {
        this.mSlika = mSlika;
    }

    public ForeignCollection<Telefon> getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(ForeignCollection<Telefon> telefoni) {
        this.telefoni = telefoni;
    }

    @Override
    public String toString() {
        return mIme + " " + mPrezime;
    }
}

package org.pi.mietverwaltung.mietverwaltung_interface;

import android.util.Log;

/**
 * Created by bro on 16.05.2016.
 */
public class Buchung {

    int id;
    String name;
    int betrag;
    String datum;
    String anlass;
    char typ;

    public Buchung(){
        name = "";
        betrag = 0;
        datum = "";
        typ = ' ';
    }

    public Buchung(int id,String name, int betrag, String datum, char typ){
        this.id = id;
        this.name = name;
        this.betrag = betrag;
        this.datum = datum;
        this.typ = typ;
    }

    public Buchung(Eingang e){
        this.id = e.getId();
        this.name = e.getName();
        this.betrag = e.getBetrag();
        this.datum = e.getDatum();
        this.anlass = e.getAnlass();
        this.typ = 'e';
    }

    public Buchung(Rechnung r){
        this.id = r.getId();
        this.betrag = r.betrag;
        this.name = r.getName();
        this.datum = r.getDatum();
        this.anlass = r.getAnlass();
        this.typ = 'a';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBetrag() {
        return betrag;
    }

    public String getDatum() {

        return this.datum;
    }

    public char getTyp() {
        return typ;
    }

    public String getAnlass() {
        return anlass;
    }

    public void setAnlass(String anlass) {
        this.anlass = anlass;
    }
}

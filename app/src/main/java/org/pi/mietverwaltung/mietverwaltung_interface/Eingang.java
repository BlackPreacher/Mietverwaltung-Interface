package org.pi.mietverwaltung.mietverwaltung_interface;

/**
 * Created by Hellhero on 15.05.2016.
 */
public class Eingang {

    int id;
    String name;
    int betrag;
    String datum;
    String anlass;
    String typ;

    public Eingang(){
        name = "";
        betrag = 0;
        datum = "";
    }

    public Eingang(int id,String name, int betrag, String datum){
        this.id = id;
        this.name = name;
        this.betrag = betrag;
        this.datum = datum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBetrag() {
        return betrag;
    }

    public void setBetrag(int betrag) {
        this.betrag = betrag;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnlass() {
        return anlass;
    }

    public void setAnlass(String anlass) {
        this.anlass = anlass;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}

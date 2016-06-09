package org.pi.mietverwaltung.mietverwaltung_interface;

/**
 * Created by Hellhero on 01.06.2016.
 */
public class Preset_Item {

    String name;
    int betrag;
    String anlass;
    String typ;

    public Preset_Item(String name, int betrag, String anlass, String typ){
        this.name = name;
        this.betrag = betrag;
        this.anlass = anlass;
        this.typ = typ;
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

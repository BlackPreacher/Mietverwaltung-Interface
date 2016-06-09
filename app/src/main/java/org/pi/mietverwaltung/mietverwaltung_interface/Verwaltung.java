package org.pi.mietverwaltung.mietverwaltung_interface;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Hellhero on 15.05.2016.
 */
public class Verwaltung extends Application {

    Konto konto = new Konto();
    Boolean token_retrieved = false;
    Boolean logged_in = false;


    public Verwaltung(){
        //konto = new Konto();
        //konto.read_eingaben_from_database();
        //konto.read_ausgaben_from_database();

        Log.i("Verwaltung","load");
//        load();
//        konto.get_Jahresbilanz_from_database();
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
    }

    public ArrayList<Rechnung> getAusgabenFromKonto(){
        return konto.getAusgaben();
    }

    public ArrayList<Eingang> getEingabenFromKonto(){
        return konto.getEingaben();
    }

    public void delete_from_konto(int id, char typ){
        konto.delete_from_database(id,typ);
    }

    public void load(){
        Log.i("verwalter","reload");
        konto.read_eingaben_from_database();
        konto.read_ausgaben_from_database();
    }

    public void get_Jahresbilanz_from_Database(){ konto.get_Jahresbilanz_from_database(); }

    public int get_Jahres_bilanz(){
        return konto.getBilanz();
    }

    public int get_Jahres_einnahmen(){
        return konto.getJahreseinnahmen();
    }

    public int get_Jahres_ausgaben(){
        return konto.getJahresausgaben();
    }

    public void write_einnahme(final String name, final String strbetrag, final String anlass, final String typ){
        int betrag = Integer.parseInt(strbetrag);
        Eingang tmp = new Eingang(0,name,betrag,"0000-00-00");
        tmp.setAnlass(anlass);
        tmp.setTyp(typ);
        konto.write_einnhame(tmp);
    }

    public void write_ausgabe(final String name, final String strbetrag, final String anlass, final String typ){
        int betrag = Integer.parseInt(strbetrag);
        Rechnung tmp = new Rechnung(0,name,betrag,"0000-00-00");
        tmp.setAnlass(anlass);
        tmp.setTyp(typ);
        konto.write_ausgabe(tmp);
    }

    public Boolean getToken_retrieved() {
        return token_retrieved;
    }

    public void setToken_retrieved(Boolean token_retrieved) {
        this.token_retrieved = token_retrieved;
    }

    public void refresh_uebersicht(){
        konto.get_Jahresbilanz_from_database();
    }

    public void refresh_all(){
        refresh_uebersicht();
        konto.read_eingaben_from_database();
        konto.read_ausgaben_from_database();
    }

    public String getLinkPrefix(Context context){
        return konto.getLinkip(context);
    }

    public void setLinkPrefix(Context context, String link){ konto.setLinkip(context, link);}

    public Boolean getNotificationState(Context context) { return konto.getNotificationState(context); }

    public void setNotificationState(Context context, Boolean state) { konto.setNotificationState(context,state); }

    public void setBackupNotificationState(Context context,Boolean state){ konto.setBackupNotificationState(context,state); }

    public Boolean getBackupNotificationState(Context context) { return konto.getBackupNotificationState(context); }

    public Boolean getLogged_in() {
        return logged_in;
    }

    public void setLogged_in(Boolean logged_in) {
        this.logged_in = logged_in;
    }
}

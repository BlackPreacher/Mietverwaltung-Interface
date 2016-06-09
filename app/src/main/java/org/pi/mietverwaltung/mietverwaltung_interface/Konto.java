package org.pi.mietverwaltung.mietverwaltung_interface;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Konto {

    ArrayList<Eingang> eingaben;
    ArrayList<Rechnung> ausgaben;

    int jahreseinnahmen;
    int jahresausgaben;
    int bilanz;

    String linkip = "";

    public Konto(){

        eingaben = new ArrayList<>();
        ausgaben = new ArrayList<>();
    }

    public ArrayList<Eingang> getEingaben() {
        return eingaben;
    }

    public void setEingaben(ArrayList<Eingang> eingaben) {
        this.eingaben = eingaben;
    }

    public ArrayList<Rechnung> getAusgaben() {
        return ausgaben;
    }

    public void setAusgaben(ArrayList<Rechnung> ausgaben) {
        this.ausgaben = ausgaben;
    }

    public void read_eingaben_from_database(){

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"abfrage_aktueller_monat_einnahme.php");
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final JSONArray jArray_ein;
        try {
            jArray_ein = new JSONArray(result);

            Log.i("json decode eingaben",result);

            eingaben.clear();
            Log.i("Konte","empty einnahmen");

            final int laenge_ein = jArray_ein.length();

            for(int i = 0; i < laenge_ein; i++){
                JSONObject data;
                data = jArray_ein.getJSONObject(i);
                Eingang tmp = new Eingang(Integer.parseInt(data.getString("id")),data.getString("name"),Integer.parseInt(data.getString("Betrag")),data.getString("datum_verw"));
                tmp.setAnlass(data.getString("Anlass"));
                tmp.setDatum(data.getString("datum_verw"));
                eingaben.add(tmp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void read_ausgaben_from_database() {

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"abfrage_aktueller_monat_ausgabe.php");
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final JSONArray jArray_ein;
        try {
            jArray_ein = new JSONArray(result);

            Log.i("jsondecode",result);
            final int laenge_ein = jArray_ein.length();
            ausgaben.clear();
            Log.i("Konte","empty ausgaben");

            for(int i = 0; i < laenge_ein; i++){
                JSONObject data;
                data = jArray_ein.getJSONObject(i);
                Rechnung tmp = new Rechnung(Integer.parseInt(data.getString("id")),data.getString("name"),Integer.parseInt(data.getString("Betrag")),data.getString("datum_rechnung"));
                tmp.setAnlass(data.getString("Anlass"));
                tmp.setDatum(data.getString("datum_rechnung"));
                ausgaben.add(tmp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void delete_from_database(final int id, final char typ){

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"delete_by_id.php");
            tmp.put("id",Integer.toString(id));
            tmp.put("typ",Character.toString(typ));
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    public void get_Jahresbilanz_from_database(){

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"abfrage_miete_uebersicht.php");
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final JSONArray jArray_ein;
        try {
            jArray_ein = new JSONArray(result);
            final int laenge = jArray_ein.length();

            final JSONObject jdata_einnahmen;
            final JSONObject jdata_ausgaben;

            jdata_einnahmen = jArray_ein.getJSONObject(0);
            jdata_ausgaben = jArray_ein.getJSONObject(1);

            String gesamt_Einnahmen = jdata_einnahmen.getString("betrag");
            String gesamt_Ausgaben = jdata_ausgaben.getString("betrag");

            int einnahmen = Integer.parseInt(gesamt_Einnahmen);
            int ausgaben = Integer.parseInt(gesamt_Ausgaben);
            int diff = einnahmen - ausgaben;

            setBilanz(diff);
            setJahreseinnahmen(einnahmen);
            setJahresausgaben(ausgaben);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getJahreseinnahmen() {
        return jahreseinnahmen;
    }

    public void setJahreseinnahmen(int jahreseinnahmen) {
        this.jahreseinnahmen = 0;
        this.jahreseinnahmen = jahreseinnahmen;
    }

    public int getJahresausgaben() {
        return jahresausgaben;
    }

    public void setJahresausgaben(int jahresausgaben) {
        this.jahresausgaben = 0;
        this.jahresausgaben = jahresausgaben;
    }

    public int getBilanz() {
        return bilanz;
    }

    public void setBilanz(int bilanz) {
        this.bilanz = 0;
        this.bilanz = bilanz;
    }

    public void write_einnhame(Eingang e){

        Log.i("Konto","Write einnahme");

        final String name = e.getName();
        final String betrag = Integer.toString(e.getBetrag());
        final String anlass = e.getAnlass();
        final String typ = e.getTyp();

        /*Log.i("Konto","Name: " + name);
        Log.i("Konto","Betrag: " + betrag);
        Log.i("Konto","Anlass: " + anlass);
        Log.i("Konto","Typ: " + typ);*/

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"insert_einnahme.php");
            tmp.put("name",name);
            tmp.put("betrag",betrag);
            tmp.put("anlass",anlass);
            tmp.put("typ",typ);
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }

    }

    public void write_ausgabe(Rechnung r){

        Log.i("Konto","Write ausgabe");
        final String name = r.getName();
        final String betrag = Integer.toString(r.getBetrag());
        final String anlass = r.getAnlass();
        final String typ = r.getTyp();

        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"insert_ausgabe.php");
            tmp.put("name",name);
            tmp.put("betrag",betrag);
            tmp.put("anlass",anlass);
            tmp.put("typ",typ);
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    public String getLinkip(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        String local_linkip = sharedPreferences.getString("url","");


        if(local_linkip != ""){
            linkip = local_linkip;
            //Toast toast = Toast.makeText(getApplicationContext(),"Found id: " + device_id,Toast.LENGTH_SHORT);
            //toast.show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String url = "http://[Your-IPv6-Adress-goes-here]/mietverwaltung/";
            editor.putString("url",url);
            editor.commit();
            linkip = url;
            //Toast toast = Toast.makeText(getApplicationContext(),"Set id to " + new_id,Toast.LENGTH_SHORT);
            //toast.show();
        }

        return linkip;
    }

    public void setLinkip(Context context, String linkip) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String url = linkip;
        editor.putString("url",url);
        editor.commit();
        linkip = url;

        this.linkip = linkip;
    }

    public Boolean getNotificationState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        Boolean notify_enable = sharedPreferences.getBoolean("notify", true);
        Boolean local_notify = true;


        if (notify_enable == true) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Boolean noti = true;
            editor.putBoolean("notify", noti);
            editor.commit();
            local_notify = true;
            //Toast toast = Toast.makeText(getApplicationContext(),"Found id: " + device_id,Toast.LENGTH_SHORT);
            //toast.show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Boolean noti = false;
            editor.putBoolean("notify", noti);
            editor.commit();
            local_notify = false;
            //Toast toast = Toast.makeText(getApplicationContext(),"Set id to " + new_id,Toast.LENGTH_SHORT);
            //toast.show();
        }

        return local_notify;
    }

    public void setNotificationState(Context context, Boolean state){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        Boolean notify_enable = sharedPreferences.getBoolean("notify", true);
        Boolean local_notify = true;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean noti = state;
        editor.putBoolean("notify", noti);
        editor.commit();

    }

    public void setBackupNotificationState(Context context, Boolean state){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        Boolean notify_enable = sharedPreferences.getBoolean("backupnotify", true);
        Boolean local_notify = true;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean noti = state;
        editor.putBoolean("backupnotify", noti);
        editor.commit();

    }

    public Boolean getBackupNotificationState(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.url_pref), context.MODE_PRIVATE);
        Boolean notify_enable = sharedPreferences.getBoolean("backupnotify", true);
        Boolean local_notify = true;


        if (notify_enable == true) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Boolean noti = true;
            editor.putBoolean("backupnotify", noti);
            editor.commit();
            local_notify = true;
            //Toast toast = Toast.makeText(getApplicationContext(),"Found id: " + device_id,Toast.LENGTH_SHORT);
            //toast.show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Boolean noti = false;
            editor.putBoolean("backupnotify", noti);
            editor.commit();
            local_notify = false;
            //Toast toast = Toast.makeText(getApplicationContext(),"Set id to " + new_id,Toast.LENGTH_SHORT);
            //toast.show();
        }

        return local_notify;
    }
}

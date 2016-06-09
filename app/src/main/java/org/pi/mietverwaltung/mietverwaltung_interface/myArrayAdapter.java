package org.pi.mietverwaltung.mietverwaltung_interface;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Created by bro on 04.05.2016.
 */


public class myArrayAdapter<t> extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    private ArrayList<t> liste;

    public myArrayAdapter(Context context, int layoutResourceId, ArrayList<t> liste) {
        super(context, layoutResourceId, liste);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.liste = liste;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        t i = liste.get(position);

        Object v;
        String name;
        String betrag;
        String datum;
        String typ;
        String anlass;

        final Buchung b_tmp;

        if (i instanceof Buchung) {
            b_tmp = (Buchung) i;
            name = b_tmp.getName();
            betrag = Integer.toString(b_tmp.getBetrag());
            datum = b_tmp.getDatum();
            anlass = b_tmp.getAnlass();
            if(b_tmp.getTyp() == 'e'){
                typ = "+";
            } else if(b_tmp.getTyp() == 'a'){
                typ = "-";
            } else {
                typ = "u";
            }
        } else {
            name = "error";
            typ = "error";
            betrag = "error";
            anlass = "error";
        }

        if (i != null) {

            final TextView tv_name = (TextView) row.findViewById(R.id.name_view);
            tv_name.setText(name);

            TextView tv_typ = (TextView) row.findViewById(R.id.typ_view);
            tv_typ.setText(typ);

            final TextView tv_betrag = (TextView) row.findViewById(R.id.betrag_view);
            tv_betrag.setText(betrag+ " Euro");

            final TextView tv_anlass = (TextView) row.findViewById(R.id.tv_anlass_listitem);
            tv_anlass.setText(anlass);
        }

        return row;
    }

}
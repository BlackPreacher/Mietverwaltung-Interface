package org.pi.mietverwaltung.mietverwaltung_interface;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hellhero on 01.06.2016.
 */
public class PresetListArrayAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    private ArrayList<Preset_Item> liste;

    public PresetListArrayAdapter(Context context, int layoutResourceId, ArrayList<Preset_Item> liste) {
        super(context, layoutResourceId, liste);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.liste = liste;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView tv_name = (TextView) row.findViewById(R.id.tv_empfaenger_preset_list_item);
        TextView tv_betrag = (TextView) row.findViewById(R.id.tv_betrag_preset_list_item);
        TextView tv_anlass = (TextView) row.findViewById(R.id.tv_anlass_preset_list_item);

        Preset_Item actuel_item = liste.get(position);

        String name = actuel_item.getName();
        String betrag = Integer.toString(actuel_item.getBetrag());
        String anlass = actuel_item.getAnlass();

        tv_name.setText(name);
        tv_betrag.setText(betrag+" Euro");
        tv_anlass.setText(anlass);

        return row;
    }
}

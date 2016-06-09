package org.pi.mietverwaltung.mietverwaltung_interface;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class booking_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        TextView tv_id_value = (TextView)findViewById(R.id.booking_details_id_value);
        EditText ed_name_value = (EditText)findViewById(R.id.booking_detail_name_value);
        EditText ed_betrag_value = (EditText)findViewById(R.id.booking_detail_betrag_value);
        EditText ed_anlass_value = (EditText)findViewById(R.id.booking_detail_anlass_value);
        EditText ed_typ_value = (EditText)findViewById(R.id.booking_detail_typ_value);
        EditText ed_datum_value = (EditText)findViewById(R.id.booking_detail_datum_value);

        Verwaltung verwalter = (Verwaltung)getApplicationContext();


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String betrag = intent.getStringExtra("betrag");
        String anlass = intent.getStringExtra("anlass");
        String typ = intent.getStringExtra("typ");
        String datum = intent.getStringExtra("datum");
        //int id = intent.getStringExtra("id",-1);
        String strid = intent.getStringExtra("id");
        if(strid.equals("-1")){
            tv_id_value.setText("An Error Occured");
        } else {
            tv_id_value.setText(strid);
            ed_anlass_value.setText(anlass);
            ed_betrag_value.setText(betrag);
            ed_name_value.setText(name);
            ed_datum_value.setText(datum);
            ed_typ_value.setText(typ);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ((Integer.parseInt(android.os.Build.VERSION.SDK) > 5)
                && (keyCode == KeyEvent.KEYCODE_BACK)
                && (event.getRepeatCount() == 0)) {
            Log.d("CDA", "onKeyDown Called");
            //Toast toast = Toast.makeText(getBaseContext(),"Zurueck gedrueck",Toast.LENGTH_SHORT);
            //toast.show();
            //finish();
            return_to_parent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void return_to_parent(){
        NavUtils.navigateUpFromSameTask(this);
    }
}

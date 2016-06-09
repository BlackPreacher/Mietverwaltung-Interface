package org.pi.mietverwaltung.mietverwaltung_interface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final Verwaltung verwaltung = (Verwaltung)getApplicationContext();

        EditText ed_link_prefix = (EditText)findViewById(R.id.settings_prefix_link_value);
        CheckBox cb_notification = (CheckBox)findViewById(R.id.settings_notification_checkBox);
        CheckBox chk_backup_notification = (CheckBox)findViewById(R.id.chk_backup_notification);

        ed_link_prefix.setText(verwaltung.getLinkPrefix(getApplicationContext()));
        cb_notification.setChecked(verwaltung.getNotificationState(getApplicationContext()));
        chk_backup_notification.setChecked(verwaltung.getBackupNotificationState(getApplicationContext()));


        cb_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb_notification = (CheckBox)v.findViewById(R.id.settings_notification_checkBox);

                verwaltung.setNotificationState(getApplicationContext(),cb_notification.isChecked());

            }
        });

        chk_backup_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox chk_backup_notification = (CheckBox)v.findViewById(R.id.chk_backup_notification);
                verwaltung.setBackupNotificationState(getApplicationContext(),chk_backup_notification.isChecked());
            }
        });


    }

    public void save_settings(View v){
        Verwaltung verwaltung = (Verwaltung)getApplicationContext();

        EditText ed_link_prefix = (EditText)findViewById(R.id.settings_prefix_link_value);
        String link_prefix = ed_link_prefix.getText().toString();

        verwaltung.setLinkPrefix(getApplicationContext(), link_prefix);

        Toast toast = Toast.makeText(getApplicationContext(),"Einstellungen wurden geändert!", Toast.LENGTH_SHORT);
        toast.show();
    }
}

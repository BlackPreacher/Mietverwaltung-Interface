package org.pi.mietverwaltung.mietverwaltung_interface;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by bro on 29.05.2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

        Log.i(TAG,"Send Token to server");

        SharedPreferences pref = getSharedPreferences(getString(R.string.url_pref), 0);
        String linkip = getString(R.string.linkprefix);
        String id = "";

        final String device_id_pref = pref.getString("id","");


        if(device_id_pref != ""){
            id = device_id_pref;
            Log.i("device id","device: " + id);
        } else {
            SharedPreferences.Editor editor = pref.edit();
            Random r = new Random();
            String new_id = Integer.toString(r.nextInt());
            editor.putString("id",new_id);
            editor.commit();
            id = new_id;
            Log.i("device id","device: " + id);
        }
        String result = "";
        try {
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("url",linkip+"insert_gcm_token.php");
            tmp.put("token",token);
            tmp.put("device",id);
            result = new NetworkOperations().execute(tmp).get();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
    }

}

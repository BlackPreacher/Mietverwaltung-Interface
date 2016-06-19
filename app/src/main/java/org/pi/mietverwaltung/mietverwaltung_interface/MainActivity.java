package org.pi.mietverwaltung.mietverwaltung_interface;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    String linkip = "";

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    //private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);

        SharedPreferences pref = getSharedPreferences(getString(R.string.url_pref), 0);
        linkip = getString(R.string.linkprefix);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Main");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,params);

        mProgressDialog.show();


        Verwaltung verwalter = (Verwaltung)getApplicationContext();
        verwalter.setToken_retrieved(true);
        String tmp = verwalter.getLinkPrefix(getApplicationContext());
        //verwalter.load();
        //verwalter.get_Jahresbilanz_from_Database();

        mProgressDialog.hide();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(MainActivity.this, "Du hast dich eingeloggt als " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Verwaltung verwalter = (Verwaltung)getApplicationContext();
                    verwalter.setLogged_in(true);
                } else {
                    // User is signed out
                    Log.i(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };


        Log.i(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

        //SharedPreferences pref = getSharedPreferences(getString(R.string.url_pref), 0);
        final String device_id_pref = pref.getString("id","");

        String id = "";

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

        FirebaseMessaging.getInstance().subscribeToTopic("backup");
        Log.i(TAG, "Subscribed to Backup topic");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if(mViewPager!=null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if(tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Aktuellen Report senden? ", Snackbar.LENGTH_LONG)
                        .setAction("Senden", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send_report(v);
                            }
                        }).show();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 3){
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 3){
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void send_report(View v){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);

        String result = "";
        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("url",linkip+"generate_miet_report.php");
        mProgressDialog.show();
        new NetworkOperations().execute(tmp)
        mProgressDialog.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh_all) {
            Verwaltung verwalter = (Verwaltung)getApplicationContext();
            verwalter.refresh_all();
        } else if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(),Settings.class);
            startActivity(intent);
        } else if (id == R.id.login) {
            showLoginDialog();
        } else if (id == R.id.googlelogin) {
            showGoogleLogin();
        } else if (id == R.id.google_logout) {
            showGoogleLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authenfizierung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.i(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void showGoogleLogout(){
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //updateUI(null);
                        Toast.makeText(MainActivity.this, "Du wurdest ausgelogt" ,Toast.LENGTH_SHORT).show();
                        Verwaltung verwaltung = (Verwaltung)getApplicationContext();
                        verwaltung.setLogged_in(false);
                    }
                });
    }

    public void showGoogleLogin(){
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();*/

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity *//*, this /* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        mAuth = FirebaseAuth.getInstance();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    public void showLoginDialog(){
        Verwaltung verwaltung = (Verwaltung)getApplicationContext();
        if(!verwaltung.getLogged_in()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Login");
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.login_dialog, null);

            final EditText ed_e_mail = (EditText) dialogView.findViewById(R.id.ed_e_mail_login_dialog);
            final EditText ed_pw = (EditText) dialogView.findViewById(R.id.ed_password_login_dialog);


            dialog.setView(dialogView);
            dialog.setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!ed_e_mail.getText().toString().equals("") && !ed_pw.getText().toString().equals("")) {
                        String email = ed_e_mail.getText().toString();
                        String password = ed_pw.getText().toString();
                        login(email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, nicht alle Felder waren gefüllt", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setNegativeButton("Abbrechen", null);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            mAuth.signOut();
            verwaltung.setLogged_in(false);
            Toast.makeText(MainActivity.this, "Ausgeloggt", Toast.LENGTH_SHORT).show();
        }


    }

    public void login(String email, String password){
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getApplicationContext(), "Anmeldung fehlgeschlagen",
                                    Toast.LENGTH_SHORT).show();
                            Verwaltung verwalter = (Verwaltung)getApplicationContext();
                            verwalter.setLogged_in(false);
                        } else {
                            Verwaltung verwalter = (Verwaltung)getApplicationContext();
                            verwalter.setLogged_in(true);
                            Toast.makeText(getApplicationContext(), "Anmeldung erfolgreich",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        mProgressDialog.hide();
                        // [END_EXCLUDE]
                    }
                });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ProgressDialog mProgressDialog;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
        {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "SMS");
        }

        @Override
        public boolean onContextItemSelected(MenuItem item){
            if(item.getTitle()=="Call"){
                Toast.makeText(getContext(),"calling code",Toast.LENGTH_LONG).show();
            }
            else if(item.getTitle()=="SMS"){
                Toast.makeText(getContext(),"sending sms code",Toast.LENGTH_LONG).show();
            }else{
                return false;
            }
            return true;
        }


    public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);

            mProgressDialog.show();

            final Verwaltung verwaltung = (Verwaltung)getActivity().getApplicationContext();
            String tmp = verwaltung.getLinkPrefix(getContext());
            verwaltung.refresh_all();
            verwaltung.load();
            verwaltung.get_Jahresbilanz_from_Database();

            mProgressDialog.hide();

            Log.i(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());

            if(verwaltung.getBackupNotificationState(getContext())) {
                FirebaseMessaging.getInstance().subscribeToTopic("backup");
                Log.i(TAG, "Subscribed to Backup topic");
            }

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                final View rootView = inflater.inflate(R.layout.fragment_aktuelle_monat, container, false);

                Verwaltung verwalter = (Verwaltung)getActivity().getApplicationContext();
                Boolean token_retrieved = verwalter.getToken_retrieved();

                TextView StatusTextView = (TextView) rootView.findViewById(R.id.status_view);

                if(token_retrieved){
                    StatusTextView.setText(getString(R.string.token_ok));
                } else {
                    StatusTextView.setText(getString(R.string.token_error));
                    Toast toast = Toast.makeText(getContext(),getString(R.string.token_error_message),Toast.LENGTH_LONG);
                    toast.show();
                }

                ListView list = (ListView)rootView.findViewById(R.id.gesamt_list);
                mProgressDialog.show();
                String result = get_aktueller_monat(rootView);
                mProgressDialog.hide();

                final SwipeRefreshLayout swipeRefreshLayout;
                swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mProgressDialog.show();
                        get_aktueller_monat(rootView);
                        swipeRefreshLayout.setRefreshing(false);
                        mProgressDialog.hide();
                    }
                });

                return rootView;

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                final View rootView = inflater.inflate(R.layout.fragment_miet_uebersicht, container, false);

                set_uebersicht(rootView);

                final SwipeRefreshLayout swipeRefreshLayout;
                swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_ue);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mProgressDialog.show();
                        set_uebersicht(rootView);
                        swipeRefreshLayout.setRefreshing(false);
                        mProgressDialog.hide();
                    }
                });

                return rootView;


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {

                final View rootView = inflater.inflate(R.layout.fragment_preset, container, false);

                final ListView lst_preset = (ListView) rootView.findViewById(R.id.preste_list_view);
                ArrayList<Preset_Item> presets = new ArrayList<>();
                presets.add( new Preset_Item("ESWE",81,"Strom","s"));
                presets.add( new Preset_Item("Unitymedia",23,"Internet","i"));
                presets.add( new Preset_Item("Deutschland",18,"GEZ","g"));

                PresetListArrayAdapter arrayAdapter = new PresetListArrayAdapter(getContext(),R.layout.preset_list_item,presets);
                lst_preset.setAdapter(arrayAdapter);

                lst_preset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final Object o = lst_preset.getItemAtPosition(position);
                        Preset_Item selecteItem = (Preset_Item)o;

                        final String name = selecteItem.getName();
                        final String betrag = Integer.toString(selecteItem.getBetrag());
                        final String anlass = selecteItem.getAnlass();
                        final String typ = selecteItem.getTyp();

                        if(verwaltung.getLogged_in()) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                            alertDialogBuilder.setTitle("Eintragen");
                            alertDialogBuilder.setMessage("Möchtest du den Eintrag wirklich vornehmen?");
                            alertDialogBuilder.setPositiveButton("Eintragen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendToAusgabe(name, betrag, anlass, typ);
                                }
                            });
                            alertDialogBuilder.setNegativeButton("Abbrechen", null);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.create().show();
                            lst_preset.setSelected(false);
                        } else {
                            Toast.makeText(getContext(),"Sorry, du musst dich einloggen!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                return  rootView;

            } else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4){

                final View rootView = inflater.inflate(R.layout.fragment_ausgaben, container, false);

                Button B_eintrag = (Button) rootView.findViewById(R.id.eintragen_button);

                B_eintrag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText E_name = (EditText) rootView.findViewById(R.id.name_edit);
                        EditText E_betrag = (EditText) rootView.findViewById(R.id.betrag_edit);
                        EditText E_anlass = (EditText) rootView.findViewById(R.id.anlass_edit);
                        EditText E_typ = (EditText) rootView.findViewById(R.id.typ_edit);
                        RadioButton R_einnahme = (RadioButton)rootView.findViewById(R.id.radio_ein);
                        RadioButton R_ausgabe = (RadioButton)rootView.findViewById(R.id.radio_aus);

                        String name = E_name.getText().toString();
                        String betrag = E_betrag.getText().toString();
                        String anlass = E_anlass.getText().toString();
                        String typ = E_typ.getText().toString();
                        if(verwaltung.getLogged_in()) {
                            if (name.equals("") || betrag.equals("") || anlass.equals("") || typ.equals("")) {
                                Toast toast = Toast.makeText(getContext(), "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                if (R_einnahme.isChecked()) {
                                    mProgressDialog.show();
                                    sendToEinnahme(name, betrag, anlass, typ);
                                    mProgressDialog.hide();
                                    Toast.makeText(getContext(), "Wurde als Einnahme eingetragen!", Toast.LENGTH_SHORT).show();
                                } else if (R_ausgabe.isChecked()) {
                                    mProgressDialog.show();
                                    sendToAusgabe(name, betrag, anlass, typ);
                                    mProgressDialog.hide();
                                    Toast.makeText(getContext(), "Wurde als Ausgabe eingetragen!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Du musst auswähle, ob es eine Eingabe oder eine Ausgabe ist!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Du musst dich einloggen!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return rootView;


            } else {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }


        }

        public void sendToEinnahme(final String name, final String betrag, final String anlass, final String typ) {

            Verwaltung verwalter = (Verwaltung)getActivity().getApplicationContext();
            verwalter.write_einnahme(name,betrag,anlass,typ);

        }

        public void sendToAusgabe(final String name, final String betrag, final String anlass, final String typ) {

            Verwaltung verwalter = (Verwaltung)getActivity().getApplicationContext();
            verwalter.write_ausgabe(name,betrag,anlass,typ);
        }

        public String get_aktueller_monat(final View view){

            String end_result;

            Verwaltung verwalter = (Verwaltung)getActivity().getApplicationContext();
            verwalter.load();

            if(verwalter.get_server_avialble()) {
                TextView txt_view_error = (TextView)view.findViewById(R.id.akt_monat_err_view);
                txt_view_error.setVisibility(View.GONE);

                final ListView list = (ListView) view.findViewById(R.id.gesamt_list);
                list.setVisibility(View.VISIBLE);

                int menge_eingaben = verwalter.getEingabenFromKonto().size();
                int menge_ausgaben = verwalter.getAusgabenFromKonto().size();

                ArrayList<Eingang> alle_eingaben = verwalter.getEingabenFromKonto();
                ArrayList<Rechnung> alle_rechungen = verwalter.getAusgabenFromKonto();

                Log.i("Getmonate", "eingaben" + menge_eingaben);
                Log.i("getmonate", "ausgaben" + menge_ausgaben);

                ArrayList<Buchung> alle_Buchungen = new ArrayList<>();


                for (int i = 0; i < menge_eingaben; i++) {
                    String value = "";
                    Eingang aktuelle_auswahl = alle_eingaben.get(i);
                    alle_Buchungen.add(new Buchung(aktuelle_auswahl));
                }

                for (int i = 0; i < menge_ausgaben; i++) {
                    String value = "";
                    Rechnung aktuelle_auswahl = alle_rechungen.get(i);
                    alle_Buchungen.add(new Buchung(aktuelle_auswahl));
                }

                myArrayAdapter<Buchung> arrayAdapter = new myArrayAdapter<Buchung>(
                        getContext(),
                        R.layout.listitem,
                        alle_Buchungen
                );

                list.setAdapter(arrayAdapter);

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, final long id) {

                        final Object o = list.getItemAtPosition(position);


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Löschen");
                        alertDialogBuilder.setMessage("Möchtest du diesen eintrag wirklich löschen?").setCancelable(false).setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int id_to_delete;
                                char typ_of_item;
                                if (o instanceof Buchung) {
                                    Buchung buchung = (Buchung) o;
                                    id_to_delete = buchung.getId();
                                    typ_of_item = buchung.getTyp();
                                } else {
                                    id_to_delete = 0;
                                    typ_of_item = 'u';
                                }
                                deleteById(id_to_delete, typ_of_item);

                                Toast toast = Toast.makeText(getContext(), "Choosen " + id_to_delete + " from " + typ_of_item, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        return true;
                    }
                });

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final Object o = list.getItemAtPosition(position);

                        Buchung buchung = (Buchung) o;

                        String name = buchung.getName();
                        String betrag = Integer.toString(buchung.getBetrag());
                        String anlass = buchung.getAnlass();
                        String typ = Character.toString(buchung.getTyp());
                        String datum = buchung.getDatum();
                        String buchungs_id = Integer.toString(buchung.getId());

                        Intent intent = new Intent(getContext(), booking_details.class);
                        intent.putExtra("name", name);
                        intent.putExtra("betrag", betrag);
                        intent.putExtra("anlass", anlass);
                        intent.putExtra("typ", typ);
                        intent.putExtra("datum", datum);
                        intent.putExtra("id", buchungs_id);

                        startActivity(intent);


                        //return true;

                    }
                });

                 end_result = "Ok";
            } else {
                end_result = "Error";
                TextView txt_view_error = (TextView)view.findViewById(R.id.akt_monat_err_view);
                txt_view_error.setText(getString(R.string.server_not_available));
                txt_view_error.setVisibility(View.VISIBLE);
                ListView list = (ListView) view.findViewById(R.id.gesamt_list);
                list.setVisibility(View.GONE);
            }

            return end_result;

        }

        public void deleteById(final int id, final char typ){

            Verwaltung verwalter = (Verwaltung)getActivity().getApplicationContext();
            verwalter.delete_from_konto(id,typ);

        }

        public void set_uebersicht(final View v){

            Verwaltung verwalter =(Verwaltung)getActivity().getApplicationContext();
            verwalter.refresh_uebersicht();
            if(verwalter.get_server_avialble()) {

                TextView txt_view_error = (TextView)v.findViewById(R.id.uebersicht_err_view);
                txt_view_error.setVisibility(View.GONE);

                int gesamt_einnahmen = verwalter.get_Jahres_einnahmen();
                int gesamt_ausgaben = verwalter.get_Jahres_ausgaben();
                int gesamt_bilanz = verwalter.get_Jahres_bilanz();

                TextView tv_einnahmen = (TextView) v.findViewById(R.id.einnahmen_value_view);
                TextView tv_ausgaben = (TextView) v.findViewById(R.id.ausgaben_value_view);
                TextView tv_bilanz = (TextView) v.findViewById(R.id.diff_value_view);

                tv_einnahmen.setText(Integer.toString(gesamt_einnahmen));
                tv_ausgaben.setText(Integer.toString(gesamt_ausgaben));
                tv_bilanz.setText(Integer.toString(gesamt_bilanz));

            } else {
                TextView txt_view_error = (TextView)v.findViewById(R.id.uebersicht_err_view);
                txt_view_error.setText(getString(R.string.server_not_available));
                txt_view_error.setVisibility(View.VISIBLE);
            }


        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Aktueller Monat";
                case 1:
                    return "Übersicht";
                case 2:
                    return "Vorgabe";
                case 3:
                    return "Manuell";
            }
            return null;
        }




    }
}

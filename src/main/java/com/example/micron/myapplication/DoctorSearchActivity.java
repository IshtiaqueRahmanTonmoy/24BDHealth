package com.example.micron.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.micon.adapter.DoctorAdapter;
import com.example.com.micon.adapter.HospitalLookAdapter;
import com.example.micon.entity.JSONParser;
import com.example.micon.entity.ListModel;
import com.example.micon.entity.ListModelDoctor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DoctorSearchActivity extends AppCompatActivity  {

    Spinner distsearch, areasearch, expertnessearch;
    ListView listview;
    private static String url_all_docsearch = "http://24bdhealth.com/bdhealth24/doctorsearch.php";
    private static String url_all_expertise = "http://24bdhealth.com/bdhealth24/expertise.php";
    private static String url_all_area = "http://24bdhealth.com/bdhealth24/areasearch.php";
    private static String url_all_doctor = "http://24bdhealth.com/bdhealth24/doctorappoint.php";
    private static final String TAG_NAME = "name";
    private static final String TAG_ENAME = "name";
    private static final String TAG_CID = "cid";
    private static final String THANA_NAME = "name";
    private static final String TAG_DOCNAME = "Name";
    private static final String DOC_EXPERTISE = "Expertise";
    private static final String HOSPITAL_NAME = "HospitalName";
    private static final String HOSPITAL_ADDRESS = "Address";
    private static final String CHAMBER_DAY = "Chamberday";
    private static final String CHAMBER_TIME = "Chambertime";
    int x;
    String getdist;
    String getarea;
    JSONObject docname, medall, exp;
    private static final String TAG_SUCCESS = "success";
    private ArrayList<String> distlist, exlist, arealist;
    HashMap<String, String> hmap;
    ArrayList<ListModelDoctor> alist;
    String cid, dname, docexpert, address, ctime, cday, name, hname;
    HashMap<String, String> map;
    ListModelDoctor listdoc;
    DoctorAdapter dadapter = null;
    EditText text;
    String dists,arst,ext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        distsearch = (Spinner) findViewById(R.id.District);
        areasearch = (Spinner) findViewById(R.id.Area);
        expertnessearch = (Spinner) findViewById(R.id.Expertness);

        listview = (ListView) findViewById(R.id.listview);

        alist = new ArrayList<ListModelDoctor>();
        distlist = new ArrayList<String>();
        exlist = new ArrayList<String>();
        arealist = new ArrayList<String>();

        hmap = new HashMap<String, String>();
        map = new HashMap<String, String>();

        //listdoc = new ListModelDoctor();

        new GetDoctorList().execute();
        new GetCategories().execute();
        new GetExpertise().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private class GetCategories extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONParser jsonParser = new JSONParser();

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(url_all_docsearch, "GET", param);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray district = json.getJSONArray("district");

                        for (int i = 0; i < district.length(); i++) {
                            JSONObject catObj1 = (JSONObject) district.get(i);
                            String name = catObj1.getString(TAG_NAME);
                            distlist.add(name);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateSpinner();
        }


        private void populateSpinner() {
            List<String> lables = new ArrayList<String>();

            for (int i = 0; i < distlist.size(); i++) {
                lables.add(distlist.get(i));
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(DoctorSearchActivity.this, android.R.layout.simple_spinner_item, lables);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            distsearch.setAdapter(spinnerAdapter);

            distsearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cid = String.valueOf(position + 1);

                    dists = distsearch.getSelectedItem().toString().toLowerCase(Locale.getDefault());
                    // Toast.makeText(getApplicationContext(),""+dists,Toast.LENGTH_LONG).show();

                    dadapter.filter1(dists);
                    new GetArea().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private class GetArea extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                arealist = new ArrayList<String>();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONParser jP = new JSONParser();
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair(TAG_CID, cid));
                        JSONObject json = jP.makeHttpRequest(url_all_area, "GET", param);

                        Log.e("Response: ", "> " + json);

                        JSONArray than = null;
                        try {
                            than = json.getJSONArray("th");
                            for (int x = 0; x < than.length(); x++) {
                                JSONObject catObj11 = than.getJSONObject(x);
                                name = catObj11.getString(THANA_NAME);
                                arealist.add(name);
                            }

                            //Log.e("Thana: ", "> " + than);
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(DoctorSearchActivity.this, android.R.layout.simple_list_item_1, arealist);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            areasearch.setAdapter(spinnerAdapter);


                            areasearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    arst = areasearch.getSelectedItem().toString().toLowerCase(Locale.getDefault());
                                    dadapter.filter(arst);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return null;
            }
        }
    }

    private class GetExpertise extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONParser jsonParser = new JSONParser();

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(url_all_expertise, "GET", param);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {

                    if (json != null) {
                        JSONArray category_doc = json.getJSONArray("category_doc");

                        for (int i = 0; i < category_doc.length(); i++) {
                            JSONObject catObj1 = (JSONObject) category_doc.get(i);
                            String name = catObj1.getString(TAG_ENAME);
                            exlist.add(name);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateSpinner1();
        }

        private void populateSpinner1() {
            List<String> lables1 = new ArrayList<String>();

            for (int i = 0; i < exlist.size(); i++) {
                lables1.add(exlist.get(i));
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(DoctorSearchActivity.this, android.R.layout.simple_spinner_item, lables1);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            expertnessearch.setAdapter(spinnerAdapter);
            expertnessearch.setSelection(15);


            expertnessearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ext = expertnessearch.getSelectedItem().toString().toLowerCase(Locale.getDefault());
                    dadapter.filter3(ext);
                }
                @Override

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private class GetDoctorList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        JSONParser jP = new JSONParser();

                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        JSONObject json = jP.makeHttpRequest(url_all_doctor, "GET", param);
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {

                            for (int x = 0; x <= 366; x++) {
                                // JSONArray jsonArray = new JSONArray(json);
                                JSONObject jsonArray = json.getJSONObject(""+x);

                                dname = jsonArray.getString(TAG_DOCNAME);
                                hname = jsonArray.getString(HOSPITAL_NAME);
                                address = jsonArray.getString(HOSPITAL_ADDRESS);
                                docexpert = jsonArray.getString(DOC_EXPERTISE);
                                cday = jsonArray.getString(CHAMBER_DAY);
                                ctime = jsonArray.getString(CHAMBER_TIME);

                                listdoc = new ListModelDoctor(dname,hname,address,docexpert,cday,ctime);
                                alist.add(listdoc);
                                dadapter = new DoctorAdapter(DoctorSearchActivity.this, alist);

                                Bundle bundle = getIntent().getExtras();
                                if (bundle != null) {
                                    getdist = bundle.getString("dist");
                                    getarea = bundle.getString("area");
                                    dadapter.filterHos(getdist,getarea);

                                    distsearch.setEnabled(false);
                                    areasearch.setEnabled(false);
                                    expertnessearch.setEnabled(false);
                                }
                                   listview.setAdapter(dadapter);
                            }
                        }

                        else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
    }
}
package com.example.micron.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;

public class HospitalSearchActivity extends AppCompatActivity {

    Spinner distsearch, areasearch;
    ListView listview;
    Button listsearch;
    private static String url_all_docsearch = "http://24bdhealth.com/bdhealth24/doctorsearch.php";
    private static String url_all_area = "http://24bdhealth.com/bdhealth24/areasearch.php";
    private static String url_all_hospital = "http://24bdhealth.com/bdhealth24/hospitalsearch.php";
    private static final String TAG_NAME = "name";
    private static final String THANA_NAME = "name";
    private static final String HOS_NAME = "name";
    private static final String HOS_ADDRESS = "address";
    private ArrayList<String> distlist, arealist;
    private static final String TAG_CID = "cid";
    ArrayList<ListModel> alist;
    private ProgressDialog pDialog;
    String cid, thananame, hospitalname, hospitaladdress, name,hname,address;
    HospitalLookAdapter hadapter = null;
    HashMap<String, String> map;
    ListModel l;
    int imgid=R.drawable.icon2;
    private static final String TAG_SUCCESS = "success";
    EditText text;
    DoctorAdapter docadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        distsearch = (Spinner) findViewById(R.id.District);
        areasearch = (Spinner) findViewById(R.id.Area);
        listview = (ListView) findViewById(R.id.listview);
        alist = new ArrayList<ListModel>();
        map = new HashMap<String, String>();

         distlist = new ArrayList<String>();
         new GetHospitals().execute();
         new GetDistricts().execute();

    }

    private class GetDistricts extends AsyncTask<Void, Void, Void> {

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

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(HospitalSearchActivity.this, android.R.layout.simple_spinner_item, lables);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            distsearch.setAdapter(spinnerAdapter);
            distsearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    cid = String.valueOf(position + 1);

                    String dt = distsearch.getSelectedItem().toString().toLowerCase(Locale.getDefault());
                    hadapter.filter(dt);
                    new GetArea().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
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
                                 ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(HospitalSearchActivity.this,android.R.layout.simple_list_item_1,arealist);
                                 spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                 areasearch.setAdapter(spinnerAdapter);
                                 areasearch.setSelection(1);

                                 areasearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                     @Override
                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                         String t = areasearch.getSelectedItem().toString().toLowerCase(Locale.getDefault());
                                         hadapter.filter(t);
                                         //Toast.makeText(getApplicationContext(),""+areasearch.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
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

    private class GetHospitals extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        JSONParser jP = new JSONParser();

                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        JSONObject json = jP.makeHttpRequest(url_all_hospital, "GET", param);
                        // Checking for SUCCESS TAG
                        int success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {

                            for (int x = 0; x <= 472; x++) {

                                JSONObject jsonArray = json.getJSONObject(""+x);

                                hname = jsonArray.getString(HOS_NAME);
                                address = jsonArray.getString(HOS_ADDRESS);

                                l = new ListModel(hname,address,imgid);
                                alist.add(l);
                                hadapter = new  HospitalLookAdapter(HospitalSearchActivity.this, alist);
                                listview.setAdapter(hadapter);

                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView c = (TextView) view.findViewById(R.id.area);
                                        String dist = c.getText().toString();

                                        TextView ca = (TextView) view.findViewById(R.id.addressphn);
                                        String area = ca.getText().toString();

                                        //docadapter.filter(dist,area);
                                        Intent intent = new Intent(HospitalSearchActivity.this,DoctorSearchActivity.class);
                                        intent.putExtra("dist", dist);
                                        intent.putExtra("area", area);
                                        startActivity(intent);
                                    }
                                });
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









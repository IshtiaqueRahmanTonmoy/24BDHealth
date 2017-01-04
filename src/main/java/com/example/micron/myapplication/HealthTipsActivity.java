package com.example.micron.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micon.entity.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

public class HealthTipsActivity extends ListActivity {
    Intent intent;
    ListView listviews;
    View rootView;
    TextView text;
    private ProgressDialog pDialog;
    JSONParser jParser;
    ArrayList<HashMap<String, String>> healthadviceList;
    private static String url_all_htips = "http://24bdhealth.com/bdhealth24/advice.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HEALTHTIPS = "htips";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADING = "heading";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_CLICK = "click here for details";
    JSONArray htips = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        listviews = (ListView) findViewById(R.id.listview);

        jParser = new JSONParser();
        healthadviceList = new ArrayList<HashMap<String, String>>();
        new LoadList().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String val = ((TextView) v.findViewById(R.id.pid)).getText()
                .toString();
        //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
        intent = new Intent(HealthTipsActivity.this,ShowHealthList.class);
        //intent.setClass(getActivity(), ShowHealthList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(TAG_ID,val);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_health_tips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadList extends AsyncTask<String , String ,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(HealthTipsActivity.this);
            pDialog.setMessage("Loading Doctors tips. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> param = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_htips, "GET", param);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    htips = json.getJSONArray(TAG_HEALTHTIPS);

                    // looping through All Products
                    for (int i = 0; i < htips.length(); i++) {
                        JSONObject c = htips.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String head = c.getString(TAG_HEADING);
                        //BengaliUnicodeString.getBengaliUTF(getActivity(),head,text);
                        String details = c.getString(TAG_DETAILS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value

                        map.put(TAG_ID, id);
                        map.put(TAG_HEADING, head);
                        map.put(TAG_DETAILS, details);

                        // adding HashList to ArrayList

                        healthadviceList.add(map);
                        Log.e("myApp",healthadviceList.toString());

                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    //Intent i = new Intent(getActivity(),ViewListActivity.class);
                    // Closing all previous activities
                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread


          //  HealthTipsActivity.this.runOnUiThread(new Runnable() {
              //  public void run() {

                    //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, healthadviceList);

                    ListAdapter adapter = new SimpleAdapter(
                            HealthTipsActivity.this, healthadviceList,
                            R.layout.list_item, new String[]{TAG_ID,
                            TAG_HEADING},
                            new int[]{R.id.pid, R.id.name});

                    //listviews.setAdapter(adapter);
                    // updating listview
                    setListAdapter(adapter);
                //}
           // });


        }


    }


}

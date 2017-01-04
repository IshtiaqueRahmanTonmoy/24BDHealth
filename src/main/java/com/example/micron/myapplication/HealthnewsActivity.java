package com.example.micron.myapplication;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.micon.entity.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthnewsActivity extends ListActivity {

    ListView listviews;
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> healthpoList;
    TextView text;
    Intent intent;
    private static String url_all_hpolist = "http://24bdhealth.com/bdhealth24/heading.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_HEALTHTPORTLIST = "healthport";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADING = "heading";
    private static final String TAG_DETAILS = "details";
    private static final String TAG_CLICK = "click here for details";
    JSONArray healthport = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthnews);

        listviews = (ListView) findViewById(R.id.listview);
        healthpoList = new ArrayList<HashMap<String, String>>();
        new LoadHealList().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String val = ((TextView) v.findViewById(R.id.pid)).getText()
                .toString();

        //Toast.makeText(getActivity(),"sfd"+id,Toast.LENGTH_LONG).show();
        intent = new Intent(HealthnewsActivity.this,ShowHospitalList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.setClass(getActivity(), ShowHospitalList.class);
        intent.putExtra(TAG_ID,val);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_healthnews, menu);
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

    class LoadHealList extends AsyncTask<String , String ,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HealthnewsActivity.this);
            pDialog.setMessage("Loading Health tips. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param =
                    new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_hpolist, "GET", param);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    healthport = json.getJSONArray(TAG_HEALTHTPORTLIST);

                    // looping through All Products
                    for (int i = 0; i < healthport.length(); i++) {
                        JSONObject c = healthport.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String head = c.getString(TAG_HEADING);
                        String details = c.getString(TAG_DETAILS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_HEADING, head);
                        map.put(TAG_DETAILS, details);
                        // adding HashList to ArrayList
                        healthpoList.add(map);
                    }
                } else {
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

            HealthnewsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            HealthnewsActivity.this, healthpoList,
                            R.layout.list_item, new String[]{TAG_ID,
                            TAG_HEADING},
                            new int[]{R.id.pid, R.id.name});
                    // updating listview

                    setListAdapter(adapter);
                }
            });
        }
    }
}

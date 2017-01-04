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

public class LatestnewsActivity extends ListActivity {

    Intent intent;
    ListView listviews;
    View rootView;
    TextView text;
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> newsList;
    private static String url_all_news = "http://24bdhealth.com/bdhealth24/news.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NEWS = "latest_news";
    private static final String TAG_ID = "id";
    private static final String TAG_HEADING = "heading";
    private static final String TAG_DETAILS = "details";
    JSONArray latest_news = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestnews);

        listviews = (ListView) findViewById(R.id.listview);
        newsList = new ArrayList<HashMap<String, String>>();
        new LoadList().execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String val = ((TextView) v.findViewById(R.id.pid)).getText()
                .toString();
        //Toast.makeText(getActivity(),""+id,Toast.LENGTH_LONG).show();
        intent = new Intent(LatestnewsActivity.this,ShowDetailNews.class);
        //intent.setClass(getActivity(), ShowHealthList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(TAG_ID,val);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_latestnews, menu);
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

            pDialog = new ProgressDialog(LatestnewsActivity.this);
            pDialog.setMessage("Loading Latest Headlines. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_news, "GET", param);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    latest_news = json.getJSONArray(TAG_NEWS);

                    // looping through All Products
                    for (int i = 0; i < latest_news.length(); i++) {
                        JSONObject c = latest_news.getJSONObject(i);

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
                        newsList.add(map);
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

            LatestnewsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            LatestnewsActivity.this, newsList,
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

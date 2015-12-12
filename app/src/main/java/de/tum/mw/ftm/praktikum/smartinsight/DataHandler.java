package de.tum.mw.ftm.praktikum.smartinsight;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcengelmann on 12.12.15.
 */
public class DataHandler {

    private String TAG = "DataHandler";

    public void downloadData() {
        try {
            URL url = new URL("www.marcengelmann.com/download.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            //create JSONObject
            try {
                JSONObject json = new JSONObject(sb.toString());
                JSONArray jArray = null;
                jArray = json.getJSONArray("posts");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject e = jArray.getJSONObject(i);
                    String s = e.getString("post");
                    JSONObject jObject = new JSONObject(s);
                    //put result on Log
                    Log.d("TAG", jObject.getString("NAME") + jObject.getInt("NUMBER"));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        catch(IOException e) {
        Log.e(TAG, e.toString());
        }
    }
}

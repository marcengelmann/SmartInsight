package de.tum.mw.ftm.praktikum.smartinsight;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcengelmann on 14.12.15.
 */
public class ConnectionHandler {

    protected boolean tryLogin(String mUsername, String mPassword)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "matrikelnummer="+mUsername+"&passwort="+mPassword;

        try
        {
            url = new URL("http://www.marcengelmann.com/smart/check_credidentials.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();

            isr.close();
            reader.close();

            if(response.contains("true")) {
                return true;
            } else {
                return false;
            }

        }
        catch(IOException e)
        {
            // Error
        }
        return false;
    }
}

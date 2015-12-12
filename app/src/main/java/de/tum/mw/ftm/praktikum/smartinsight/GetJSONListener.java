package de.tum.mw.ftm.praktikum.smartinsight;

/**
 * Created by marcengelmann on 12.12.15.
 */
import org.json.JSONObject;

public interface GetJSONListener {
    public void onRemoteCallComplete(JSONObject jsonFromNet);
}


package com.jernung.plugins.firebase;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PluginUtils {

    public static Bundle jsonToBundle(JSONObject eventParams) throws Exception {
        Bundle bundle = new Bundle();
        Iterator<String> keys = eventParams.keys();

        while (keys.hasNext()) {
            try {
                String key = keys.next();
                Object value = eventParams.get(key);

                if (value instanceof Boolean) {
                    bundle.putBoolean(key, (Boolean) value);
                } else if (value instanceof Double) {
                    bundle.putDouble(key, (Double) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key, (Integer) value);
                } else if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else {
                    throw new Exception("Unsupported data type: " + value.getClass().getName());
                }
            } catch (JSONException error) {
                throw new Exception("JSONException: " + error.getMessage());
            }
        }

        return bundle;
    }

}

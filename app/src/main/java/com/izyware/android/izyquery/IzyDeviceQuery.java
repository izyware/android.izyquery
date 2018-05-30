package com.izyware.android.izyquery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IzyDeviceQuery {

    private ContentResolver contentResolver;
    private IzyModtask modtask;

    public IzyDeviceQuery(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.modtask = new IzyModtask();
    }

    public JSONObject queryMMS() {
        // The underlying DB for content://mms is PDU
        // Google "how to read mms data in android" for more information
        // There are more things that need to get queried
        return runQuery("content://mms", new String[]{"*"}, "");
    }

    public JSONObject querySMS() {
        // The underlying DB for content://mms is sms
        // DB Schema is:
        // {"body":"Hi There",
        // "svc_cmd":"0",
        // "type":"1",
        // "date":"1526123000021",
        // "teleservice_id":"0",
        // "_id":"10605",
        // "spam_report":"0",
        // "read":"1",
        // "thread_id":"4",
        // "reply_path_present":"0",
        // "protocol":"0",
        // "status":"-1",
        // "deletable":"0",
        // "error_code":"0",
        // "date_sent":"1526000000000",
        // "seen":"1",
        // "reserved":"0",
        // "pri":"0",
        // "sim_slot":"0",
        // "service_center":"+14054054050",
        // "address":"+18008888188",
        // "hidden":"0",
        // "msg_id":"0",
        // "app_id":"0",
        // "locked":"0",
        // "roam_pending":"0"}

        // SELECT address, person, date, body, type FROM sms WHERE ( WHERE ) ORDER BY date ASC
        return runQuery("content://sms", new String[]{"address", "person", "date", "body", "type"}, "date asc");
    }

    // You will need <uses-permission android:name="android.permission.READ_SMS" /> in the manifest
    public JSONObject runQuery(final String uri, final String[] columns, String sortOrder) {
        JSONObject ret = new JSONObject();

        try {

            JSONArray array = new JSONArray();
            ret.put("data", array);

            modtask.Log("Perform Query On: " + uri);

            Cursor myCursor = contentResolver.query(Uri.parse(uri),
                    columns,
                    " 1 = 1 ", null, sortOrder);

            modtask.Log("Query Results: " + Integer.toString(myCursor.getCount()));
            if (myCursor.getCount() > 0) {
                String count = Integer.toString(myCursor.getCount());
                while (myCursor.moveToNext()) {
                    JSONObject item = new JSONObject();

                    String[] cols = myCursor.getColumnNames();
                    for(int j=0; j < cols.length; ++j) {
                        item.put(cols[j], myCursor.getString(j));
                    }
                    array.put(item);
                    // modtask.Log(item.toString());
                }
            }
            ret.put("success", true);
        } catch(JSONException e) {
            try {
                ret.put("reason", "Query Error: " + e.toString());
            } catch(JSONException e1) {}
        }
        return ret;

    }
}

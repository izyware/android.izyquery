package com.izyware.android.izyquery;

import android.content.ContentResolver;
import org.json.JSONException;
import org.json.JSONObject;

public class IzyAndroidSync {
    private ContentResolver contentResolver;
    private IzyDeviceQuery izy;
    private IzyModtask modtask;
    private String postUrl;
    private String idtoken;
    private boolean pushToCloud;

    public IzyAndroidSync(String postUrl, String idtoken, ContentResolver contentResolver) {
        this.postUrl = postUrl;
        this.idtoken = idtoken;
        this.pushToCloud = true;
        if (postUrl == null) {
            this.pushToCloud = false;
        }
        this.contentResolver = contentResolver;
        modtask = new IzyModtask();
        izy =  new IzyDeviceQuery(contentResolver);
    }

    private void processQueryResult(JSONObject ret, String typ) throws JSONException {
        if (ret.get("success").toString() == "true") {
            ret.put("idtoken", idtoken);
            ret.put("typ", typ);
            String str = ret.toString();
            modtask.Log("Query Returned Size: " + str.length());
            if (pushToCloud) {
                IzyPost post = new IzyPost();
                post.sendPost(postUrl, str);
            } else {
                modtask.Log("Skip pushing to cloud ...");
            }
        } else {
            modtask.Log("Skipping post since success was not true");
            modtask.Log(ret.toString());
        }
    }

    public void start() {
        modtask.Log("Starting Izy Android Sync Thread");
        try {
            processQueryResult(izy.querySMS(), "sms");
        } catch(Exception e) {
            modtask.Log("Exception Occured" + e.toString());
        }

        modtask.Log("Izy Android Sync Is Complete");
    }
}

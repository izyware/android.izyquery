package com.izyware.android.izyquery;

import java.net.*;
import java.io.DataOutputStream;

public class IzyPost {
    public void sendPost(final String urlStr, final String payload) {
        final IzyModtask modtask = new IzyModtask();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    modtask.Log("HTTP POST: " + urlStr);
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    // conn.setDoOutput(true);
                    conn.setDoInput(true);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    modtask.Log("payload: " + payload.length());
                    os.write(payload.getBytes("utf-8"));
                    os.flush();
                    os.close();
                    modtask.Log("HTTP Response: " + String.valueOf(conn.getResponseCode()) + ' ' + String.valueOf(conn.getResponseMessage()));
                    conn.disconnect();
                } catch (Exception e) {
                    modtask.Log("Exception: " + e.toString());
                }
            }
        });

        thread.start();
    }
}

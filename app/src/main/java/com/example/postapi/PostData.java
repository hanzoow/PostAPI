package com.example.postapi;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PostData extends AsyncTask<String, Void, UserResponse<String>> {
    private PostDataListener listener;
    private Exception e;

    public PostData(PostDataListener listener) {
        this.listener = listener;
    }

    private String data = "";
    //String urlString = "http://loto.vlythaytien.com/api/v1/provider/login";
    private HttpURLConnection httpsURLConnection = null;

    @Override
    protected UserResponse<String> doInBackground(String... params) {
        try {
            httpsURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            //httpsURLConnection.connect();
            httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(httpsURLConnection.getOutputStream(), "UTF-8");
            wr.write(params[1]);
            wr.flush();
            wr.close();
            httpsURLConnection.getOutputStream().close();

            httpsURLConnection.connect();
            int code = httpsURLConnection.getResponseCode();

            if (code >= 200 && code <= 299) {
                InputStream in = httpsURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
//                int code = httpsURLConnection.getResponseCode();
                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data = data + current;
                }
            } else if (code == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                throw new AuthException();
            } else if (code == HttpsURLConnection.HTTP_INTERNAL_ERROR) {
                throw new AuthException();
            } else {
                InputStream in = httpsURLConnection.getErrorStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int inputStreamData = inputStreamReader.read();

                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data = data + current;
                }

                JSONObject jsonObject = new JSONObject(data);
                throw new Exception(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();

            UserResponse<String> result = new UserResponse<String>();
            result.setE(e);
            return result;
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }

        UserResponse<String> result = new UserResponse<String>();
        result.setData(data);
        return result;
    }

    @Override
    protected void onPostExecute(UserResponse<String> data) {
        super.onPostExecute(data);

        listener.onFinish(data);
    }
}

class AuthException extends Exception {
    public AuthException() {
        super("");
    }
}

interface PostDataListener {
    void onFinish(UserResponse<String> result);
}
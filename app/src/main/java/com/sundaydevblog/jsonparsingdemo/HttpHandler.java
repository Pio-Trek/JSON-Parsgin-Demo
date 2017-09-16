package com.sundaydevblog.jsonparsingdemo;

import android.text.TextUtils;
import android.util.Log;

import com.sundaydevblog.jsonparsingdemo.Models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpHandler {

    /**
     * Tag for the log messages
     */
    private static final String TAG = HttpHandler.class.getSimpleName();

    /**
     * Returns new URL object from the given string URL.
     */
    public URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the user JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link UserModel} object by parsing out information
     * about the user from the input userJSON string.
     */
    UserModel extractFeatureFromJson(String userJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(userJSON)) {
            return null;
        }

        try {
            // Extract out the user name, email, phone and website values
            JSONObject baseJsonResponse = new JSONObject(userJSON);
            String name = baseJsonResponse.getString("name");
            String email = baseJsonResponse.getString("email");
            String phone = baseJsonResponse.getString("phone");
            String website = baseJsonResponse.getString("website");

            // Extract out the user address values
            JSONObject address = baseJsonResponse.getJSONObject("address");
            String street = address.getString("street");
            String suite = address.getString("suite");
            String city = address.getString("city");
            String postcode = address.getString("zipcode");

            // Create a new {@link UserModel} object
            return new UserModel(name, email, phone, website, street, suite, city, postcode);

        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the user JSON results", e);
        }
        return null;
    }

}
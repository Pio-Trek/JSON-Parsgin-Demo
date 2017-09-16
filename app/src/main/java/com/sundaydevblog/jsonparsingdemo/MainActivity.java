package com.sundaydevblog.jsonparsingdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sundaydevblog.jsonparsingdemo.Models.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Displays information about a user.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Tag for the log messages
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * URL to get JSON user data
     */
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users/";

    /**
     * Instantiate Random class object to generate random user number to use with a BASE_URL
     */
    private static Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserAsyncTask task = new UserAsyncTask();
        task.execute();
    }

    private class UserAsyncTask extends AsyncTask<URL, Void, UserModel> {
        HttpHandler handler = new HttpHandler();

        @Override
        protected UserModel doInBackground(URL... urls) {
            // Generate a random number in the range 1..10
            String userRandomNumber = String.valueOf(random.nextInt(10 - 1 + 1) + 1);
            URL url = handler.createUrl(BASE_URL + userRandomNumber);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = handler.makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and return the {@link UserModel} object as the result fo the {@link UserAsyncTask}
            return handler.extractFeatureFromJson(jsonResponse);
        }

        /**
         * Update the screen with the given user (which was the result of the
         * {@link UserAsyncTask}).
         */
        @Override
        protected void onPostExecute(UserModel userModel) {
            if (userModel == null) {
                Toast.makeText(MainActivity.this, "Couldn't get JSON from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                return;
            }

            updateUi(userModel);
        }

        /**
         * Update the screen to display information from the given {@link UserModel}.
         */
        private void updateUi(UserModel user) {

            TextView name = (TextView) findViewById(R.id.name);
            name.setText(user.getName());

            TextView email = (TextView) findViewById(R.id.email);
            email.setText(user.getEmail());

            TextView phone = (TextView) findViewById(R.id.phone);
            phone.setText(user.getPhone());

            TextView website = (TextView) findViewById(R.id.website);
            website.setText(user.getWebsite());

            TextView address = (TextView) findViewById(R.id.address);
            address.setText(getString(R.string.address_text, user.getStreet(), user.getSuite(), user.getPostcode(), user.getCity()));
        }
    }
}

package hackillinois.whitehan.edu.msu.devme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hewhite on 2/20/16.
 */
public class SplashScreen extends Activity {


    public static final String GLOBAL_USERNAME = "edu.msu.whitehan.USERNAME";
    public static final String GLOBAL_PASSWORD = "edu.msu.whitehan.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (devicePreferences.contains("username")) {

            TextView resultText = (TextView)findViewById(R.id.resultText);
            String username = devicePreferences.getString("username", "");
            String password = devicePreferences.getString("password", "");
            new AttemptLogin(resultText, username, password);
        } else {
            // redirect user to login screen
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public class AttemptLogin {

        /**
         * Post param for username during login
         */
        private final String USERNAME = "username=";

        /**
         * Post param for password during login
         */
        private final String PASSWORD = "password=";

        /**
         * Post param for magic so no randoms can attempt to access the login page
         */
        private final String ANDROID_KEY = "androidKey=";

        /**
         * The key that lets us access the login page
         */
        private final String KEY = "qweSghERgtrjhFrh";


        /**
         * The username and password the user input
         */
        private String username = "";
        private String password = "";

        /**
         * The results we process from the server response
         */
        private String results = "";

        /**
         * Creates a new thread to attempt to login to the remote sever
         * @param view
         * @param username
         * @param password
         */
        public AttemptLogin(final TextView view, final String username, final String password) {

            this.username = username;
            this.password = password;

            // Create a thread to login
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String loginResult = login();
                    results = loginResult;

                    Handler handler = new Handler(Looper.getMainLooper());
                    if (results.equals("login success")) {
                        handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                    } else if (results.equals("not verified")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashScreen.this, VerificationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                // pass these along for verification rather than storing them on the phone
                                intent.putExtra(GLOBAL_USERNAME, username);
                                intent.putExtra(GLOBAL_PASSWORD, password);
                                startActivity(intent);
                            }
                        });
                        // invalid credentials
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // remove saved login info so that login process must be redone
                                SharedPreferences device_preferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = device_preferences.edit();
                                editor.remove("username");   // This will delete your preferences
                                editor.remove("password");
                                editor.apply();

                                // redirect user to login screen
                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                }

            }).start();
        }

        public String login() {
            String serverResults = "";

            String postData = USERNAME + username + "&" + PASSWORD + password + "&" + ANDROID_KEY + KEY;

            String urlStr = "http://devme.tech/login-mobile.php";

            try {

                byte[] postDataArray = postData.getBytes();
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Length", Integer.toString(postDataArray.length));
                connection.setUseCaches(false);

                OutputStream out = connection.getOutputStream();
                out.write(postDataArray);
                out.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while((line = reader.readLine()) != null) {
                        serverResults += line;
                    }
                    reader.close();
                }

            } catch (MalformedURLException e) {
                return "malformed url";
            } catch (ProtocolException e) {
                return "post exception";
            } catch (IOException e) {
                return "io exception";
            }

            return serverResults;
        }
    }
}

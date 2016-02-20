package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
 * Created by kayla on 2/20/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.register);

        if (bundle != null)
        {

        }
    }

    public void onNextClick(View view) {
        EditText firstNameTextBox = (EditText)findViewById(R.id.firstName);
        EditText lastNameTextBox = (EditText)findViewById(R.id.lastName);
        EditText usernameTextBox = (EditText)findViewById(R.id.username);
        EditText emailAddressTextBox = (EditText)findViewById(R.id.emailAddress);
        EditText passwordTextBox = (EditText)findViewById(R.id.password);
        EditText confirmPasswordTextBox = (EditText)findViewById(R.id.confirmPassword);
        EditText phoneNumberTextBox = (EditText)findViewById(R.id.phoneNumber);


        TextView resultText = (TextView)findViewById(R.id.registerResultText);

        String firstName = firstNameTextBox.getText().toString();
        String lastName = lastNameTextBox.getText().toString();
        String username = usernameTextBox.getText().toString();
        String emailAddress = emailAddressTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        String confirmPassword = confirmPasswordTextBox.getText().toString();
        String phoneNumber = phoneNumberTextBox.getText().toString();

        if (firstName.length() == 0 )
        {
            resultText.setText("Please provide a first name.");
        } else if (lastName.length() == 0) {
            resultText.setText("Please provide a last name.");
        } else if (username.length() == 0) {
            resultText.setText("Please provide a username.");
        } else if (emailAddress.length() == 0) {
            resultText.setText("Please provide an email address.");
        } else if (password.length() == 0) {
            resultText.setText("Please provide a password.");
        } else if (confirmPassword.length() == 0) {
            resultText.setText("Please provide a password confirmation.");
        } else if (password != confirmPassword) {
            resultText.setText("Passwords don't match.");
        }else if (phoneNumber.length() == 0) {
            resultText.setText("Please provide a phone number.");
        }
        else
        {
            // CALL DATABASE AND THEN REDIRECT TO VERIFICATION PAGE

            Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
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


        /*public AttemptRegister(final TextView view, final String username, final String password) {

            this.username = username;
            this.password = password;

            // Create a thread to login
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String loginResult = login();
                    results = loginResult;

                    /*if (results.equals("login success")) {
                        Handler handler = new Handler(Looper.getMainLooper());

                        // Save credentials to the device for future use

                        if (results.equals("login success")) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    //view.setText(results);
                                }
                            });
                        }
                    } else if (results.equals("not verified")){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //view.setText(results);
                            }
                        });
                        // invalid credentials
                    } else {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setText(results);
                            }
                        });
                    }
                }

            }).start();
        }*/

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

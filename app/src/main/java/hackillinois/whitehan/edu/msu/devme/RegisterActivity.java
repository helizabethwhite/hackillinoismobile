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
        } else if (!password.equals(confirmPassword)) {
            resultText.setText("Passwords don't match.");
        }else if (phoneNumber.length() == 0) {
            resultText.setText("Please provide a phone number.");
        }
        else
        {
            // CALL DATABASE AND THEN REDIRECT TO VERIFICATION PAGE
            resultText.setText("");
            new AttemptRegistration(resultText, username, password, firstName, lastName, phoneNumber, emailAddress);

            /*Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }


    }

    public class AttemptRegistration {

        /**
         * Post param for username during login
         */
        private final String USERNAME = "username=";

        /**
         * Post param for password during login
         */
        private final String PASSWORD = "password=";
        /**
         * Post param for password during login
         */
        private final String EMAIL = "emailAddress=";
        /**
         * Post param for username during login
         */
        private final String FIRSTNAME = "firstName=";

        /**
         * Post param for password during login
         */
        private final String LASTNAME = "lastName=";

        /**
         * Post param for username during login
         */
        private final String VERIFIED = "verified=";


        /**
         * Post param for magic so no randoms can attempt to access the login page
         */
        private final String ANDROID_KEY = "androidKey=";

        /**
         * The key that lets us access the login page
         */
        private final String KEY = "qweSghERgtrjhFrh";


        /**
         * The user inputs
         */
        private String username = "";
        private String password = "";
        private String firstName = "";
        private String lastName = "";
        private String phoneNumber = "";
        private String emailAddress = "";

        /**
         * The results we process from the server response
         */
        private String results = "";

        /**
         * Creates a new thread to attempt to register a user to the remote sever
         * @param view
         * @param username
         * @param password
         * @param firstName
         * @param lastName
         * @param phoneNumber
         */
        public AttemptRegistration(final TextView view, final String username, final String password, final String firstName, final String lastName, final String phoneNumber, final String emailAddress) {

            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.emailAddress = emailAddress;

            // Create a thread to register
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String registerResult = register();
                    results = registerResult;

                    if (results.equals("account created")) {
                        Handler handler = new Handler(Looper.getMainLooper());

                        // Save phone number to phone for future use (text verification)
                        SharedPreferences devicePreferences;
                        devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = devicePreferences.edit();
                        editor.putString("phone-number", phoneNumber);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }  else {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setText(results);
                            }
                        });
                    }
                }

            }).start();
        }

        public String register() {
            String serverResults = "";

            String postData = USERNAME + username + "&" + PASSWORD + password + "&" + ANDROID_KEY + KEY+ "&" + FIRSTNAME + firstName + "&" + LASTNAME + lastName + "&" + EMAIL + emailAddress + "&" + VERIFIED + "0";

            String urlStr = "http://devme.tech/register-mobile.php";

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

package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class VerificationActivity extends AppCompatActivity{


    public static final String GLOBAL_USERNAME = "edu.msu.whitehan.USERNAME";
    public static final String GLOBAL_PASSWORD = "edu.msu.whitehan.PASSWORD";

    private String username;
    private String password;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            username = bundle.getString(GLOBAL_USERNAME, "");
            password = bundle.getString(GLOBAL_PASSWORD, "");
        }
        // get saved phone number from phone
        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
        if (devicePreferences.contains("phone-number")) {
            phoneNumber = devicePreferences.getString("phone-number", "");
            username = devicePreferences.getString("username", "");
            password = devicePreferences.getString("password", "");
            generateRand();
            sendText(phoneNumber);
        }
    }

    public void generateRand() {
        Random r = new Random();
        int code = r.nextInt(9999 - 1000) + 1000; // generate random 4-digit number

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = devicePreferences.edit();
        editor.putInt("code", code);
        editor.commit();
    }

    public void sendText(String phoneNumber) {
        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (devicePreferences.contains("code")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "Verification code: "+Integer.toString(devicePreferences.getInt("code", 0)), null, null);

        } else {
            generateRand();
            sendText(phoneNumber);
        }
    }

    public void checkCode() {

        TextView resultText = (TextView)findViewById(R.id.verificationResultText);
        EditText codeTextBox = (EditText)findViewById(R.id.verificationCodeTextBox);
        String code = codeTextBox.getText().toString();

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
        String storedCode = Integer.toString(devicePreferences.getInt("code", 0));

        if (code.equals(storedCode)) {
            // update database to store verified = 1
            resultText.setText("");
            new AttemptVerification(resultText, username, password);
        } else {
            resultText.setText("Incorrect code.");
        }

    }

    public void onDoneClick(View view) {
        checkCode();
    }

    public void onResendClick(View view) {
        sendText(phoneNumber);
    }

    public class AttemptVerification {

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
        public AttemptVerification(final TextView view, final String username, final String password) {

            this.username = username;
            this.password = password;

            // Create a thread to login
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String updateResult = update();
                    results = updateResult;

                    if (results.equals("update successful")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(VerificationActivity.this, NDAActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    //view.setText(results);
                                }
                            });
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
        }

        public String update() {
            String serverResults = "";

            String postData = USERNAME + username + "&" + PASSWORD + password + "&" + ANDROID_KEY + KEY;

            String urlStr = "http://devme.tech/verify-mobile.php";

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

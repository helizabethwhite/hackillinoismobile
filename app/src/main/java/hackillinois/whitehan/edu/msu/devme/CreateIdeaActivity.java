package hackillinois.whitehan.edu.msu.devme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class CreateIdeaActivity extends AppCompatActivity {

    public static final String GLOBAL_USERNAME = "edu.msu.whitehan.USERNAME";
    public String user;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.create_idea);

        if (bundle != null)
        {
        }

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
        user = devicePreferences.getString("username", "");



        CheckBox checkBoxCasualTest = (CheckBox) findViewById(R.id.checkCasual);

        checkBoxCasualTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    showIPPopUp();

                    CheckBox checkBoxOneDevTest = (CheckBox) findViewById(R.id.checkOneDev);
                    CheckBox checkBoxTwoDevTest = (CheckBox) findViewById(R.id.checkTwoDev);
                    CheckBox checkBoxThreeDevTest = (CheckBox) findViewById(R.id.checkThreeDev);
                    CheckBox checkBoxFourDevTest = (CheckBox) findViewById(R.id.checkFourDev);

                    TextView chooseDevTextTest = (TextView) findViewById(R.id.textHowManyDevs);

                    checkBoxOneDevTest.setEnabled(false);
                    checkBoxTwoDevTest.setEnabled(false);
                    checkBoxThreeDevTest.setEnabled(false);
                    checkBoxFourDevTest.setEnabled(false);

                    chooseDevTextTest.setText("");
                }
                if(!isChecked){
                    CheckBox checkBoxOneDevTest = (CheckBox) findViewById(R.id.checkOneDev);
                    CheckBox checkBoxTwoDevTest = (CheckBox) findViewById(R.id.checkTwoDev);
                    CheckBox checkBoxThreeDevTest = (CheckBox) findViewById(R.id.checkThreeDev);
                    CheckBox checkBoxFourDevTest = (CheckBox) findViewById(R.id.checkFourDev);

                    TextView chooseDevTextTest = (TextView) findViewById(R.id.textHowManyDevs);

                    checkBoxOneDevTest.setEnabled(true);
                    checkBoxTwoDevTest.setEnabled(true);
                    checkBoxThreeDevTest.setEnabled(true);
                    checkBoxFourDevTest.setEnabled(true);

                    chooseDevTextTest.setText(R.string.howManyDevs);
                }
            }
        });

        CheckBox checkBoxWebTest = (CheckBox) findViewById(R.id.checkWebApp);
        CheckBox checkBoxMobileTest = (CheckBox) findViewById(R.id.checkMobileApp);
        CheckBox checkBoxDesktopTest = (CheckBox) findViewById(R.id.checkDesktopApp);

        checkBoxWebTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            CheckBox checkBoxMobileTest2 = (CheckBox) findViewById(R.id.checkMobileApp);
            CheckBox checkBoxDesktopTest2 = (CheckBox) findViewById(R.id.checkDesktopApp);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxMobileTest2.setEnabled(false);
                    checkBoxDesktopTest2.setEnabled(false);
                }
                if(!isChecked) {
                    checkBoxMobileTest2.setEnabled(true);
                    checkBoxDesktopTest2.setEnabled(true);
                }
            }
        });

        checkBoxMobileTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            CheckBox checkBoxWebTest2 = (CheckBox) findViewById(R.id.checkWebApp);
            CheckBox checkBoxDesktopTest2 = (CheckBox) findViewById(R.id.checkDesktopApp);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxWebTest2.setEnabled(false);
                    checkBoxDesktopTest2.setEnabled(false);
                }
                if(!isChecked) {
                    checkBoxWebTest2.setEnabled(true);
                    checkBoxDesktopTest2.setEnabled(true);
                }
            }
        });

        checkBoxDesktopTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            CheckBox checkBoxMobileTest2 = (CheckBox) findViewById(R.id.checkMobileApp);
            CheckBox checkBoxWebTest2 = (CheckBox) findViewById(R.id.checkWebApp);

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBoxMobileTest2.setEnabled(false);
                    checkBoxWebTest2.setEnabled(false);
                }
                if(!isChecked) {
                    checkBoxMobileTest2.setEnabled(true);
                    checkBoxWebTest2.setEnabled(true);
                }
            }
        });
    }

    public void onCreateClick(View view) {

        String title, description;
        String tech = "";
        int casual = 0;
        int numDevs = 0;

        TextView resultText = (TextView)findViewById(R.id.textCreateResult);

        EditText titleTextBox = (EditText)findViewById(R.id.editIdeaTitle);

        CheckBox checkBoxWeb = (CheckBox) findViewById(R.id.checkWebApp);
        CheckBox checkBoxMobile = (CheckBox) findViewById(R.id.checkMobileApp);
        CheckBox checkBoxDesktop = (CheckBox) findViewById(R.id.checkDesktopApp);

        EditText descriptionTextBox = (EditText)findViewById(R.id.editIdeaDescription);

        CheckBox checkBoxCasual = (CheckBox) findViewById(R.id.checkCasual);

        CheckBox checkBoxOneDev = (CheckBox) findViewById(R.id.checkOneDev);
        CheckBox checkBoxTwoDev = (CheckBox) findViewById(R.id.checkTwoDev);
        CheckBox checkBoxThreeDev = (CheckBox) findViewById(R.id.checkThreeDev);
        CheckBox checkBoxFourDev = (CheckBox) findViewById(R.id.checkFourDev);

        if (checkBoxWeb.isChecked()) {
            tech = "Web";
        } else if(checkBoxMobile.isChecked()) {
            tech = "Mobile";
        } else if(checkBoxDesktop.isChecked()) {
            tech = "Desktop";
        } else {
            resultText.setText("Please choose an application type for your idea.");
        }

        if(checkBoxCasual.isChecked()) {
            casual = 1;
        } else if(checkBoxOneDev.isChecked()) {
            numDevs = 1;
            casual = 0;
        } else if (checkBoxTwoDev.isChecked()) {
            numDevs = 2;
            casual = 0;
        } else if (checkBoxThreeDev.isChecked()) {
            numDevs = 3;
            casual = 0;
        } else if (checkBoxFourDev.isChecked()) {
            numDevs = 4;
            casual = 0;
        } else {
            resultText.setText("Please choose either to post a casual idea or the number of developers you'd like.");
        }

        title = titleTextBox.getText().toString();
        description = descriptionTextBox.getText().toString();

        if (title.length() == 0 )
        {
            resultText.setText("Please provide a title for your idea.");
        } else if (description.length() == 0) {
            resultText.setText("Please provide a description for your idea.");
        } else {
            // CALL DATABASE AND THEN REDIRECT TO VERIFICATION PAGE
            resultText.setText("");
            new AttemptCreation(resultText, user, title, tech, description, casual, numDevs);

            /*Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }

    }

    public class AttemptCreation {

        /**
         * Post param for title during idea creation
         */
        private final String TITLE = "title=";

        /**
         * Post param for description during idea creation
         */
        private final String DESCRIPTION = "description=";

        private final String USER = "user=";

        private final String TECH = "tech=";

        private final String CASUAL = "casual=";

        private final String NUMDEVS = "numDevs=";

        /**
         * The user inputs
         */
        private String user = "";
        private String title = "";
        private String tech = "";
        private String description = "";
        private int casual = 0;
        private int numDevs = 0;

        /**
         * The results we process from the server response
         */
        private String results = "";


        public AttemptCreation(final TextView view, final String user, final String title, final String tech, final String description, final int casual, final int numDevs) {

            this.user = user;
            this.title = title;
            this.tech = tech;
            this.description = description;
            this.casual = casual;
            this.numDevs = numDevs;

            // Create a thread to register
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String createResult = create();
                    results = createResult;

                    if (results.equals("idea created")) {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(CreateIdeaActivity.this, NavigationActivity.class);
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

        public String create() {
            String serverResults = "";

            String postData = USER + user + "&" + TITLE + title + "&" + TECH + tech + "&" + DESCRIPTION + description + "&" + CASUAL + casual + "&" + NUMDEVS + numDevs;

            String urlStr = "http://devme.tech/create-idea-mobile.php";

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

    public void showIPPopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Intellectual Property Warning");
        helpBuilder.setMessage("By choosing this option, you are releasing your intellectual property rights to this idea.");
        helpBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}

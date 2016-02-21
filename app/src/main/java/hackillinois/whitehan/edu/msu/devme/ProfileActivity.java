package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
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
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.net.ssl.HttpsURLConnection;

public class ProfileActivity extends AppCompatActivity {

    private String profileUsername;
    private String username;

    public final static String PROFILE_USERNAME = "whitehan.edu.msu.profile_username";

    /**
     * The items we display in the list box. Initially this is
     * null until we get items from the server.
     */
    private ArrayList<Profile> myProfiles = new ArrayList<Profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //if (savedInstanceState != null) {
            //profileUsername = savedInstanceState.getString(PROFILE_USERNAME);
        //}

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (getIntent().hasExtra(PROFILE_USERNAME)) {
            username = getIntent().getStringExtra(PROFILE_USERNAME);
        } else if (devicePreferences.contains("username")) {
            username = devicePreferences.getString("username", "");
        }

        TextView resultText = (TextView) findViewById(R.id.profileResultsText);
        new GetProfileData(resultText, username);
        //displayData();
    }

    public void displayData() {
        TextView welcome = (TextView) findViewById(R.id.profileWelcome);
        TextView first = (TextView) findViewById(R.id.textViewFirstName);
        TextView last = (TextView) findViewById(R.id.textViewLastName);
        TextView userName = (TextView) findViewById(R.id.textViewUsername);
        TextView aboutMe = (TextView) findViewById(R.id.textViewAboutMe);
        TextView email = (TextView) findViewById(R.id.textViewEmail);

        /*SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (devicePreferences.contains("firstName")) {
            welcome.setText("Welcome, " + devicePreferences.getString("firstName", ""));
            username.setText(devicePreferences.getString("username", ""));
            first.setText(devicePreferences.getString("firstName", ""));
            last.setText(devicePreferences.getString("lastName", ""));
            aboutMe.setText(devicePreferences.getString("aboutMe", ""));
        }*/


        if (myProfiles.size() > 0) {
            Profile profile = myProfiles.get(0);

            if (username.equals(profileUsername)) {
                welcome.setText("Welcome, "+ profile.firstName);
            }
            userName.setText(username);
            first.setText(profile.firstName);
            last.setText(profile.lastName);
            aboutMe.setText(profile.aboutMe);
            email.setText(profile.email);
            Log.d("email after set", profile.email );

            welcome.invalidate();
        }
    }


    /**
     * Nested class to store one catalog row
     */
    private static class Profile {
        public String firstName = "";
        public String lastName = "";
        public String email = "";
        public String aboutMe = "";
    }

    public class GetProfileData {

        /**
         * Post param for username during login
         */
        private final String USERNAME = "username=";

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


        /**
         * Creates a new thread to attempt to register a user to the remote sever
         *
         * @param view
         * @param username
         */
        public GetProfileData(final TextView view, final String username) {

            // Create a thread to register
            new Thread(new Runnable() {

                @Override
                public void run() {

                    myProfiles = getData(username);

                    if(myProfiles != null) {
                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                // Tell the adapter the data set has been changed
                                displayData();
                            }

                        });
                    }
                }

            }).start();
        }

        public ArrayList<Profile> getData(String username) {

            Log.d("getdata", "getdata");

            ArrayList<Profile> profiles = new ArrayList<Profile>();

            String postData = USERNAME + username;

            String urlStr = "http://devme.tech/get-user-profile.php";

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
                    /**
                     * Create an XML parser for the result
                     */
                    try {
                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");

                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "profile");

                        String status = xml.getAttributeValue(null, "status");
                        if(status.equals("no")) {
                            return null;
                        }

                        while(xml.nextTag() == XmlPullParser.START_TAG) {
                            if(xml.getName().equals("profile")) {
                                Profile profile = new Profile();
                                profile.firstName = xml.getAttributeValue(null, "firstName");
                                Log.d("first", profile.firstName);
                                profile.lastName = xml.getAttributeValue(null, "lastName");
                                profile.email = xml.getAttributeValue(null, "email");
                                profile.aboutMe = xml.getAttributeValue(null, "aboutMe");
                                profiles.add(profile);
                            }

                            skipToEndTag(xml);
                        }

                        // We are done
                    } catch(XmlPullParserException ex) {
                        return null;
                    } catch(IOException ex) {
                        return null;
                    } finally {
                        try {
                            stream.close();
                        } catch(IOException ex) {

                        }
                    }
                }

            } catch (MalformedURLException e) {
                return null;
            } catch (ProtocolException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            return profiles;
        }
    }

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }
}

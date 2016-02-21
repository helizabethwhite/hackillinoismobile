package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class IdeaActivity extends AppCompatActivity {

    public String title = "";
    public String tech = "";
    public String user = "";
    public String description = "";
    public int casual = 0;


    public String applyAccept = "";
    public String id = "";

    public final static String ID = "hackillinois.whitehan.edu.msu.devme.ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_full_idea);

        if (getIntent().hasExtra(ID)) {
            id = getIntent().getStringExtra(ID);
        }

        //Log.d("YourTag", "YourOutput");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                getIdea(id);
                Log.d("MyTag", id);
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(casual == 1){
            applyAccept = "Accept";
        } else {
            applyAccept = "Apply";
        }

        Button buttonApplyAccept = (Button) findViewById(R.id.buttonApplyAccept);
        TextView textFullTitle = (TextView) findViewById(R.id.textFullTitle);
        TextView textFullDescription = (TextView) findViewById(R.id.textFullDescription);
        TextView textFullTech = (TextView) findViewById(R.id.textFullTech);
        TextView textFullUsername = (TextView) findViewById(R.id.textFullUsername);

        buttonApplyAccept.setText(applyAccept);
        Log.d("YourTag", "display");
        textFullTitle.setText(title);
        textFullDescription.setText(description);
        textFullTech.setText(tech);
        textFullUsername.setText(user);
    }

    /**
     * Get the catalog items from the server
     * @return Array of items or null if failed
     */
    public void getIdea(String id) {

        //Log.d("YourTag", "YourOutput");

        // Create a GET query
        String query = "http://devme.tech/full-activity-get-mobile.php?id="+id;
        Log.d("myothertag", id);

        /**
         * Open the connection
         */
        InputStream stream = null;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            //HttpURLConnection.HTTP_OK
            if(responseCode != 200) {
                return;
            }

            stream = conn.getInputStream();
            //logStream(stream);

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xml = Xml.newPullParser();
                xml.setInput(stream, "UTF-8");

                xml.nextTag();      // Advance to first tag
                xml.require(XmlPullParser.START_TAG, null, "ideas");

                String status = xml.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return;
                }

                //while
                if(xml.nextTag() == XmlPullParser.START_TAG) {
                    if(xml.getName().equals("idea")) {
                        title = xml.getAttributeValue(null, "title");
                        Log.d("mytitle", title);
                        tech = xml.getAttributeValue(null, "tech");
                        description = xml.getAttributeValue(null, "description");
                        Log.d("mydesc", description);
                        user = xml.getAttributeValue(null, "user");
                        casual = Integer.parseInt(xml.getAttributeValue(null, "casual"));

                        Log.d("YourTag", "db");
                    }

                    skipToEndTag(xml);
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return;
            } catch(IOException ex) {
                return;
            } finally {
                try {
                    stream.close();
                } catch(IOException ex) {

                }
            }

        } catch (MalformedURLException e) {
            // Should never happen
            return;
        } catch (IOException ex) {
            return;
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

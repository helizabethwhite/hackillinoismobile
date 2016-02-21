package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private static class fullIdea {
        public String title = "";
        public String tech = "";
        public String id = "";
        public String user = "";
        public int casual = 0;
        public String description = "";
    }


    public String applyAccept = "";
    public String id = "";

    public final static String ID = "hackillinois.whitehan.edu.msu.devme.ID";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_full_idea);

        if (getIntent().hasExtra(ID)) {
            id = getIntent().getStringExtra(ID);
        }

        fullIdea currentIdea = getIdea(id);

        if(currentIdea.casual == 1){
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
        textFullTitle.setText(currentIdea.title);
        textFullDescription.setText(currentIdea.description);
        textFullTech.setText(currentIdea.tech);
        textFullUsername.setText(currentIdea.user);
    }

    /**
     * Get the catalog items from the server
     * @return Array of items or null if failed
     */
    public fullIdea getIdea(String id) {
        fullIdea currentIdea = new fullIdea();

        // Create a GET query
        String query = "http://devme.tech/full-activity-get-mobile.php?id="+id;

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
                return null;
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
                    return null;
                }

                while(xml.nextTag() == XmlPullParser.START_TAG) {
                    if(xml.getName().equals("idea")) {
                        fullIdea idea = new fullIdea();
                        idea.title = xml.getAttributeValue(null, "title");
                        idea.tech = xml.getAttributeValue(null, "tech");
                        idea.id = xml.getAttributeValue(null, "id");
                        idea.description = xml.getAttributeValue(null, "description");
                        idea.user = xml.getAttributeValue(null, "user");
                        idea.casual = Integer.parseInt(xml.getAttributeValue(null, "casual"));
                        currentIdea = idea;
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

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }

        return currentIdea;
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

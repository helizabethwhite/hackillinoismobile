package hackillinois.whitehan.edu.msu.devme;

import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Data {

    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-cat.php";
    private static final String REGISTER_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-save.php";
    private static final String VERIFICATION_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-delete.php";
    private static final String DASHBOARD_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String APPLY_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String ACCEPT_IDEA_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String CREATE_IDEA_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String USER_SETTINGS_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String EDIT_USER_SETTINGS_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String PROFILE_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String EDIT_PROFILE_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String IDEAS_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String IDEA_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String ACCEPT_DEV_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String DELETE_IDEA_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String PROJECTS_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String REVOKE_APPLICATION_URL = "http://webdev.cse.msu.edu/~grotskyk/cse476/step6/hatter-load.php";
    private static final String UTF8 = "UTF-8";

    /**
     * Nested class to store one catalog row
     */
    private static class Idea {
        public String title = "";
        public String tech = "";
        public String id = "";
    }

    /**
     * An adapter so that list boxes can display a list of filenames from
     * the cloud server.
     */
    public static class DashboardAdapter extends BaseAdapter {
        /**
         * Constructor
         */
        public DashboardAdapter(final View view) {
            // Create a thread to load the catalog
            new Thread(new Runnable() {

                @Override
                public void run() {
                    ArrayList<Idea> newIdeas = getCatalog();
                    if(newIdeas != null) {

                        ideas = newIdeas;

                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                // Tell the adapter the data set has been changed
                                notifyDataSetChanged();
                            }

                        });
                    } else {
                        // Error condition!
                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), R.string.ideas_fail, Toast.LENGTH_SHORT).show();
                            }

                        });
                    }

                }

            }).start();
        }

        /**
         * Get the catalog items from the server
         * @return Array of items or null if failed
         */
        public ArrayList<Idea> getCatalog() {
            ArrayList<Idea> newIdeas = new ArrayList<Idea>();

            // Create a GET query -- NOT DONE
            String query = DASHBOARD_URL;

            /**
             * Open the connection
             */
            InputStream stream = null;
            try {
                URL url = new URL(query);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int responseCode = conn.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                stream = conn.getInputStream();

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
                            Idea idea = new Idea();
                            idea.title = xml.getAttributeValue(null, "title");
                            idea.id = xml.getAttributeValue(null, "tech");
                            idea.id = xml.getAttributeValue(null, "id");
                            newIdeas.add(idea);
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

            return newIdeas;
        }

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private ArrayList<Idea> ideas = new ArrayList<Idea>();

        @Override
        public int getCount() {
            return ideas.size();
        }

        @Override
        public Idea getItem(int position) {
            return ideas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_idea, parent, false);
            }

            TextView tv = (TextView)view.findViewById(R.id.textIdeaTitle);
            tv.setText(ideas.get(position).title);

            TextView tv2 = (TextView)view.findViewById(R.id.textIdeaTech);
            tv2.setText(ideas.get(position).tech);

            return view;
        }

        public String getTech(int position) {
            return ideas.get(position).tech;
        }

        public String getTitle(int position){
            return ideas.get(position).title;
        }

        public String getId(int position){
            return ideas.get(position).id;
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

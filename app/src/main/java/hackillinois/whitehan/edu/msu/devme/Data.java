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
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

}

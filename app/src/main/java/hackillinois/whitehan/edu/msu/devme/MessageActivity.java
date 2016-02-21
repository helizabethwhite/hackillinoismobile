package hackillinois.whitehan.edu.msu.devme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;

public class MessageActivity extends AppCompatActivity {


    public static final String USER_RECIPIENT = "edu.msu.whitehan.RECIPIENT";
    public static final String USER_SENDER = "edu.msu.whitehan.SENDER";

    private String username_sender;
    private String username_recipient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            username_sender = bundle.getString(USER_RECIPIENT, "");
            username_recipient = bundle.getString(USER_SENDER, "");
        }

        final Firebase sFireRef = new Firebase("https://mi491app.firebaseio.com");


    }

}

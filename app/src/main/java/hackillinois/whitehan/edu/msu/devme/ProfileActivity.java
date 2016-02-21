package hackillinois.whitehan.edu.msu.devme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity{

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.profile);

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (devicePreferences.contains("username")) {
            username = devicePreferences.getString("username", "");
            password = devicePreferences.getString("password", "");

        }

    }
}

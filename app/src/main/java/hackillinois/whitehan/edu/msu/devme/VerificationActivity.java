package hackillinois.whitehan.edu.msu.devme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class VerificationActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.verification);

        if (bundle != null)
        {

        }

        // get saved phone number from phone
        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
        if (devicePreferences.contains("phone-number")) {
            String phoneNumber = devicePreferences.getString("phone-number", "");

            generateRand();
            sendText(phoneNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_create_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}

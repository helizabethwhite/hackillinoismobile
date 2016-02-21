package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class NDAActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nda);

        TextView IPAgreement = (TextView)findViewById(R.id.textViewIPTerms);
        IPAgreement.setText("DEVELOPER agrees to inform CREATOR of all INVENTIONS and to reasonably cooperate with CREATOR, at CREATOR's expense, to obtain intellectual property (including copyright(s), patent(s), trademark(s), trade secret(s), or other means of protecting inventions) related to INVENTIONS. \n\n" +
                "DEVELOPER agrees to make themself reasonably available to CREATOR personnel, including its attorneys and agents, to sign all papers, take all rightful oaths, and perform all acts which may be necessary, desirable or convenient for fulfilling this assignment and for securing and maintaining " +
                        "intellectual property to INVENTIONS in any and all countries and for vesting title in CREATOR, its successors, assigns, and legal representatives. \n\n" + "The parties agree that any xerographically or electronically reproduced copy of this fully-executed agreement shall have the same legal " +
                        "force and effect as any copy bearing digital signatures of the parties. This agreement constitutes the entire agreement between the parties concerning the subject matter thereof.\n\n " + "DEVELOPER understands they are not required to participate in PROJECT, but if they do so, then the terms of " +
                        "this agreement apply.\n\n " + "DEVELOPER's obligations and responsibilities under this agreement will continue after completion of PROJECT and/or conclusion of their involvement with PROJECT.");
    }


    public void onAcceptClick(View view) {
        Intent intent = new Intent(NDAActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}

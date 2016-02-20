package hackillinois.whitehan.edu.msu.devme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dashboard);

        if (bundle != null)
        {
        }

        // Find the list view
        ListView list = (ListView)findViewById(R.id.listDashboard);

        // Create an adapter
        final Data.DashboardAdapter adapter = new Data.DashboardAdapter(list);
        list.setAdapter(adapter);

        /*
        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position,  long id) {

                // Get the id of the one we want to load
                String catId = adapter.getIdeaId(position);

                DashboardIdeaActivity idea = new DashboardIdeaActivity();
                idea.setIdeaId(ideaId);
            }

        }); */

    }

}

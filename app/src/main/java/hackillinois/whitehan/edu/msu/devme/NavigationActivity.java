package hackillinois.whitehan.edu.msu.devme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String username;
    private String password;

    private static final int MY_CONTENT = 0;
    private static final int MY_PROJECTS = 1;
    private static final int MESSAGES = 2;
    private static final int PROFILE = 4;
    private static final int SETTINGS = 3;
    private static final int LOG_OUT = 5;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    // used for hamburger
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        SharedPreferences devicePreferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);

        if (devicePreferences.contains("username")) {
            username = devicePreferences.getString("username", "");
            password = devicePreferences.getString("password", "");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the list view
        ListView list = (ListView)findViewById(R.id.listDashboard);
        // Create an adapter -- for the main page (ideas)
        final Data.DashboardAdapter adapter = new Data.DashboardAdapter(list);
        list.setAdapter(adapter);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView)findViewById(R.id.navList);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void addDrawerItems() {
        String[] osArray = {"My Content", "My Projects","Messages", "Settings", "Profile", "Log Out" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if (position == MY_CONTENT) {
                    intent = new Intent(NavigationActivity.this, IdeasActivity.class);
                } else if (position == MY_PROJECTS) {
                    intent = new Intent(NavigationActivity.this, ProjectsActivity.class);
                } else if (position == SETTINGS) {
                    intent = new Intent(NavigationActivity.this, UserSettingsActivity.class);
                } else if (position == PROFILE) {
                    intent = new Intent(NavigationActivity.this, ProfileActivity.class);
                } else if (position == MESSAGES) {
                    intent = new Intent(NavigationActivity.this, MessagesActivity.class);
                } else if (position == LOG_OUT) {
                    // remove the preferences (stored login data)
                    SharedPreferences device_preferences = getSharedPreferences("DevMeUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = device_preferences.edit();
                    editor.remove("username");   // This will delete your preferences
                    editor.remove("password");
                    editor.apply();

                    intent = new Intent(NavigationActivity.this, MainActivity.class);
                    // prevent the user from being able to hit the back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Dashboard");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public void onNewIdeaClick(MenuItem item) {
        Intent intent = new Intent(NavigationActivity.this, CreateIdeaActivity.class);
        startActivity(intent);
    }
}

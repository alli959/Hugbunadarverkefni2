package yolo.basket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import yolo.basket.db.Database;
import yolo.basket.teamActivity.TeamActivity;

public class MainActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    Button resumeGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLoginTask checkLoginTask = new CheckLoginTask();
        checkLoginTask.execute((Void) null);

        setContentView(R.layout.activity_main);


        resumeGame = findViewById(R.id.start_gameActivity_button);
        findViewById(R.id.start_teamActivity_button).setOnClickListener(v -> openTeamView());
        findViewById(R.id.start_statsActivity_button).setOnClickListener(v -> openStatsView());
        findViewById(R.id.start_gameActivity_button).setOnClickListener(v -> openGameView());
        new CheckActiveGameTask().execute((Void) null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckActiveGameTask().execute((Void) null);
    }

    private void openStatsView() {
        runOnUiThread(() -> startActivity(new Intent(this, StatsActivity.class)));
    }

    public class CheckLoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (!isLoggedIn())
                goToLoginForm();
            return null;
        }

        private boolean isLoggedIn() {
            try {
                return Database.isLoggedIn();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public class CheckActiveGameTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isLoggedIn();
        }

        private boolean isLoggedIn() {
            try {
                return Database.user.hasActiveGame();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean hasActiveGame) {
            if (hasActiveGame) {
                resumeGame.setVisibility(View.VISIBLE);
            } else {
                resumeGame.setVisibility(View.INVISIBLE);
            }

        }
    }



    public void goToLoginForm() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void openTeamView() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, TeamActivity.class);
            startActivity(intent);
        });
    }

    public void openGameView() {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

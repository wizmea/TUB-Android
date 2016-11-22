package xyz.lebot.tub.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.lebot.tub.R;
import xyz.lebot.tub.ui.adapter.MainActivityFragmentPagerAdapter;
import xyz.lebot.tub.ui.navigator.Navigator;
import xyz.lebot.tub.ui.navigator.NavigatorImpl;
import xyz.lebot.tub.ui.view.CustomBottomNavigationView;
import xyz.lebot.tub.ui.view.CustomViewPager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.activity_main_bottom_navigation)
    CustomBottomNavigationView bottomNavigationView;

    @BindView(R.id.activity_main_view_pager)
    CustomViewPager viewPager;

    @BindView(R.id.activity_main_app_bar_toolbar)
    Toolbar toolbar;

    private PagerAdapter mPagerAdapter;
    private Navigator navigator;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPagerAdapter = new MainActivityFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setPagingEnabled(false);

        navigator = new NavigatorImpl(this, navigator, viewPager, (MainActivityFragmentPagerAdapter) mPagerAdapter);
        navigator.initPartLine();
        navigator.initPartStop();
        navigator.initPartHome();
        navigator.navigateToPartHome();

        //Navigation Drawer
        this.mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.activity_main_manu_action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        navigator.navigateBack();
    }

    public void setContextColor(int res) {
        int color = ContextCompat.getColor(this, res);
        setToolbarColor(color);
        setBottomNavigationColor(res);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void setToolbarColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setBottomNavigationColor(int res) {
        bottomNavigationView.setItemBackgroundResource(res);
    }

    public void setSelecteBottomNavigation(int i) {
        bottomNavigationView.setSelected(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_main_bottom_navigation_home_action:
                navigator.navigateToPartHome();
                break;
            case R.id.activity_main_bottom_navigation_line_action:
                navigator.navigateToPartLine();
                break;
            case R.id.activity_main_bottom_navigation_stop_action:
                navigator.navigateToPartStop();
                break;
        }
        return false;
    }
}

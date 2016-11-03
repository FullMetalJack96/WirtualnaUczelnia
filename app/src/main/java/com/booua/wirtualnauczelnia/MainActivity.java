package com.booua.wirtualnauczelnia;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialTabListener {


    Typeface font_bold;
    TextView home,title,logout;
    private Toolbar mToolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        font_bold =  Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/CaviarDreams.ttf");

        title = (TextView)findViewById(R.id.title);
        title.setTypeface(font_bold);

        home = (TextView)findViewById(R.id.home);
        home.setTypeface(font_bold);

        logout = (TextView)findViewById(R.id.logout);
        logout.setTypeface(font_bold);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                File rememberedUser = new File(getCacheDir(), "XOR_user_cache");
                if(rememberedUser.exists()){
                    BufferedReader input;
                    File file = new File(getCacheDir(), "XOR_user_cache");
                    boolean deleted = file.delete();
                    finish();
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener (new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);

            }
        });


        Bundle extras = getIntent().getExtras();
        String result = extras.getString("result");
        String dateMap = extras.getString("dateMap");
        try {

            JSONArray jsonArray = new JSONArray(dateMap);

            for(int i = 0; i<jsonArray.length() ; i++){
                JSONObject json = jsonArray.getJSONObject(i);

                mTabHost.addTab(mTabHost.newTab().setText(json.getString("date")).setTabListener(MainActivity.this));
                mTabHost.notifyDataSetChanged();
                mAdapter.setCount(mAdapter.getCount() + 1);
            }

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + dateMap + "\"");
        }

        Log.i("tetet", "lol"+result.toString());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());
        Toast.makeText(MainActivity.this, "create view"+mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public static class DummyFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.item_drawer, container, false);
            //tutaj dorzucać poszczególne dane
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int count = 2;
        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            DummyFragment dummyFragment = new DummyFragment();
            return dummyFragment;

        }

        @Override
        public int getCount() {
            return count;
        }

        public void setCount(int newCount) {
            count = newCount;
            notifyDataSetChanged();
        }
    }
}
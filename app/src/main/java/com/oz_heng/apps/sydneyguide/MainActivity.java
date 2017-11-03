package com.oz_heng.apps.sydneyguide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnListFragmentInteractionListener,
        LocationFragment.OnLocationFragmentInteractionListener {

    static final int CATEGORY_PLACE_TO_VISIT = 0;
    static final int CATEGORY_PICNIC_SPOT = 1;
    static final int CATEGORY_RESTAURANT = 2;
    static final int CATEGOGY_WHAT = 3;

    static ArrayList<ArrayList<Location>> listOfListsOfLocations;
    static String[] categoriesArray;
    int category = 0;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        categoriesArray = getResources().getStringArray(R.array.categories_array);

        // Setup the navigation drawer.
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        populateLocationsData();

        selectItem(category);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem  item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_places_visit:
                selectItem(CATEGORY_PLACE_TO_VISIT);
                break;
            case R.id.nav_picnic_spots:
                selectItem(CATEGORY_PICNIC_SPOT);
                break;
            case R.id.nav_restaurants:
                selectItem(CATEGORY_RESTAURANT);
                break;
            case R.id.nav_what:
                selectItem(CATEGOGY_WHAT);
                break;
            case R.id.nav_exit:
                finish();   // Close the activity.
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Launches the {@link ListFragment} with the selected categoty, updates the navigation's
     * selected item and the action bar title.
     * @param category Selected category.
     */
    private void selectItem(int category) {
        // Update the main content by replacing the fragment.
        ListFragment fragment = ListFragment.newInstance(category);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        // Update navigation's selected item and action bar title.
        navigationView.getMenu().getItem(category).setChecked(true);
        setTitle(categoriesArray[category]);
    }

    /**
     * Set the title on the action bar.
     * @param title To be displayed on the action bar.
     */
    private void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    /**
     * Callback from {@link ListFragment} to this host Activity to select the location
     * corresponding the the following parameters.
     * @param category_nbr Number identifying the corresponding category.
     * @param location_nbr Number identifying the selected location.
     */
    @Override
    public void selectLocationFragment(int category_nbr, int location_nbr) {
        // Update the main content by replacing the fragment.
        LocationFragment fragment = LocationFragment.newInstance(category_nbr, location_nbr);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        // Set the title with the location name.
        Location location = listOfListsOfLocations.get(category_nbr).get(location_nbr);
        setTitle(location.getName());
    }

    /**
     * Callback from {@link LocationFragment} to this host Activity, after the user clicks
     * on the "OK" button: go back to the {@link ListFragment} with the selected category.
     * @param category Selected category.
     */
    @Override
    public void backToListFragment(int category) {
        selectItem(category);
    }

    /*
     * Populate the locations data.
     */
    private void populateLocationsData() {
        ArrayList<Location> listOfPlacesToVisit = new ArrayList<>(
                Arrays.asList(
                        new Location(getString(R.string.circular_quay)),
                        new Location("Visit 02"),
                        new Location("Visit 02"),
                        new Location("Visit 03"),
                        new Location("Visit 04"),
                        new Location("Visit 05"),
                        new Location("Visit 06"),
                        new Location("Visit 07"),
                        new Location("Visit 08"),
                        new Location("Visit 09"),
                        new Location("Visit 10"),
                        new Location("Visit 11"),
                        new Location("Visit 12"),
                        new Location("Visit 13"),
                        new Location("Visit 14")
                ));

        ArrayList<Location> listOfPicnicSpots = new ArrayList<>(
                Arrays.asList(
                        new Location("Picnic 01"),
                        new Location("Picnic 02"),
                        new Location("Picnic 02"),
                        new Location("Picnic 03"),
                        new Location("Picnic 04"),
                        new Location("Picnic 05"),
                        new Location("Picnic 06"),
                        new Location("Picnic 07"),
                        new Location("Picnic 08"),
                        new Location("Picnic 09"),
                        new Location("Picnic 10")
                ));

        ArrayList<Location> listOfRestaurants = new ArrayList<>(
                Arrays.asList(
                        new Location("Restaurant 01"),
                        new Location("Restaurant 02"),
                        new Location("Restaurant 02"),
                        new Location("Restaurant 03"),
                        new Location("Restaurant 04"),
                        new Location("Restaurant 05"),
                        new Location("Restaurant 06"),
                        new Location("Restaurant 07"),
                        new Location("Restaurant 08"),
                        new Location("Restaurant 09"),
                        new Location("Restaurant 10")
                ));

        ArrayList<Location> listOfWhats = new ArrayList<>(
                Arrays.asList(
                        new Location("What 01"),
                        new Location("What 02"),
                        new Location("What 02"),
                        new Location("What 03"),
                        new Location("What 04"),
                        new Location("What 05"),
                        new Location("What 06"),
                        new Location("What 07"),
                        new Location("What 08"),
                        new Location("What 09"),
                        new Location("What 10")
                ));

        listOfListsOfLocations = new ArrayList<>(Arrays.asList(listOfPlacesToVisit,
                listOfPicnicSpots, listOfRestaurants, listOfWhats));
    }
}

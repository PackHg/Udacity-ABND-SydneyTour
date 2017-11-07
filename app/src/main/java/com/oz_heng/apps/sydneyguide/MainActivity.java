package com.oz_heng.apps.sydneyguide;

import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnListFragmentInteractionListener,
        LocationFragment.OnLocationFragmentInteractionListener {

    static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Constants for identifying the categories of locations
    static final int CATEGORY_PLACE_TO_VISIT = 0;
    static final int CATEGORY_PICNIC_SPOT = 1;
    static final int CATEGORY_CHEAP_EATS = 2;
    static final int CATEGOGY_NIGHTLIFE = 3;

    // Locations data
    static ArrayList<ArrayList<Location>> listOfListsOfLocations;
    // Array of category names
    static String[] categoriesArray;

    // Current category number
    int currentCategoryNbr = CATEGORY_PLACE_TO_VISIT;
    int currentLocationNbr = 0;

    // Constants identifying the current view (list or location)
    static final int LIST_VIEW = 0;
    static final int LOCATION_VIEW = 1;

    // Current view, list by default
    int currentView = LIST_VIEW;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;

    // Tags used to save user data with SharedPreferences
    static final String USER_DATA = "com.oz_heng.apps.sydneyguide.userData";
    static final String KEY_CATEGORY = "category";
    static final String KEY_LOCATION = "location";
    static final String KEY_VIEW = "view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore user data from SharedPreferences
        SharedPreferences sp = getSharedPreferences(USER_DATA, 0);
        if (sp != null) {
            currentCategoryNbr = sp.getInt(KEY_CATEGORY, currentCategoryNbr);
            currentLocationNbr = sp.getInt(KEY_LOCATION, currentLocationNbr);
            currentView = sp.getInt(KEY_VIEW, currentView);
        }

        Log.v(LOG_TAG, "onCreate() - currentCategoryNbr: " + currentCategoryNbr);
        Log.v(LOG_TAG, "onCreate() - currentLocationNbr: " + currentLocationNbr);
        Log.v(LOG_TAG, "onCreate() - currentView: " + currentView);

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

        if (currentView == LOCATION_VIEW) {
            selectLocationItem(currentCategoryNbr, currentLocationNbr);
        } else {
            selectCategoryItem(currentCategoryNbr);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem  item) {
        // Handle navigation currentView item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_places_visit:
                selectCategoryItem(CATEGORY_PLACE_TO_VISIT);
                break;
            case R.id.nav_picnic_spots:
                selectCategoryItem(CATEGORY_PICNIC_SPOT);
                break;
            case R.id.nav_restaurants:
                selectCategoryItem(CATEGORY_CHEAP_EATS);
                break;
            case R.id.nav_what:
                selectCategoryItem(CATEGOGY_NIGHTLIFE);
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
     * @param category Selected categoryNumber.
     */
    private void selectCategoryItem(int category) {
        // Update navigation's selected item and action bar title.
        navigationView.getMenu().getItem(category).setChecked(true);
        setTitle(categoriesArray[category]);

        currentCategoryNbr = category;
        currentView = LIST_VIEW;

        // Update the main content by replacing the fragment.
        ListFragment fragment = ListFragment.newInstance(category);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
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
     * @param category_nbr Number identifying the corresponding categoryNumber.
     * @param location_nbr Number identifying the selected location.
     */
    @Override
    public void selectLocationItem(int category_nbr, int location_nbr) {
        // Set the title with the location name.
        Location location = listOfListsOfLocations.get(category_nbr).get(location_nbr);
        setTitle(location.getName());

        currentCategoryNbr = category_nbr;
        currentLocationNbr = location_nbr;
        currentView = LOCATION_VIEW;

        // Update the main content by replacing the fragment.
        LocationFragment fragment = LocationFragment.newInstance(category_nbr, location_nbr);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    /**
     * Callback from {@link LocationFragment} to this host Activity, after the user clicks
     * on the "OK" button: go back to the {@link ListFragment} with the selected categoryNumber.
     * @param categoryNumber Selected category number.
     */
    @Override
    public void backToListFragment(int categoryNumber) {
        selectCategoryItem(categoryNumber);
    }


    @Override
    protected void onStop() {
        super.onStop();

        // Save user data into SharedPreferences
        SharedPreferences sp = getSharedPreferences(USER_DATA, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_CATEGORY, currentCategoryNbr);
        editor.putInt(KEY_LOCATION, currentLocationNbr);
        editor.putInt(KEY_VIEW, currentView);
        editor.apply();


        Log.v(LOG_TAG, "onStop() - currentCategoryNbr: " + currentCategoryNbr);
        Log.v(LOG_TAG, "onStop() - currentLocationNbr: " + currentLocationNbr);
        Log.v(LOG_TAG, "onStop() - currentView: " + currentView);
    }

    /**
     * Populate the locations data.
     */
    private void populateLocationsData() {
        ArrayList<Location> listOfPlacesToVisit = new ArrayList<>(
                Arrays.asList(
                        new Location(getString(R.string.c00_circular_quay),
                                R.drawable.c00_circular_quay,
                                getString(R.string.c00_circular_quay_description),
                                getString(R.string.c00_circular_quay_address),
                                getString(R.string.c00_circular_quay_map_url),
                                getString(R.string.c00_circular_quay_web_adr)),
                        new Location(getString(R.string.c00_darling_harbour),
                                R.drawable.c00_darling_harbour,
                                getString(R.string.c00_darling_harbour_description),
                                getString(R.string.c00_darling_harbour_address),
                                getString(R.string.c00_darling_harbour_map_url),
                                getString(R.string.c00_darling_harbour_web_adr)),
                        new Location(getString(R.string.c00_bondi_beach),
                                R.drawable.c00_bondi_beach,
                                getString(R.string.c00_bondi_beach_description),
                                getString(R.string.c00_bondi_beach_address),
                                getString(R.string.c00_bondi_beach_map_url),
                                getString(R.string.c00_bondi_beach_web_adr)),
                        new Location(getString(R.string.c00_taronga_zoo),
                                R.drawable.c00_taronga_zoo,
                                getString(R.string.c00_taronga_zoo_description),
                                getString(R.string.c00_taronga_zoo_address),
                                getString(R.string.c00_taronga_zoo_map_url),
                                getString(R.string.c00_taronga_zoo_web_adr)),
                        new Location(getString(R.string.c00_the_rocks),
                                R.drawable.c00_the_rocks,
                                getString(R.string.c00_the_rocks_description),
                                getString(R.string.c00_the_rocks_address),
                                getString(R.string.c00_the_rocks_map_url),
                                getString(R.string.c00_the_rocks_web_adr)),
                        new Location(getString(R.string.c00_chinatown),
                                R.drawable.c00_chinatown,
                                getString(R.string.c00_chinatown_description),
                                getString(R.string.c00_chinatown_address),
                                getString(R.string.c00_chinatown_map_url),
                                getString(R.string.c00_chinatown_web_adr)),
                        new Location(getString(R.string.c00_manly),
                                R.drawable.c00_manly,
                                getString(R.string.c00_manly_description),
                                getString(R.string.c00_manly_address),
                                getString(R.string.c00_manly_map_url),
                                getString(R.string.c00_manly_web_adr)),
                        new Location(getString(R.string.c00_watsons_bay),
                                R.drawable.c00_watsons_bay,
                                getString(R.string.c00_watsons_bay_description),
                                getString(R.string.c00_watsons_bay_address),
                                getString(R.string.c00_watsons_bay_map_url),
                                getString(R.string.c00_watsons_bay_web_adr)),
                        new Location(getString(R.string.c00_paddington),
                                R.drawable.c00_paddington,
                                getString(R.string.c00_paddington_description),
                                getString(R.string.c00_paddington_address),
                                getString(R.string.c00_paddington_map_url),
                                getString(R.string.c00_paddington_web_adr)),
                        new Location(getString(R.string.c00_newton),
                                R.drawable.c00_newton,
                                getString(R.string.c00_newton_description),
                                getString(R.string.c00_newton_address),
                                getString(R.string.c00_newton_map_url),
                                getString(R.string.c00_newton_web_adr))
                ));

        ArrayList<Location> listOfPicnicSpots = new ArrayList<>(
                Arrays.asList(
                        new Location(getString(R.string.c01_bobbin_head),
                                R.drawable.c01_bobbin_head,
                                getString(R.string.c01_bobbin_head_description),
                                getString(R.string.c01_bobbin_head_address),
                                getString(R.string.c01_bobbin_head_map_url),
                                getString(R.string.c01_bobbin_head_web_adr)),
                        new Location(getString(R.string.c01_wattamolla),
                                R.drawable.c01_wattamolla,
                                getString(R.string.c01_wattamolla_description),
                                getString(R.string.c01_wattamolla_address),
                                getString(R.string.c01_wattamolla_map_url),
                                getString(R.string.c01_wattamolla_web_adr)),
                        new Location(getString(R.string.c01_royal_botanic_garden),
                                R.drawable.c01_royal_botanic_garden,
                                getString(R.string.c01_royal_botanic_garden_description),
                                getString(R.string.c01_royal_botanic_garden_address),
                                getString(R.string.c01_royal_botanic_garden_map_url),
                                getString(R.string.c01_royal_botanic_garden_web_adr)),
                        new Location(getString(R.string.c01_cockatoo_island),
                                R.drawable.c01_cockatoo_island,
                                getString(R.string.c01_cockatoo_island_description),
                                getString(R.string.c01_cockatoo_island_address),
                                getString(R.string.c01_cockatoo_island_map_url),
                                getString(R.string.c01_cockatoo_island_web_adr)),
                        new Location(getString(R.string.c01_bradleys_head),
                                R.drawable.c01_bradleys_head,
                                getString(R.string.c01_bradleys_head_description),
                                getString(R.string.c01_bradleys_head_address),
                                getString(R.string.c01_bradleys_head_map_url),
                                getString(R.string.c01_bradleys_head_web_adr)),
                        new Location(getString(R.string.c01_west_head_lookout),
                                R.drawable.c01_west_head_lookout,
                                getString(R.string.c01_west_head_lookout_description),
                                getString(R.string.c01_west_head_lookout_address),
                                getString(R.string.c01_west_head_lookout_map_url),
                                getString(R.string.c01_west_head_lookout_web_adr)),
                        new Location(getString(R.string.c01_barangaroo_reserve),
                                R.drawable.c01_barangaroo_reserve,
                                getString(R.string.c01_barangaroo_reserve_description),
                                getString(R.string.c01_barangaroo_reserve_address),
                                getString(R.string.c01_barangaroo_reserve_map_url),
                                getString(R.string.c01_barangaroo_reserve_web_adr)),
                        new Location(getString(R.string.c01_bicentennial_park),
                                R.drawable.c01_bicentennial_park,
                                getString(R.string.c01_bicentennial_park_description),
                                getString(R.string.c01_bicentennial_park_address),
                                getString(R.string.c01_bicentennial_park_map_url),
                                getString(R.string.c01_bicentennial_park_web_adr)),
                        new Location(getString(R.string.c01_bradfield_park),
                                R.drawable.c01_bradfield_park,
                                getString(R.string.c01_bradfield_park_description),
                                getString(R.string.c01_bradfield_park_address),
                                getString(R.string.c01_bradfield_park_map_url),
                                getString(R.string.c01_bradfield_park_web_adr)),
                       new Location(getString(R.string.c01_balls_head_reserve),
                                R.drawable.c01_balls_head_reserve,
                                getString(R.string.c01_balls_head_reserve_description),
                                getString(R.string.c01_balls_head_reserve_address),
                                getString(R.string.c01_balls_head_reserve_map_url),
                                getString(R.string.c01_balls_head_reserve_web_adr))
                ));

        ArrayList<Location> listOfChaepEats = new ArrayList<>(
                Arrays.asList(
                        new Location(getString(R.string.c02_belles_hot_chicken),
                                R.drawable.c02_belles_hot_chicken,
                                getString(R.string.c02_belles_hot_chicken_description),
                                getString(R.string.c02_belles_hot_chicken_address),
                                getString(R.string.c02_belles_hot_chicken_map_url),
                                getString(R.string.c02_belles_hot_chicken_web_adr)),
                        new Location(getString(R.string.c02_bovine_and_swine_barbecue),
                                R.drawable.c02_bovine_and_swine_barbecue,
                                getString(R.string.c02_bovine_and_swine_barbecue_description),
                                getString(R.string.c02_bovine_and_swine_barbecue_address),
                                getString(R.string.c02_bovine_and_swine_barbecue_map_url),
                                getString(R.string.c02_bovine_and_swine_barbecue_web_adr)),
                        new Location(getString(R.string.c02_happy_chef),
                                R.drawable.c02_happy_chef,
                                getString(R.string.c02_happy_chef_description),
                                getString(R.string.c02_happy_chef_address),
                                getString(R.string.c02_happy_chef_map_url),
                                getString(R.string.c02_happy_chef_web_adr)),
                        new Location(getString(R.string.c02_malay_chinese_takeaway),
                                R.drawable.c02_malay_chinese_takeaway,
                                getString(R.string.c02_malay_chinese_takeaway_description),
                                getString(R.string.c02_malay_chinese_takeaway_address),
                                getString(R.string.c02_malay_chinese_takeaway_map_url),
                                getString(R.string.c02_malay_chinese_takeaway_web_adr)),
                        new Location(getString(R.string.c02_casa_do_benfica),
                                R.drawable.c02_casa_do_benfica,
                                getString(R.string.c02_casa_do_benfica_description),
                                getString(R.string.c02_casa_do_benfica_address),
                                getString(R.string.c02_casa_do_benfica_map_url),
                                getString(R.string.c02_casa_do_benfica_web_adr)),
                        new Location(getString(R.string.c02_chatkazz),
                                R.drawable.c02_chatkazz,
                                getString(R.string.c02_chatkazz_description),
                                getString(R.string.c02_chatkazz_address),
                                getString(R.string.c02_chatkazz_map_url),
                                getString(R.string.c02_chatkazz_web_adr)),
                        new Location(getString(R.string.c02_chum_tang),
                                R.drawable.c02_chum_tang,
                                getString(R.string.c02_chum_tang_description),
                                getString(R.string.c02_chum_tang_address),
                                getString(R.string.c02_chum_tang_map_url),
                                getString(R.string.c02_chum_tang_web_adr)),
                       new Location(getString(R.string.c02_kingsford_chinese),
                                R.drawable.c02_kingsford_chinese,
                                getString(R.string.c02_kingsford_chinese_description),
                                getString(R.string.c02_kingsford_chinese_address),
                                getString(R.string.c02_kingsford_chinese_map_url),
                                getString(R.string.c02_kingsford_chinese_web_adr)),
                        new Location(getString(R.string.c02_mr_crackles),
                                R.drawable.c02_mr_crackles,
                                getString(R.string.c02_mr_crackles_description),
                                getString(R.string.c02_mr_crackles_address),
                                getString(R.string.c02_mr_crackles_map_url),
                                getString(R.string.c02_mr_crackles_web_adr)),
                        new Location(getString(R.string.c02_mamak),
                                R.drawable.c02_mamak,
                                getString(R.string.c02_mamak_description),
                                getString(R.string.c02_mamak_address),
                                getString(R.string.c02_mamak_map_url),
                                getString(R.string.c02_mamak_web_adr))
                ));

        ArrayList<Location> listOfWhats = new ArrayList<>(
                Arrays.asList(
                        new Location(getString(R.string.c03_opera_bar),
                                R.drawable.c03_opera_bar,
                                getString(R.string.c03_opera_bar_description),
                                getString(R.string.c03_opera_bar_address),
                                getString(R.string.c03_opera_bar_map_url),
                                getString(R.string.c03_opera_bar_web_adr)),
                        new Location(getString(R.string.c03_cafe_sydney),
                                R.drawable.c03_cafe_sydney,
                                getString(R.string.c03_cafe_sydney_description),
                                getString(R.string.c03_cafe_sydney_address),
                                getString(R.string.c03_cafe_sydney_map_url),
                                getString(R.string.c03_cafe_sydney_web_adr)),
                        new Location(getString(R.string.c03_the_basement),
                                R.drawable.c03_the_basement,
                                getString(R.string.c03_the_basement_description),
                                getString(R.string.c03_the_basement_address),
                                getString(R.string.c03_the_basement_map_url),
                                getString(R.string.c03_the_basement_web_adr)),
                        new Location(getString(R.string.c03_chinese_laundry),
                                R.drawable.c03_chinese_laundry,
                                getString(R.string.c03_chinese_laundry_description),
                                getString(R.string.c03_chinese_laundry_address),
                                getString(R.string.c03_chinese_laundry_map_url),
                                getString(R.string.c03_chinese_laundry_web_adr)),
                        new Location(getString(R.string.c03_annandale_hotel),
                                R.drawable.c03_annandale_hotel,
                                getString(R.string.c03_annandale_hotel_description),
                                getString(R.string.c03_annandale_hotel_address),
                                getString(R.string.c03_annandale_hotel_map_url),
                                getString(R.string.c03_annandale_hotel_web_adr)),
                        new Location(getString(R.string.c03_home),
                                R.drawable.c03_home,
                                getString(R.string.c03_home_description),
                                getString(R.string.c03_home_address),
                                getString(R.string.c03_home_map_url),
                                getString(R.string.c03_home_web_adr)),
                        new Location(getString(R.string.c03_grasshopper),
                                R.drawable.c03_grasshopper,
                                getString(R.string.c03_grasshopper_description),
                                getString(R.string.c03_grasshopper_address),
                                getString(R.string.c03_grasshopper_map_url),
                                getString(R.string.c03_grasshopper_web_adr)),
                        new Location(getString(R.string.c03_arq),
                                R.drawable.c03_arq,
                                getString(R.string.c03_arq_description),
                                getString(R.string.c03_arq_address),
                                getString(R.string.c03_arq_map_url),
                                getString(R.string.c03_arq_web_adr)),
                        new Location(getString(R.string.c03_qudos_bank_arena),
                                R.drawable.c03_qudos_bank_arena,
                                getString(R.string.c03_qudos_bank_arena_description),
                                getString(R.string.c03_qudos_bank_arena_address),
                                getString(R.string.c03_qudos_bank_arena_map_url),
                                getString(R.string.c03_qudos_bank_arena_web_adr))
                ));

        listOfListsOfLocations = new ArrayList<>(Arrays.asList(listOfPlacesToVisit,
                listOfPicnicSpots, listOfChaepEats, listOfWhats));
    }
}

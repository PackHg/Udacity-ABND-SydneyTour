package com.oz_heng.apps.sydneyguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.oz_heng.apps.sydneyguide.MainActivity.listOfListsOfLocations;

/**
 * Fragment displaying a location data.
 */
public class LocationFragment extends Fragment {
    private static final String ARG_CATEGORY_NUMBER = "category_number";
    private static final String ARG_LOCATION_NUMBER = "location_number";

    private int categoryNbr;
    private int locationNbr;
    private Location location;

    private OnLocationFragmentInteractionListener mListener;

    private Unbinder unbinder;
    @BindView(R.id.location_picture) ImageView locationPicture;
    @BindView(R.id.location_description) TextView locationDescription;
    @BindView(R.id.location_map) TextView locationMap;
    @BindView(R.id.location_web_adr) TextView locationWebAddress;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryNbr Selected category number.
     * @param locationNbr Selected location number.
     * @return A new instance of fragment LocationFragment.
     */
    public static LocationFragment newInstance(int categoryNbr, int locationNbr) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_NUMBER, categoryNbr);
        args.putInt(ARG_LOCATION_NUMBER, locationNbr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryNbr = getArguments().getInt(ARG_CATEGORY_NUMBER);
            locationNbr = getArguments().getInt(ARG_LOCATION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Display location data
        location = listOfListsOfLocations.get(categoryNbr).
                get(locationNbr);
        locationPicture.setImageResource(location.getDrawableId());
        locationDescription.setText(location.getDescription());
        locationMap.setText(location.getAddress());
        locationWebAddress.setText(location.getWebAddress());

        return view;
    }

    @OnClick(R.id.location_map)
    void openMaps() {
        openWebPage(location.getMapUrl());
    }

    /**
     * On click of the OK button: go back to the corresponding {@link ListFragment}.
     */
    @OnClick(R.id.location_button_ok)
    void onClickOkButton() {
        mListener.backToListFragment(categoryNbr, locationNbr);
    }

    /**
     * This interface must be implemented by a hosting activity.
     */
    interface OnLocationFragmentInteractionListener {
        /**
         * Go back the {@link ListFragment} with the selected categoryNbr.
         * @param categoryNumber Selected category number.
         * @param locationNbr Selected location number.
         */
        void backToListFragment(int categoryNumber, int locationNbr);
    }

    /**
     * Checks if the hosting Activity implements {@link OnLocationFragmentInteractionListener} interface.
     * If not, throws a {@link RuntimeException}.
     * @param context The Activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationFragmentInteractionListener) {
            mListener = (OnLocationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Open a Web browser at the url address.
     *
     * @param url a {@link String}
     */
    private void openWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

package com.oz_heng.apps.sydneyguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 *
 **/
public class LocationFragment extends Fragment {
    private static final String LOG_TAG = LocationFragment.class.getSimpleName();

    private static final String ARG_CATEGORY = "category";
    private static final String ARG_LOCATION = "location";

    private int category;
    private int location;

    private OnLocationFragmentInteractionListener mListener;

    private Unbinder unbinder;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Selected category.
     * @param location Selected location.
     * @return A new instance of fragment LocationFragment.
     */
    public static LocationFragment newInstance(int category, int location) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        args.putInt(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getInt(ARG_CATEGORY);
            location = getArguments().getInt(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView()");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        unbinder = ButterKnife.bind(this, view);

//        // In the viewStub, inflate the layout corresponding location.
//        ViewStub viewStub = (ViewStub) view.findViewById(R.id.view_stub);
//        viewStub.setLayoutResource(R.layout.circular_quay);
//        viewStub.inflate();

//        ImageView imageView = viewStub.findViewById(R.id.location_picture_large);
//        imageView.setImageDrawable(R.drawable.);

        return view;
    }

    /**
     * on click of the OK button: go back to the corresponding {@link ListFragment}.
     */
    @OnClick(R.id.location_button_ok)
    void onClickOkButton() {
        mListener.backToListFragment(category);
    }

    /**
     * This interface must be implemented by a hosting activity.
     */
    interface OnLocationFragmentInteractionListener {
        /**
         * Go back the {@link ListFragment} with the selected category.
         * @param category Selected category.
         */
        void backToListFragment(int category);
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
}

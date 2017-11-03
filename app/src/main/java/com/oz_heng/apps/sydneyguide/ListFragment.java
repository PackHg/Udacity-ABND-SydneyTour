package com.oz_heng.apps.sydneyguide;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.oz_heng.apps.sydneyguide.MainActivity.listOfListsOfLocations;


/**
 * {@link ListFragment} class pertaining to a list of {@link Location}.
 */
public class ListFragment extends Fragment {
    private static final String LOG_TAG = ListFragment.class.getSimpleName();

    static final String ARG_CATEGORY = "category";

    private int category;

    private Unbinder unbinder;

    private OnListFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Selected category.
     * @return A new instance of ListFragment.
     */
    public static ListFragment newInstance(int category) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getInt(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView()");

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Setup a LocationAdapter for the GridView.
        GridView gridView = view.findViewById(R.id.grid_view);
        ArrayList<Location> locations = listOfListsOfLocations.get(category);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), locations);
        gridView.setAdapter(locationAdapter);

        // Setup an OnItemClickListener on the GridView.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.selectLocationFragment(category, i);
            }
        });

        return view;
    }

    /**
     * This interface must be implemented by a hosting activity.
     */
    interface OnListFragmentInteractionListener {
        /**
         * To launch the {@link LocationFragment} with the selected category and location.
         * @param category Selected category.
         * @param location Selected location.
         */
        void selectLocationFragment(int category, int location);
    }

    /**
     * Checks if the hosting Activity implements {@link OnListFragmentInteractionListener} interface.
     * If not, throws a {@link RuntimeException}.
     * @param context The Activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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

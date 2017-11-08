package com.oz_heng.apps.sydneyguide;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.oz_heng.apps.sydneyguide.MainActivity.listOfListsOfLocations;


/**
 * {@link ListFragment} class displaying a list of {@link Location} based on the selected category
 * number.
 * The location number is for the {@link ListView} to focus on the location that has been selected.
 */
public class ListFragment extends Fragment {

    static final String ARG_CATEGORY_NUMBER = "category_number";
    static final String ARG_LOCATION_NUMBER = "location_number";

    private int categoryNbr;
    private int locationNbr;

    private Unbinder unbinder;

    private OnListFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoryNbr Selected category number.
     * @param locationNbr Selected location number.
     * @return A new instance of ListFragment.
     */
    public static ListFragment newInstance(int categoryNbr, int locationNbr) {
        ListFragment fragment = new ListFragment();
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Setup a LocationAdapter for the GridView.
        ListView listView = view.findViewById(R.id.list_view);
        ArrayList<Location> locations = listOfListsOfLocations.get(categoryNbr);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), locations);
        listView.setAdapter(locationAdapter);

        // Sets the currently selected item.
        listView.setSelection(locationNbr);

        // Setup an OnItemClickListener on the GridView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.selectLocationItem(categoryNbr, i);
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
         * @param categoryNbr Selected category number.
         * @param locationNbr Selected location number.
         */
        void selectLocationItem(int categoryNbr, int locationNbr);
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

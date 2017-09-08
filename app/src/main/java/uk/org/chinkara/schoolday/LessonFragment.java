package uk.org.chinkara.schoolday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.TimeZone;

import uk.org.chinkara.schoolday.model.SchoolCalendar;
import uk.org.chinkara.schoolday.model.TimetableItem;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LessonFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonFragment() {
    }

    @SuppressWarnings("unused")
    public static LessonFragment newInstance(int columnCount) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Frag", "Enter onCreate");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        Log.d("Frag", "Exit onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Frag", "Enter onCreateView");
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            _recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                _recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                _recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            _displayed_date = Calendar.getInstance(_calendar.timezone());
            _recyclerView.setAdapter(new MyLessonRecyclerViewAdapter(_calendar.createTimetable(_displayed_date), mListener));
            refreshDisplay();
        }
        Log.d("Frag", "Exit onCreateView");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Frag", "Enter onAttach");
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        _calendar = new SchoolCalendar(context);
        Log.d("Frag", "Exit onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Frag", "Enter onDetach");
        mListener = null;
        _calendar = null;
        Log.d("Frag", "Exit onDetach");
    }

    public void refreshDisplay() {

        _recyclerView.getAdapter().notifyDataSetChanged();
    }

    Calendar _displayed_date;
    SchoolCalendar _calendar;
    RecyclerView _recyclerView;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(TimetableItem item);
    }
}

package uk.org.chinkara.schoolday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import uk.org.chinkara.schoolday.model.SchoolCalendar;
import uk.org.chinkara.schoolday.model.TimetableItem;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LessonFragment extends Fragment {

    private static final String ARG_DATE_OFFSET = "date-offset";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonFragment() {
    }

    public static LessonFragment newInstance(int date_offset) {

        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DATE_OFFSET, date_offset);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Frag", "Enter onCreate");
        if (getArguments() != null) {

            _dateOffset = getArguments().getInt(ARG_DATE_OFFSET);
        }
        Log.d("Frag", "Exit onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Frag", "Enter onCreateView");
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        // Set the adapter
        _recyclerView = view.findViewById(R.id.list);
        Context context = view.getContext();
        _recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Calendar displayedDate = Calendar.getInstance(_calendar.timezone());
        displayedDate.add(Calendar.DAY_OF_YEAR, _dateOffset);

        _recyclerView.setAdapter(new MyLessonRecyclerViewAdapter(_calendar.createTimetable(displayedDate), mListener));
        refreshDisplay();

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

    @Override
    public void onStart() {

        super.onStart();
        Log.d("Frag", "Enter onStart");
        Calendar cal = Calendar.getInstance();
        _timer = new Timer();
        _timer.schedule(new RefreshLessonTask(), (60 - cal.get(Calendar.SECOND))*1000, 60*1000);
        Log.d("Frag", "Exit onStart");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d("Frag", "Enter onStop");
        if (_timer != null) {

            _timer.cancel();
            _timer = null;
        }
        Log.d("Frag", "Exit onStop");
    }

    private class RefreshLessonTask extends TimerTask {

        RefreshLessonTask() {

        }

        @Override
        public void run() {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    refreshDisplay();
                }
            });
        }
    }

    private void refreshDisplay() {

        _recyclerView.getAdapter().notifyDataSetChanged();
    }

    private int _dateOffset;
    private SchoolCalendar _calendar;
    private RecyclerView _recyclerView;
    private Timer _timer;
    private OnListFragmentInteractionListener mListener;

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

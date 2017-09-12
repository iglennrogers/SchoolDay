package uk.org.chinkara.schoolday;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.org.chinkara.schoolday.LessonFragment.OnListFragmentInteractionListener;
import uk.org.chinkara.schoolday.model.TimetableItem;

import java.util.List;


/**
 * {@link RecyclerView.Adapter} that can display a {@link TimetableItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
class MyLessonRecyclerViewAdapter extends RecyclerView.Adapter<MyLessonRecyclerViewAdapter.ViewHolder> {

    private final List<TimetableItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    MyLessonRecyclerViewAdapter(List<TimetableItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TimetableItem item = mValues.get(position);
        holder.mItem = item;
        if (item.doubleLesson()) {

            holder.mContentView.setText("\n" + item.description() + "\n");
        }
        else {

            holder.mContentView.setText(item.description());
        }
        holder.mTimeView.setText(item.startTime());
        holder.mRoomView.setText(item.room());
        holder.mTeacherView.setText(item.teacher());

        int colour;
        if (item.currentTimeWithin()) {

            colour = ContextCompat.getColor((Context)mListener, R.color.current);
        }
        else if (item.isBreak()) {

            colour = ContextCompat.getColor((Context)mListener, R.color.break_time);
        }
        else if (item.period()%2 == 0) {

            colour = ContextCompat.getColor((Context)mListener, R.color.even);
        }
        else {

            colour = ContextCompat.getColor((Context)mListener, R.color.odd);
        }
        holder.mView.setBackgroundColor(colour);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTimeView;
        final TextView mContentView;
        final TextView mRoomView;
        final TextView mTeacherView;
        TimetableItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mTimeView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
            mRoomView = view.findViewById(R.id.room);
            mTeacherView = view.findViewById(R.id.teacher);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

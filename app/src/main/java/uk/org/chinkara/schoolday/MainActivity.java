package uk.org.chinkara.schoolday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.TimeZone;

import uk.org.chinkara.schoolday.model.SchoolCalendar;
import uk.org.chinkara.schoolday.model.TimetableItem;


public class MainActivity extends AppCompatActivity implements LessonFragment.OnListFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Act", "Enter onCreate");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        DayListAdapter dayAdapter = new DayListAdapter(getSupportFragmentManager());
        _pager = (ViewPager)findViewById(R.id.pager);
        _pager.setAdapter(dayAdapter);
        _pager.setOffscreenPageLimit(NUM_DAYS);
        _pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                changePage(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        changePage(0);
        Log.d("Act", "Exit onCreate");
    }

    @Override
    public void onBackPressed() {

        Log.d("Act", "onBackPressed");
        if (_pager.getCurrentItem() > 0) {

            _pager.setCurrentItem(0);
        }
        else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            _pager.setCurrentItem(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(TimetableItem item) {
    }

    private void changePage(int position) {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            if (position == 0) {

                String title = String.format("%s - %s", getString(R.string.app_name),
                        getString(R.string.today));
                actionBar.setTitle(title);
            }
            else {

                Calendar displayedDate = Calendar.getInstance(TimeZone.getDefault());
                displayedDate.add(Calendar.DAY_OF_YEAR, position);
                actionBar.setTitle(SchoolCalendar.day_format.format(displayedDate.getTime()));
            }
            actionBar.setHomeButtonEnabled(position != 0);
            actionBar.setDisplayHomeAsUpEnabled(position != 0);
        }
    }

    private static class DayListAdapter extends FragmentPagerAdapter {

        DayListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {

            return NUM_DAYS;
        }

        @Override
        public Fragment getItem(int position) {

            return LessonFragment.newInstance(position);
        }
    }

    static final int NUM_DAYS = 7;

    private ViewPager _pager;
}

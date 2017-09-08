package uk.org.chinkara.schoolday;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import uk.org.chinkara.schoolday.model.TimetableItem;


public class MainActivity extends AppCompatActivity implements LessonFragment.OnListFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Act", "Enter onCreate");
        _context = this;

        Log.d("Act", "Exit onCreate");
    }

    @Override
    public void onListFragmentInteraction(TimetableItem item) {

    }

    @Override
    public void onStart() {

        super.onStart();
        Log.d("Act", "Enter onStart");
        _lesson_frag = (LessonFragment)getSupportFragmentManager().findFragmentById(R.id.fragLesson);
        Calendar cal = Calendar.getInstance();
        _timer = new Timer();
        _timer.schedule(new RefreshLessonTask(), (60 - cal.get(Calendar.SECOND))*1000, 60*1000);
        Log.d("Act", "Exit onStart");
    }

    @Override
    public void onStop() {

        super.onStop();
        Log.d("Act", "Enter onStop");
        if (_timer != null) {

            _timer.cancel();
            _timer = null;
        }
        Log.d("Act", "Exit onStop");
    }

    private class RefreshLessonTask extends TimerTask {

        RefreshLessonTask() {

        }

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    _lesson_frag.refreshDisplay();
                }
            });
        }
    }

    private Context _context;
    private LessonFragment _lesson_frag;
    private Timer _timer;
}

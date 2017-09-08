package uk.org.chinkara.schoolday.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import uk.org.chinkara.schoolday.model.personal.LessonDetail;
import uk.org.chinkara.schoolday.model.structure.LessonTimes;


public class TimetableItem {

    static TimetableItem createHolidays() {

        return new TimetableItem(0, "Holiday");
    }

    TimetableItem(SchoolCalendar cal, LessonDetail detail, LessonTimes times) {

        _period = times.period();
        _start.setTime(times.startTime());
        _end.setTime(times.endTime());

        if (times.isBreak()) {

            _span = 1;
            _description = "Break";
        }
        else {

            _span = detail.span();
            _description = cal.subject(detail.subject());
            _room = detail.room();
            _teacher = detail.teacher();
        }
    }

    private TimetableItem(int period, String description) {

        _period = period;
        _start = null;
        _end = null;
        _description = description;
    }

    public boolean isBreak() {

        return _period == 0;
    }

    void extendEnd(LessonTimes times) {

        _end.setTime(times.endTime());
    }

    public boolean doubleLesson() {

        return (_span > 1);
    }

    public int period() {

        return _period;
    }

    public String startTime() {

        if (_start != null) {

            return SchoolCalendar.time_format.format(_start.getTime());
        }
        else {

            return "";
        }
    }

    public String description() {

        return _description;
    }

    public String room() {

        return _room;
    }

    public String teacher() {

        return _teacher;
    }

    public boolean currentTimeWithin()
    {
        if (_start != null) {

            Calendar now = GregorianCalendar.getInstance();
            return minutes(_start) <= minutes(now) && minutes(now) < minutes(_end);
        }
        return false;
    }

    @Override
    public String toString() {

        return String.format(Locale.getDefault(),
                "TimetableItem(Period: %d, Start: %s, End: %s, Desc: %s)",
                _period, SchoolCalendar.time_format.format(_start.getTime()),
                SchoolCalendar.time_format.format(_end.getTime()), _description);
    }

    private int minutes(Calendar time) {

        return time.get(Calendar.HOUR_OF_DAY)*3600 + time.get(Calendar.MINUTE)*60 +
                time.get(Calendar.SECOND);
    }

    private int _span;
    private int _period;
    private Calendar _start = GregorianCalendar.getInstance();
    private Calendar _end = GregorianCalendar.getInstance();
    private String _room;
    private String _teacher;
    private String _description;
}

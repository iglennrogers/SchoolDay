package uk.org.chinkara.schoolday.model.structure;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.org.chinkara.schoolday.model.SchoolCalendar;


class Term {

    Term(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "start":
                    Date start = SchoolCalendar.date_format.parse(reader.nextString());
                    _start = GregorianCalendar.getInstance();
                    _start.setTime(start);
                    break;
                case "end":
                    Date end = SchoolCalendar.date_format.parse(reader.nextString());
                    _end = GregorianCalendar.getInstance();
                    _end.setTime(end);
                    break;
                case "startweek":
                    _week_number = reader.nextInt();
                    break;
                default:
                    throw new JSONException("Bad tag in Term object");
            }
        }
        reader.endObject();

        if (_start == null || _end == null || _week_number == 0) {

            throw new JSONException("Need start, end and week number for Term");
        }
    }

    int weekNumber(final Calendar date, final int cycle) {

        int week_date = date.get(Calendar.WEEK_OF_YEAR);
        int week_start = _start.get(Calendar.WEEK_OF_YEAR);

        return  1 + (week_date - week_start + _week_number - 1)%cycle;
    }

    boolean isDateWithin(final Calendar date) {

        return (_start.before(date) || _end.after(date));
    }

    @Override
    public String toString() {

        return "Term(Start:" + SchoolCalendar.date_format.format(_start.getTime()) +
                ", End: " + SchoolCalendar.date_format.format(_end.getTime()) + ", Week: " +
                ((Integer)_week_number).toString() + ")";
    }

    private Calendar _start;
    private Calendar _end;
    private int _week_number;
}

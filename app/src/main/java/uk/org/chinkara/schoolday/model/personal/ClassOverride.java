package uk.org.chinkara.schoolday.model.personal;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.org.chinkara.schoolday.model.SchoolCalendar;


class ClassOverride {

    ClassOverride(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "date": {
                    Date date = SchoolCalendar.date_format.parse(reader.nextString());
                    _date = GregorianCalendar.getInstance();
                    _date.setTime(date);
                }
                break;
                case "description": {
                    _description = reader.nextString();
                }
                break;
                case "start": {
                    Date time_start = SchoolCalendar.time_format.parse(reader.nextString());
                    _start_time = GregorianCalendar.getInstance();
                    _start_time.setTime(time_start);
                }
                break;
                case "end": {
                    Date time_end = SchoolCalendar.time_format.parse(reader.nextString());
                    _end_time = GregorianCalendar.getInstance();
                    _end_time.setTime(time_end);
                }
                break;
                default:
                    throw new JSONException("Bad tag in ClassOverride object");
            }
        }
        reader.endObject();

        if ((_date == null) || _description.isEmpty()) {

            throw new JSONException("Need both date and description for ClassOverride");
        }
    }

    String description()
    {
        return _description;
    }

    boolean isOverridden(final Calendar date)
    {
        return date.get(Calendar.DAY_OF_YEAR) == _date.get(Calendar.DAY_OF_YEAR);
    }

    boolean isInsertDay() {

        return (_start_time == null) && (_end_time == null);
    }

    Date startTime(final Date school_start)
    {
        if (_start_time != null) {

            return _start_time.getTime();
        }
        else {

            return school_start;
        }
    }

    Date endTime(final Date school_end)
    {
        if (_end_time != null) {

            return _end_time.getTime();
        }
        else {

            return school_end;
        }
    }

    private Calendar _date;
    private String _description;
    private Calendar _start_time = null;
    private Calendar _end_time = null;
}

package uk.org.chinkara.schoolday.model.structure;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import uk.org.chinkara.schoolday.model.SchoolCalendar;


public class LessonTimes {

    LessonTimes(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "period": {
                    String next = reader.nextString();
                    if (next.toLowerCase().equals("break")) {
                        _period = 0;
                    }
                    else {
                        _period = Integer.valueOf(next);
                    }
                }
                break;
                case "start": {
                    _start_time = SchoolCalendar.time_format.parse(reader.nextString());
                }
                break;
                case "end": {
                    _end_time = SchoolCalendar.time_format.parse(reader.nextString());
                }
                break;
                default:
                    throw new JSONException("Bad tag in LessonTimes object");
            }
        }
        reader.endObject();

        if ((_start_time == null) || (_end_time == null) || (_period == -1)) {

            throw new JSONException("Required fields missing in LessonTimes");
        }
    }

    public boolean isBreak()
    {
        return (_period == 0);
    }

    public int period()
    {
        return _period;
    }

    public Date startTime()
    {
        return _start_time;
    }

    public Date endTime()
    {
        return _end_time;
    }

    @Override
    public String toString() {

        String res = "LessonTimes(";
        if (isBreak()) {

            res += "Break";
        }
        else {

            res += String.valueOf(_period);
        }
        res += ", Start: " + SchoolCalendar.time_format.format(_start_time) + ", End: " +
                SchoolCalendar.time_format.format(_end_time) + ")";
        return res;
    }

    private int _period = -1;
    private Date _start_time;
    private Date _end_time;
}

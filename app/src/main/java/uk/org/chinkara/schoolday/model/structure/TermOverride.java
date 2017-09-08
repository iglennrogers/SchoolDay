package uk.org.chinkara.schoolday.model.structure;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import uk.org.chinkara.schoolday.model.SchoolCalendar;


class TermOverride {

    TermOverride(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "date": {
                    Date date = SchoolCalendar.date_format.parse(reader.nextString());
                    _date = GregorianCalendar.getInstance();
                    _date.setTime(date);
                }
                break;
                case "plan": {
                    _plan = reader.nextString();
                }
                break;
                default:
                    throw new JSONException("Bad tag in TermOverride object");
            }
        }
        reader.endObject();

        if ((_date == null) || _plan.isEmpty()) {

            throw new JSONException("Need both date and plan for TermOverride");
        }
    }

    String plan()
    {
        return _plan;
    }

    boolean isOverridden(final Calendar date)
    {
        return date.get(Calendar.DAY_OF_YEAR) == _date.get(Calendar.DAY_OF_YEAR);
    }

    private Calendar _date;
    private String _plan;
}

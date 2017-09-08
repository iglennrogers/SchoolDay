package uk.org.chinkara.schoolday.model.personal;

import android.util.JsonReader;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class PersonalLessons {

    public PersonalLessons(JsonReader reader) throws IOException, JSONException, ParseException
    {
        reader.beginObject();
        while (reader.hasNext()) {

            String tag = reader.nextName().toLowerCase();
            switch (tag) {

                case "timetable":
                    readTimetable(reader);
                    break;
                case "override":
                    readOverride(reader);
                    break;
                case "clubs":
                    readClubs(reader);
                    break;
                default:
                    break;
            }
        }
        reader.endObject();
    }

    private void readTimetable(JsonReader reader) throws IOException, ParseException, JSONException
    {
        reader.beginArray();
        while (reader.hasNext()) {

            WeekLessons plan = new WeekLessons(reader);
            int weekno = plan.weekno();
            _timetable.put(weekno, plan);
        }
        reader.endArray();
    }

    private void readOverride(JsonReader reader) throws IOException, ParseException, JSONException
    {
        reader.beginArray();
        while (reader.hasNext()) {

            ClassOverride ovr = new ClassOverride(reader);
            _lessonoverrides.add(ovr);
        }
        reader.endArray();
    }

    private void readClubs(JsonReader reader) throws IOException, ParseException, JSONException
    {
        reader.beginArray();
        while (reader.hasNext()) {

            reader.beginObject();
            while (reader.hasNext()) {

                reader.nextName();
                reader.nextString();
            }
            reader.endObject();
        }
        reader.endArray();
    }

    public DayLessons timetable(int weekno, int dayno) {

        WeekLessons week = _timetable.get(weekno);
        if (week != null) {

            Log.d("Personal", week.toString());
            DayLessons day = week.lessons(dayno);
            if (day != null) {

                Log.d("Personal", day.toString());
                return day;
            }
        }
        Log.d("Personal", "Unknown timetable for Week:" + ((Integer)weekno).toString() +
                ", Day:" + ((Integer)dayno).toString());
        return null;
    }

    private final List<ClassOverride> _lessonoverrides = new ArrayList<>();
    private final SparseArray<WeekLessons> _timetable = new SparseArray<>();
}

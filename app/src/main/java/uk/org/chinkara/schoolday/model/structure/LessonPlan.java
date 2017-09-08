package uk.org.chinkara.schoolday.model.structure;

import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class LessonPlan {

    LessonPlan(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "plan":
                    _plan = reader.nextString();
                    break;
                case "timetable":
                    readTimetable(reader);
                    break;
                default:
                    throw new JSONException("Bad tag in LessonPlan object");
            }
        }
        reader.endObject();
    }

    String plan() {

        return _plan;
    }

    public List<LessonTimes> timetable() {

        return _lessons;
    }

    @Override
    public String toString() {

        String res = "LessonPlan(Plan: " + _plan;
        for (LessonTimes l: _lessons) {

            res += ", " + l.toString();
        }
        res += ")";
        return res;
    }

    private void readTimetable(JsonReader reader) throws IOException, ParseException, JSONException {

        reader.beginArray();
        while (reader.hasNext()) {

            LessonTimes lesson = new LessonTimes(reader);
            _lessons.add(lesson);
        }
        reader.endArray();

    }

    private String _plan;
    private final List<LessonTimes> _lessons = new ArrayList<>();
}

package uk.org.chinkara.schoolday.model.structure;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.chinkara.schoolday.model.SchoolCalendar;


public class TermStructure {

    public TermStructure(JsonReader reader) throws IOException, JSONException, ParseException
    {
        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName().toLowerCase()) {

                case "timezone":
                    _timezone = reader.nextString();
                    break;
                case "weekcycle":
                    _weekcycle = reader.nextInt();
                    break;
                case "subjects":
                    readSubjects(reader);
                    break;
                case "terms":
                    reader.beginArray();
                    while (reader.hasNext()) {

                        Term term = new Term(reader);
                        _terms.add(term);
                    }
                    reader.endArray();
                    break;
                case "override":
                    reader.beginArray();
                    while (reader.hasNext()) {

                        TermOverride ovr = new TermOverride(reader);
                        _termoverrides.add(ovr);
                    }
                    reader.endArray();
                    break;
                case "lessons":
                    reader.beginArray();
                    while (reader.hasNext()) {

                        LessonPlan plan = new LessonPlan(reader);
                        _lessonplans.put(plan.plan(), plan);
                    }
                    reader.endArray();
                    break;
                default:
                    throw new JSONException("Bad tag in Structure Object");
            }
        }
        reader.endObject();
    }

    public boolean isTermTime(Calendar date) {

        Term term = getTerm(date);
        return (term != null);
    }

    public LessonPlan getLessonPlan(Calendar date) {

        Term term = getTerm(date);
        if (term == null) {

            throw new RuntimeException("Date not within term-time");
        }

        Log.d("Structure", term.toString() + ", Date: " + SchoolCalendar.date_format.format(date.getTime()));
        String planId = planId(date);
        LessonPlan plan = _lessonplans.get(planId);
        Log.d("Structure", plan.toString());
        return _lessonplans.get(planId);
    }

    public int weekNumber(final Calendar date) {

        Term term = getTerm(date);
        if (term == null) {

            throw new RuntimeException("Date not within term-time");
        }
        return term.weekNumber(date, _weekcycle);
    }

    public String subject(String id) {

        if (_subjects.containsKey(id)) {

            return _subjects.get(id);
        }
        return id;
    }

    public String timezone() {

        return _timezone;
    }

    private String planId(Calendar date) {

        for (TermOverride ovr: _termoverrides) {

            if (ovr.isOverridden(date)) {

                return ovr.plan();
            }
        }
        return "default";
    }

    private Term getTerm(Calendar date) {

        for (Term term: _terms) {

            if (term.isDateWithin(date)) {

                return term;
            }
        }
        return null;
    }

    private void readSubjects(JsonReader reader) throws IOException {

        reader.beginArray();
        while (reader.hasNext()) {

            reader.beginObject();
            String id = reader.nextName();
            String name = reader.nextString();
            _subjects.put(id, name);
            reader.endObject();
        }
        reader.endArray();
    }

    private String _timezone = "Europe/London";
    private int _weekcycle = 2;
    private final Map<String,String> _subjects = new HashMap<>();
    private final List<Term> _terms = new ArrayList<>();
    private final List<TermOverride> _termoverrides = new ArrayList<>();
    private final Map<String,LessonPlan> _lessonplans = new HashMap<>();
}

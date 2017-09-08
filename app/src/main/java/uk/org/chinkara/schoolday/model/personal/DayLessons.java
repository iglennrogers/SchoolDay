package uk.org.chinkara.schoolday.model.personal;

import android.support.compat.BuildConfig;
import android.util.JsonReader;
import android.util.SparseArray;

import java.io.IOException;
import java.util.Locale;


public class DayLessons {

    DayLessons(JsonReader reader, int dayno) throws IOException {

        _dayno = dayno;

        reader.beginArray();
        while (reader.hasNext()) {

            LessonDetail lesson = new LessonDetail(reader);
            _lessons.put(lesson.period(), lesson);
        }
        reader.endArray();
    }

    public LessonDetail lesson(int period) {

        if (BuildConfig.DEBUG && period == 0) {

            throw new RuntimeException("period=0 on lesson call");
        }

        return _lessons.get(period);
    }

    @Override
    public String toString() {

        String res = String.format(Locale.getDefault(), "DayLessons(Day: %d", _dayno);
        for (int i = 0; i < _lessons.size(); i++) {

            res += ", " + _lessons.valueAt(i).toString();
        }
        res += ")";
        return res;
    }

    private int _dayno;
    private final SparseArray<LessonDetail> _lessons = new SparseArray<>();
}

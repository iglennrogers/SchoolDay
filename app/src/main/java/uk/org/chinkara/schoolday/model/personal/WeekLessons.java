package uk.org.chinkara.schoolday.model.personal;

import android.util.JsonReader;
import android.util.SparseArray;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


class WeekLessons {

    WeekLessons(JsonReader reader) throws IOException {

        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName()) {

                case "week": {
                    _weekno = reader.nextInt();
                }
                break;
                case "monday": {
                    readDayLessons(reader, Calendar.MONDAY);
                }
                break;
                case "tuesday": {
                    readDayLessons(reader, Calendar.TUESDAY);
                }
                break;
                case "wednesday": {
                    readDayLessons(reader, Calendar.WEDNESDAY);
                }
                break;
                case "thursday": {
                    readDayLessons(reader, Calendar.THURSDAY);
                }
                break;
                case "friday": {
                    readDayLessons(reader, Calendar.FRIDAY);
                }
                break;
                default:
                    throw new IllegalArgumentException("Unexpected day of week");
            }
        }
        reader.endObject();
    }

    private void readDayLessons(JsonReader reader, int dayno) throws IOException {

        DayLessons lessons = new DayLessons(reader, dayno);
        _lessons.put(dayno, lessons);
    }

    int weekno() {

        return _weekno;
    }

    DayLessons lessons(int dayno) {

        return _lessons.get(dayno);
    }

    @Override
    public String toString() {

        return String.format(Locale.getDefault(), "WeekLessons(Week: %d)", _weekno);
    }

    private int _weekno;
    private SparseArray<DayLessons> _lessons = new SparseArray<>();
}

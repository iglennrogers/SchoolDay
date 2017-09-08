package uk.org.chinkara.schoolday.model.personal;

import android.util.JsonReader;

import java.io.IOException;
import java.util.Locale;


public class LessonDetail {

    LessonDetail(JsonReader reader) throws IOException {

        _span = 1;
        reader.beginObject();
        while (reader.hasNext()) {

            switch (reader.nextName()) {

                case "period": {
                    _period = reader.nextInt();
                }
                break;
                case "subject": {
                    _subject = reader.nextString();
                }
                break;
                case "room": {
                    _room = reader.nextString();
                }
                break;
                case "teacher": {
                    _teacher = reader.nextString();
                }
                break;
                case "span": {
                    _span = reader.nextInt();
                }
                break;
                default:
                    throw new IllegalArgumentException("Unexpected tag in LessonDetail");
            }
        }
        reader.endObject();
    }

    int period() {

        return _period;
    }

    public String subject() {

        return _subject;
    }

    public String room() {

        return _room;
    }

    public String teacher() {

        return _teacher;
    }

    public int span() {

        return _span;
    }

    @Override
    public String toString() {

        return String.format(Locale.getDefault(),
                "LessonDetail(Period: %d, subject: %s, Room: %s, Teacher: %s, Span: %d)",
                _period, _subject, _room, _teacher, _span);
    }

    private int _period;
    private String _subject;
    private String _room;
    private String _teacher;
    private int _span;
}

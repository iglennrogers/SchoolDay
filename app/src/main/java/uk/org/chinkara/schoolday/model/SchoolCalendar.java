package uk.org.chinkara.schoolday.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import uk.org.chinkara.schoolday.model.personal.DayLessons;
import uk.org.chinkara.schoolday.model.personal.LessonDetail;
import uk.org.chinkara.schoolday.model.personal.PersonalLessons;
import uk.org.chinkara.schoolday.model.structure.LessonPlan;
import uk.org.chinkara.schoolday.model.structure.LessonTimes;
import uk.org.chinkara.schoolday.model.structure.TermStructure;


public class SchoolCalendar {

    public static final SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    public static final SimpleDateFormat day_format = new SimpleDateFormat("EEEE", Locale.UK);
    public static final SimpleDateFormat time_format = new SimpleDateFormat("HH:mm", Locale.UK);

    public SchoolCalendar(Context context)
    {
        try {
            copyFile(context, "schoolday.json");
            FileReader json = new FileReader(context.getFilesDir() + "/schoolday.json");
            JsonReader reader = new JsonReader(json);

            reader.beginObject();
            while (reader.hasNext()) {

                String tag = reader.nextName().toLowerCase();
                switch (tag) {

                    case "structure":
                        _structure = new TermStructure(reader);
                        break;
                    case "personal":
                        _personal = new PersonalLessons(reader);
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();

        File destParentDir = context.getFilesDir();
        if (!destParentDir.exists()) {

            if (!destParentDir.mkdirs()) {

                throw new IOException("Cannot create asset folder");
            }
        }

        InputStream in = assetManager.open(fileName);

        File destFile = new File(context.getFilesDir() + File.separator + fileName);
        OutputStream out = new FileOutputStream(destFile);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

    public List<TimetableItem> createTimetable(Calendar date) {

        List<TimetableItem> items = new ArrayList<>();
        if (!isTermTime(date)) {

            Log.d("Schoolday", "Holidays, Date: " + SchoolCalendar.date_format.format(date.getTime()));
            items.add(new TimetableItem().new Holiday());
            return items;
        }

        List<LessonTimes> plan = lessonPlan(date).timetable();
        if (plan.size() == 0) {

            // inset day
            items.add(new TimetableItem().new InsetDay());
            return items;
        }
        DayLessons lessons = lessonDetail(date);
        if (lessons == null) {

            // probably the weekend
            items.add(new TimetableItem().new Weekend());
            return items;
        }

        TimetableItem lastitem = null;
        for (LessonTimes period: plan) {

            if (period.isBreak()) {

                items.add(new TimetableItem(this, null, period));
            }
            else if (lastitem != null) {

                lastitem.extendEnd(period);
                lastitem = null;
            }
            else {

                int p = period.period();
                LessonDetail detail = lessons.lesson(p);
                if (detail == null) {

                    Log.d("Schoolday", String.format(Locale.getDefault(), "Unknown period: %d", p));
                    throw new RuntimeException("Unknown period in timetable");
                }
                TimetableItem item = new TimetableItem(this, detail, period);
                items.add(item);
                if (detail.span() == 2) {

                    lastitem = item;
                }
            }
        }

        return items;
    }

    String subject(String id) {

        return _structure.subject(id);
    }

    public TimeZone timezone() {

        return TimeZone.getTimeZone(_structure.timezone());
    }

    private DayLessons lessonDetail(Calendar date) {

        int weekno = _structure.weekNumber(date);
        int dayno = date.get(Calendar.DAY_OF_WEEK);

        return _personal.timetable(weekno, dayno);
    }

    private boolean isTermTime(Calendar date) {

        return _structure.isTermTime(date);
    }

    private LessonPlan lessonPlan(Calendar date) {

        return _structure.getLessonPlan(date);
    }

    private TermStructure _structure;
    private PersonalLessons _personal;
}

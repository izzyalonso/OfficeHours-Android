package org.tndata.officehours.database;


import android.provider.BaseColumns;

/**
 * OfficeHours' database contract
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class DatabaseContract{
    public static abstract class CourseEntry implements BaseColumns{
        static final String TABLE = "Course";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String NAME = "name";
        static final String TIME = "time";
        static final String CODE = "code";
    }

    public static abstract class ScheduleEntry implements BaseColumns{
        static final String TABLE = "Schedule";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String NAME = "name";
        static final String TIME = "time";
        static final String INSTRUCTOR = "instructor";
    }
}

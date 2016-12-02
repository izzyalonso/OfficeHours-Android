package org.tndata.officehours.database;


import android.provider.BaseColumns;

/**
 * OfficeHours' database contract
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
class DatabaseContract{
    static abstract class CourseEntry implements BaseColumns{
        static final String TABLE = "Course";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String CODE = "code";
        static final String NAME = "name";
        static final String MEETING_TIME = "meeting_time";
        static final String EXPIRATION_DATE = "expiration_date";
        static final String ACCESS_CODE = "access_code";
        static final String INSTRUCTOR_NAME = "instructor_name";
    }
}

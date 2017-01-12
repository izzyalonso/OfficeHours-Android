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
        static final String NAME = "name";
        static final String LOCATION = "location";
        static final String MEETING_TIME = "meeting_time";
        static final String ACCESS_CODE = "access_code";
    }

    static abstract class PersonEntry implements BaseColumns{
        static final String TABLE = "Person";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String COURSE_ID = "course_id";
        static final String NAME = "name";
        static final String AVATAR = "avatar";
        static final String IS_INSTRUCTOR = "is_instructor";
    }
}

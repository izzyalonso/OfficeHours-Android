package org.tndata.officehours.database;


import android.provider.BaseColumns;

/**
 * OfficeHours' database contract
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
class DatabaseContract{
    static abstract class InstructorCourseEntry implements BaseColumns{
        static final String TABLE = "InstructorCourse";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String NAME = "name";
        static final String TIME = "time";
        static final String CODE = "code";
    }

    static abstract class StudentCourseEntry implements BaseColumns{
        static final String TABLE = "StudentCourse";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String NAME = "name";
        static final String TIME = "time";
        static final String INSTRUCTOR = "instructor";
    }
}

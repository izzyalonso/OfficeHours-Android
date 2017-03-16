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
        static final String LAST_MESSAGE = "last_message";
    }

    static abstract class MessageEntry implements BaseColumns{
        static final String TABLE = "Message";
        //TODO table for queue

        static final String ID = _ID;
        static final String SENDER_ID = "sender_id";
        static final String RECIPIENT_ID = "recipient_id";
        static final String TEXT = "text";
        static final String TIMESTAMP = "timestamp";
        static final String IS_READ = "read";
    }

    static abstract class QuestionEntry implements BaseColumns{
        static final String TABLE = "Questions";

        static final String ID = _ID;
        static final String CLOUD_ID = "cloud_id";
        static final String TITLE = "title";
        static final String CONTENT = "content";
        static final String VOTES = "votes";
        static final String KEYWORDS = "keywords";
    }
}

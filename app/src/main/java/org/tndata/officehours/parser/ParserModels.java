package org.tndata.officehours.parser;


import org.tndata.officehours.model.Course;
import org.tndata.officehours.model.ResultSet;

import java.util.List;

/**
 * This class contains the specifications of the API responses that do not match the model
 * to let Gson take care of everything.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public final class ParserModels{
    private abstract class ListResultSet implements ResultSet{
        public int count;
        public String previous;
        public String next;
    }

    public class CourseList extends ListResultSet{
        public List<Course> results;
    }

    private ParserModels(){

    }
}

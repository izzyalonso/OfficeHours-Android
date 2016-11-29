package org.tndata.officehours.model;


import android.os.Parcel;

/**
 * Model for a course from the perspective of the instructor.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class InstructorCourse extends Course{
    private String code;


    /**
     * Constructor.
     *
     * @param name the name of the course.
     * @param time the time of the course.
     * @param code the code of the course.
     */
    public InstructorCourse(String name, String time, String code){
        super(name, time);
        this.code = code;
    }

    /**
     * Constructor.
     *
     * @param id the id of the course.
     * @param name the name of the course.
     * @param time the time of the course.
     * @param code the code of the course.
     */
    public InstructorCourse(long id, String name, String time, String code){
        super(id, name, time);
        this.code = code;
    }

    /**
     * Code getter.
     *
     * @return the code to access the course.
     */
    public String getCode(){
        return code;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeString(code);
    }

    public static final Creator<InstructorCourse> CREATOR = new Creator<InstructorCourse>(){
        @Override
        public InstructorCourse createFromParcel(Parcel src){
            return new InstructorCourse(src);
        }

        @Override
        public InstructorCourse[] newArray(int count){
            return new InstructorCourse[count];
        }
    };

    private InstructorCourse(Parcel src){
        super(src);
        code = src.readString();
    }
}

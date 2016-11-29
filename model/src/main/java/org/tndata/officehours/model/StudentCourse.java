package org.tndata.officehours.model;


import android.os.Parcel;

/**
 * Model for a course from the student's perspective.
 *
 * @author Ismael Alonso
 * @author 1.0.0
 */
public class StudentCourse extends Course{
    private String instructor;


    /**
     * Constructor.
     *
     * @param name the name of the course.
     * @param time the time of the course.
     * @param instructor the instructor of the course.
     */
    public StudentCourse(String name, String time, String instructor){
        super(name, time);
        this.instructor = instructor;
    }

    /**
     * Constructor.
     *
     * @param id the id of the course.
     * @param name the name of the course.
     * @param time the time of the course.
     * @param instructor the instructor of the course.
     */
    public StudentCourse(long id, String name, String time, String instructor){
        super(id, name, time);
        this.instructor = instructor;
    }

    /**
     * Instructor getter.
     *
     * @return the name of the instructor.
     */
    public String getInstructor(){
        return instructor;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);
        parcel.writeString(instructor);
    }

    public static final Creator<StudentCourse> CREATOR = new Creator<StudentCourse>(){
        @Override
        public StudentCourse createFromParcel(Parcel src){
            return new StudentCourse(src);
        }

        @Override
        public StudentCourse[] newArray(int count){
            return new StudentCourse[count];
        }
    };

    private StudentCourse(Parcel src){
        super(src);
        instructor = src.readString();
    }
}

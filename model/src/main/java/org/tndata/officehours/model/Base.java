package org.tndata.officehours.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Base class for all the model.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public abstract class Base implements Parcelable, ResultSet{
    @SerializedName("id")
    private long id;


    /**
     * Constructor.
     *
     * @param id the id of the object.
     */
    public Base(long id){
        this.id = id;
    }

    /**
     * Id setter.
     *
     * @param id the new id of the instance.
     */
    protected void setId(long id){
        this.id = id;
    }

    /**
     * Id getter.
     *
     * @return the id of the instance
     */
    public long getId(){
        return id;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeLong(id);
    }

    /**
     * Parcelable constructor.
     *
     * @param src the source parcel.
     */
    protected Base(Parcel src){
        id = src.readLong();
    }
}

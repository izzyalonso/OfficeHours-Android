package org.tndata.officehours.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;


/**
 * Model for the new representation of office hours.
 *
 * @author Ismael Alonso
 */
public class OfficeHours extends Base{
    @SerializedName("Monday")
    private String monday[][];
    @SerializedName("Tuesday")
    private String tuesday[][];
    @SerializedName("Wednesday")
    private String wednesday[][];
    @SerializedName("Thursday")
    private String thursday[][];
    @SerializedName("Friday")
    private String friday[][];
    @SerializedName("Saturday")
    private String saturday[][];
    @SerializedName("Sunday")
    private String sunday[][];


    public OfficeHours(){
        super(-1);
    }

    public boolean hasMondayHours(){
        return monday != null;
    }

    public boolean hasTuesdayHours(){
        return monday != null;
    }

    public boolean hasWednesdayHours(){
        return monday != null;
    }

    public boolean hasThursdayHours(){
        return monday != null;
    }

    public boolean hasFridayHours(){
        return monday != null;
    }

    public boolean hasSaturdayHours(){
        return monday != null;
    }

    public boolean hasSundayHours(){
        return monday != null;
    }

    private String getHoursFor(String[][] day){
        String result = "";
        for (int i = 0; i < day.length; i++){
            result += day[i][0] + "-" + day[i][1];
            if (day.length < 2){
                result += ", ";
                if (i == day.length-2){
                    result += "and ";
                }
            }
            else if (day.length == 2 && i == 0){
                result += " and ";
            }
        }
        return result;
    }

    public String getMondayHours(){
        if (hasMondayHours()){
            return getHoursFor(monday);
        }
        return "";
    }

    public String getTuesdayHours(){
        if (hasTuesdayHours()){
            return getHoursFor(tuesday);
        }
        return "";
    }

    public String getWednesdayHours(){
        if (hasWednesdayHours()){
            return getHoursFor(wednesday);
        }
        return "";
    }

    public String getThursdayHours(){
        if (hasThursdayHours()){
            return getHoursFor(thursday);
        }
        return "";
    }

    public String getFridayHours(){
        if (hasFridayHours()){
            return getHoursFor(friday);
        }
        return "";
    }

    public String getSaturdayHours(){
        if (hasSaturdayHours()){
            return getHoursFor(saturday);
        }
        return "";
    }

    public String getSundayHours(){
        if (hasSundayHours()){
            return getHoursFor(sunday);
        }
        return "";
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags){
        super.writeToParcel(parcel, flags);

        if (hasMondayHours()){
            parcel.writeInt(monday.length);
            for (String[] slot:monday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasTuesdayHours()){
            parcel.writeInt(tuesday.length);
            for (String[] slot:tuesday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasWednesdayHours()){
            parcel.writeInt(wednesday.length);
            for (String[] slot:wednesday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasThursdayHours()){
            parcel.writeInt(thursday.length);
            for (String[] slot:thursday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasFridayHours()){
            parcel.writeInt(friday.length);
            for (String[] slot:friday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSaturdayHours()){
            parcel.writeInt(saturday.length);
            for (String[] slot:saturday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSundayHours()){
            parcel.writeInt(sunday.length);
            for (String[] slot:sunday){
                parcel.writeString(slot[0]);
                parcel.writeString(slot[1]);
            }
        }
        else{
            parcel.writeInt(0);
        }
    }

    public static final Creator<OfficeHours> CREATOR = new Creator<OfficeHours>(){
        @Override
        public OfficeHours createFromParcel(Parcel source){
            return new OfficeHours(source);
        }

        @Override
        public OfficeHours[] newArray(int size){
            return new OfficeHours[size];
        }
    };

    public OfficeHours(Parcel src){
        super(src);

        int slots = src.readInt();
        monday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            monday[i][0] = src.readString();
            monday[i][1] = src.readString();
        }

        slots = src.readInt();
        tuesday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            tuesday[i][0] = src.readString();
            tuesday[i][1] = src.readString();
        }

        slots = src.readInt();
        wednesday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            wednesday[i][0] = src.readString();
            wednesday[i][1] = src.readString();
        }

        slots = src.readInt();
        thursday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            thursday[i][0] = src.readString();
            thursday[i][1] = src.readString();
        }

        slots = src.readInt();
        friday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            friday[i][0] = src.readString();
            friday[i][1] = src.readString();
        }

        slots = src.readInt();
        saturday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            saturday[i][0] = src.readString();
            saturday[i][1] = src.readString();
        }

        slots = src.readInt();
        sunday = new String[slots][2];
        for (int i = 0; i < slots; i++){
            sunday[i][0] = src.readString();
            sunday[i][1] = src.readString();
        }
    }
}

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
    private TimeSlot monday[];
    @SerializedName("Tuesday")
    private TimeSlot tuesday[];
    @SerializedName("Wednesday")
    private TimeSlot wednesday[];
    @SerializedName("Thursday")
    private TimeSlot thursday[];
    @SerializedName("Friday")
    private TimeSlot friday[];
    @SerializedName("Saturday")
    private TimeSlot saturday[];
    @SerializedName("Sunday")
    private TimeSlot sunday[];


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

    private String getHoursFor(TimeSlot[] day){
        String result = "";
        for (int i = 0; i < day.length; i++){
            result += day[i].from + "-" + day[i].to;
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
            for (TimeSlot slot:monday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasTuesdayHours()){
            parcel.writeInt(tuesday.length);
            for (TimeSlot slot:tuesday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasWednesdayHours()){
            parcel.writeInt(wednesday.length);
            for (TimeSlot slot:wednesday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasThursdayHours()){
            parcel.writeInt(thursday.length);
            for (TimeSlot slot:thursday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasFridayHours()){
            parcel.writeInt(friday.length);
            for (TimeSlot slot:friday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSaturdayHours()){
            parcel.writeInt(saturday.length);
            for (TimeSlot slot:saturday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSundayHours()){
            parcel.writeInt(sunday.length);
            for (TimeSlot slot:sunday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
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
        monday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            monday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        tuesday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            tuesday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        wednesday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            wednesday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        thursday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            thursday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        friday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            friday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        saturday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            saturday[i] = new TimeSlot(src.readString(), src.readString());
        }

        slots = src.readInt();
        sunday = new TimeSlot[slots];
        for (int i = 0; i < slots; i++){
            sunday[i] = new TimeSlot(src.readString(), src.readString());
        }
    }


    /**
     * Model for a from-to dataset
     *
     * @author Ismael Alonso
     */
    private class TimeSlot{
        private String from;
        private String to;


        private TimeSlot(String from, String to){
            this.from = from;
            this.to = to;
        }
    }
}

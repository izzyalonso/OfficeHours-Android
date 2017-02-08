package org.tndata.officehours.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Model for the new representation of office hours.
 *
 * @author Ismael Alonso
 */
public class OfficeHours extends Base{
    @SerializedName("Monday")
    private List<TimeSlot> monday;
    @SerializedName("Tuesday")
    private List<TimeSlot> tuesday;
    @SerializedName("Wednesday")
    private List<TimeSlot> wednesday;
    @SerializedName("Thursday")
    private List<TimeSlot> thursday;
    @SerializedName("Friday")
    private List<TimeSlot> friday;
    @SerializedName("Saturday")
    private List<TimeSlot> saturday;
    @SerializedName("Sunday")
    private List<TimeSlot> sunday;


    public OfficeHours(){
        super(-1);
    }

    public boolean hasMondayHours(){
        return monday != null;
    }

    public boolean hasTuesdayHours(){
        return tuesday != null;
    }

    public boolean hasWednesdayHours(){
        return wednesday != null;
    }

    public boolean hasThursdayHours(){
        return thursday != null;
    }

    public boolean hasFridayHours(){
        return friday != null;
    }

    public boolean hasSaturdayHours(){
        return saturday != null;
    }

    public boolean hasSundayHours(){
        return sunday != null;
    }

    private String getHoursFor(List<TimeSlot> day){
        String result = "";
        for (int i = 0; i < day.size(); i++){
            result += day.get(i).from + "-" + day.get(i).to;
            if (day.size() < 2){
                result += ", ";
                if (i == day.size()-2){
                    result += "and ";
                }
            }
            else if (day.size() == 2 && i == 0){
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
            parcel.writeInt(monday.size());
            for (TimeSlot slot:monday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasTuesdayHours()){
            parcel.writeInt(tuesday.size());
            for (TimeSlot slot:tuesday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasWednesdayHours()){
            parcel.writeInt(wednesday.size());
            for (TimeSlot slot:wednesday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasThursdayHours()){
            parcel.writeInt(thursday.size());
            for (TimeSlot slot:thursday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasFridayHours()){
            parcel.writeInt(friday.size());
            for (TimeSlot slot:friday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSaturdayHours()){
            parcel.writeInt(saturday.size());
            for (TimeSlot slot:saturday){
                parcel.writeString(slot.from);
                parcel.writeString(slot.to);
            }
        }
        else{
            parcel.writeInt(0);
        }

        if (hasSundayHours()){
            parcel.writeInt(sunday.size());
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
        if (slots > 0){
            monday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                monday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();
        if (slots > 0){
            tuesday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                tuesday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();
        if (slots > 0){
            wednesday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                wednesday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();

        if (slots > 0){
            thursday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                thursday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();
        if (slots > 0){
            friday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                friday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();
        if (slots > 0){
            saturday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                saturday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }

        slots = src.readInt();
        if (slots > 0){
            sunday = new ArrayList<>(slots);
            for (int i = 0; i < slots; i++){
                sunday.add(new TimeSlot(src.readString(), src.readString()));
            }
        }
    }


    /**
     * Model for a from-to data set
     *
     * @author Ismael Alonso
     */
    public class TimeSlot{
        private String from;
        private String to;


        private TimeSlot(String from, String to){
            this.from = from;
            this.to = to;
        }
    }
}

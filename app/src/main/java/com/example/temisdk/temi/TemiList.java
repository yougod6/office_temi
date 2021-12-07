package com.example.temisdk.temi;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TemiList {
    public String location;
    public Float distance1;
    public Float distance2;
    public Float distance3;

    public TemiList(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TemiList(String location, Float distance1, Float distance2, Float distance3) {
        this.location = location;
        this.distance1 = distance1;
        this.distance2 = distance2;
        this.distance3 = distance3;
    }
    public String getLocation(){return location;}
    public Float getDistance1(){return distance1;}
    public Float getDistance2(){return distance2;}
    public Float getDistance3(){return distance3;}
    public void setLocation(String location){this.location=location;}
    public void setDistacne1(Float distance){this.distance1=distance;}
    public void setDistacne2(Float distance){this.distance2=distance;}
    public void setDistacne3(Float distance){this.distance3=distance;}


}

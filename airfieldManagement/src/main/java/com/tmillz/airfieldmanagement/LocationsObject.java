package com.tmillz.airfieldmanagement;

import android.support.annotation.Keep;

/**
 * Created by Terrance Miller on 8/4/17.
 */


@Keep
public class LocationsObject {

    public String date;
    public String id_by;
    public Double lat;
    public Double lng;
    public String notes;
    public String title;

    public LocationsObject() {
    }

    public LocationsObject(String date, String id_by, Double lat, Double lng,
                           String notes, String title) {
        this.date = date;
        this.id_by = id_by;
        this.lat = lat;
        this.lng = lng;
        this.notes = notes;
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setId_by(String id_by) {
        this.id_by = id_by;
    }
    public  void setLat(Double lat) {
        this.lat = lat;
    }
    public void setLng(Double lng) {
        this.lng = lng;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}

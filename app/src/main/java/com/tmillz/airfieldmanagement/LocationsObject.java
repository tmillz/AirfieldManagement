package com.tmillz.airfieldmanagement;

/**
 * Created by Terrance Miller on 8/4/17.
 */

class LocationsObject {

    private String date;
    private String id_by;
    private Double lat;
    private Double lng;
    private String notes;
    private String title;

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

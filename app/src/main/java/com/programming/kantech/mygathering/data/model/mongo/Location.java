package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Location implements Serializable
{

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_short")
    @Expose
    private String countryShort;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("state_prov")
    @Expose
    private String stateProv;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("location")
    @Expose
    private Location_Coords location_coords;
    private final static long serialVersionUID = 152059590157352799L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Location() {
    }

    /**
     *
     * @param stateProv
     * @param countryShort
     * @param postalCode
     * @param location_coords
     * @param name
     * @param locality
     * @param formattedAddress
     * @param notes
     * @param country
     */
    public Location(String country, String countryShort, String formattedAddress, String locality, String postalCode, String stateProv, String name, String notes, Location_Coords location_coords) {
        super();
        this.country = country;
        this.countryShort = countryShort;
        this.formattedAddress = formattedAddress;
        this.locality = locality;
        this.postalCode = postalCode;
        this.stateProv = stateProv;
        this.name = name;
        this.notes = notes;
        this.location_coords = location_coords;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryShort() {
        return countryShort;
    }

    public void setCountryShort(String countryShort) {
        this.countryShort = countryShort;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateProv() {
        return stateProv;
    }

    public void setStateProv(String stateProv) {
        this.stateProv = stateProv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Location_Coords getLocation() {
        return location_coords;
    }

    public void setLocation(Location_Coords location) {
        this.location_coords = location;
    }

    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", countryShort='" + countryShort + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", locality='" + locality + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", stateProv='" + stateProv + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", location_coords=" + location_coords +
                '}';
    }
}

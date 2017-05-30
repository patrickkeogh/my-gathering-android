package com.programming.kantech.mygathering.data.model.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by patrick keogh on 2017-05-19.
 */

public class Banner implements Serializable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("cropped")
    @Expose
    private Cropped cropped;
    @SerializedName("converted")
    @Expose
    private Boolean converted;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("isWriteable")
    @Expose
    private Boolean isWriteable;
    private final static long serialVersionUID = -6098062075851892125L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Banner() {
    }

    /**
     *
     * @param id
     * @param mimetype
     * @param client
     * @param converted
     * @param cropped
     * @param filename
     * @param isWriteable
     * @param url
     * @param size
     */
    public Banner(String url, String filename, String mimetype, Integer size, Cropped cropped, Boolean converted, Integer id, String client, Boolean isWriteable) {
        super();
        this.url = url;
        this.filename = filename;
        this.mimetype = mimetype;
        this.size = size;
        this.cropped = cropped;
        this.converted = converted;
        this.id = id;
        this.client = client;
        this.isWriteable = isWriteable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Cropped getCropped() {
        return cropped;
    }

    public void setCropped(Cropped cropped) {
        this.cropped = cropped;
    }

    public Boolean getConverted() {
        return converted;
    }

    public void setConverted(Boolean converted) {
        this.converted = converted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Boolean getIsWriteable() {
        return isWriteable;
    }

    public void setIsWriteable(Boolean isWriteable) {
        this.isWriteable = isWriteable;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "url='" + url + '\'' +
                ", filename='" + filename + '\'' +
                ", mimetype='" + mimetype + '\'' +
                ", size=" + size +
                ", cropped=" + cropped +
                ", converted=" + converted +
                ", id=" + id +
                ", client='" + client + '\'' +
                ", isWriteable=" + isWriteable +
                '}';
    }
}

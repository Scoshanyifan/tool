package com.kunbu.common.util.tool.sql.mongo.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-04-02 16:56
 **/
@Document(collection = "device_location")
public class DeviceLocation {

    @Id
    private String id;
    private String deviceSn;
    /**
     * 坐标
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DeviceLocation{" +
                "id='" + id + '\'' +
                ", deviceSn='" + deviceSn + '\'' +
                ", location=" + location +
                '}';
    }
}

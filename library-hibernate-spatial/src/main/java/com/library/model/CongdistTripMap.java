package com.library.model;

import com.vividsolutions.jts.geom.MultiLineString;

public final class CongdistTripMap {

  private int id;
  private String agencyId;
  private String agencyId_def;
  private String tripId; 
  private String routeId;
  private String congdistId;
  private String serviceId;
  private double length;
  private int tlength;
  private int stopscount;
  private MultiLineString shape;
  private String uid;
  private CongDist congdist;
    
      
  public CongdistTripMap() {	
}

public CongdistTripMap(CongdistTripMap a) {
    this.id = a.id;
    this.agencyId = a.agencyId;
    this.agencyId_def = a.agencyId_def;
    this.tripId = a.tripId;
    this.routeId = a.routeId;
    this.congdistId = a.congdistId;
    this.length = a.length;
    this.tlength = a.tlength;
    this.shape = a.shape;
    this.uid = a.uid;
    this.congdist = a.congdist;
  }

  public int getId() {
    return id;
  }
  
  public void setId(int id) {
	    this.id = id;
	  }
  
  public String getTripId() {
	    return tripId;
	  }
  
  public void setTripId(String tripId) {
	    this.tripId= tripId;
	  }
  
  public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
  
  public String getagencyId() {
    return agencyId;
  }
  
  public void setagencyId(String agencyId) {
    this.agencyId = agencyId;
  }
  
  public String getagencyId_def() {
	    return agencyId;
	  }
	  
  public void setagencyId_def(String agencyId_def) {
    this.agencyId_def = agencyId_def;
  }
  
  public String getCongdistId() {
    return congdistId;
  }

  public void setCongdistId(String congdistId) {
    this.congdistId = congdistId;
  }
  
  public String getServiceId() {
	    return serviceId;
	  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }
  
  public int getStopscount() {
	    return stopscount;
	  }

  public void setStopscount(int stopscount) {
    this.stopscount = stopscount;
  }
  
  public double getLength() {
	    return length;
	  }

 public void setLength(double length) {
  this.length = length;
 	 }
 
 public int getTlength() {
	    return tlength;
	  }

public void setTlength(int tlength) {
this.tlength = tlength;
	 }

  public MultiLineString getShape(){
	  return shape;
  }
  
  public void setShape(MultiLineString shape){
	  this.shape = shape;
  }
  
  public String getUid(){
	  return uid;
  }
  
  public void setUid(String uid){
	  this.uid = uid;
  }
  
  public CongDist getCongdist() {
	return congdist;
}

public void setCongdist(CongDist congdist) {
	this.congdist = congdist;
}

public String toString() {
    return "<Trip " + this.tripId + "Intersects with congressional district "+ this.congdistId+ ">";
  }
}

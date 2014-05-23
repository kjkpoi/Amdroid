package net.daum.android.map.openapi.sampleapp;

/**
 * @author lacti
 *
 */
public class Treasure { 
    
    int id; 
    String name; 
    String serial;
    String comment;
    String owner;
    String date;
    String level;
    String size;
    double latitude;
    double longitude;
    byte[] image;
    
    public Treasure(){ 
   
    } 
    public Treasure(int id, String name, String serial, String comment, String owner, String date, String level, String size, double latitude, double longitude, byte[] image){ 
        this.id = id; 
        this.name = name; 
        this.serial = serial;
        this.comment = comment;
        this.owner = owner;
        this.date = date;
        this.level = level;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    } 
   
    public Treasure(String name, String serial, String comment, String owner, String date, String level, String size, double latitude, double longitude, byte[] image){ 
        this.name = name; 
        this.serial = serial;
        this.comment = comment;
        this.owner = owner;
        this.date = date;
        this.level = level;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    } 
     
    public int getID(){ 
        return this.id; 
    } 
   
    public void setID(int id){ 
        this.id = id; 
    } 
   
    public String getName(){ 
        return this.name; 
    } 
   
    public void setName(String name){ 
        this.name = name; 
    } 
    
    public String getComment(){
    	return this.comment;
    }
    
    public void setComment(String comment){
    	this.comment = comment;
    }
    
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	} 

	
}
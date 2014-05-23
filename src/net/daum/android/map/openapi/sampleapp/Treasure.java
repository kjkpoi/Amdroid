package net.daum.android.map.openapi.sampleapp;

public class Treasure { 
    
    int id; 
    String name; 
    String comment;
    String phone_number;
    double latitude;
    double longitude;
    byte[] image;
    
    public Treasure(){ 
   
    } 
    public Treasure(int id, String name, String comment, String phone_number, double latitude, double longitude, byte[] image){ 
        this.id = id; 
        this.name = name; 
        this.comment = comment;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    } 
   
    public Treasure(String name, String comment, String phone_number, double latitude, double longitude, byte[] image){ 
        this.name = name; 
        this.comment = comment;
        this.phone_number = phone_number; 
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
   
    public String getPhoneNumber(){ 
        return this.phone_number; 
    } 
   
    public void setPhoneNumber(String phone_number){ 
        this.phone_number = phone_number; 
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
package chuk.lime.act01;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Contact implements Serializable {
	
	long id;
	String appContactsId;
	String name;
	String number;
	String email;
	
	// Constructors
	public Contact(){}
	public Contact(String id, String name, String num, String email){
		this.appContactsId = id;
		this.name = name;
		this.number = num;
		this.email = email;
	}
	
	// Setters
	public void setId(long id){
		this.id = id;
	}
	
	public void setAppId(String appId){
		this.appContactsId = appId;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
}

package com.bobotosoft.economy101;

public class Movement {
	public int id;
    public String description;
    public double amount;
    public int date;
    public String currency;

    public Movement(String d, double a, int date, String c){
    	this.description = d;
    	this.amount = a;    	
    	this.date = date;
    	this.currency = c;
    }
    
    public void setId(int i)
    {
    	this.id = i;
    }

    public String getDescription()
    {
        return this.description;
    }
    
    public double getAmount()
    {
    	return this.amount;
    }

    public int getDate()
    {
        return this.date;
    }
    
    public String getCurrency()
    {
    	return this.currency;
    }
    
	public int getId() {
		return this.id;
	}
}

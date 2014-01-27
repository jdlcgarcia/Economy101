package com.bobotosoft.economy101;

public class Movement {
	public int id;
    public String description;
    public double amount;

    public Movement(String d, double a){
    	this.description = d;
    	this.amount = a;    	
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

	public int getId() {
		return this.id;
	}
}

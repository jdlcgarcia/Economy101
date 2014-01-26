package com.bobotosoft.economy101;

public class Movement {
    public String description;
    public double amount;

    public Movement(String d, double a){
    	this.description = d;
    	this.amount = a;    	
    }

    public String getDescription()
    {
        return this.description;
    }
    
    public double getAmount()
    {
    	return this.amount;
    }
}

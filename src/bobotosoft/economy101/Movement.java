package bobotosoft.economy101;

public class Movement {
	public int id;
    public String description;
    public double amount;
    public long date;
    public String currency;

    public Movement(String d, double a, long date, String c){
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

    public long getDate()
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
	
	@Override
	public String toString() {
		String converted = null;
		converted = "[id] = "+String.valueOf(this.id);
		converted += "; [description] = "+this.description;
		converted += "; [amount] = "+String.valueOf(this.amount);
		converted += "; [date] = "+String.valueOf(this.date);
		converted += "; [currency] = "+this.currency;
		
		
		
		return converted;
		
	}
}

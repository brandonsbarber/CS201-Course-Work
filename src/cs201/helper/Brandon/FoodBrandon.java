package cs201.helper.Brandon;
//Change for commit
public class FoodBrandon
{
	private String type;
	private int cookingTime, amount, minimum, orderAmount;
	private double price;
	private boolean ordered = false;
	
	public FoodBrandon(String type, int startingAmount, int minimumAmount, int orderAmount, int cookingTime, double price)
	{
		this.type = type;
		this.cookingTime = cookingTime;
		this.amount = startingAmount;
		this.minimum = minimumAmount;
		this.orderAmount = orderAmount;
		this.price = price;
	}
	
	public void setAmount(int amount)
	{
		this.amount= amount;
		ordered = false;
	}
	
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	public void useFood()
	{
		amount--;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public String getType()
	{
		return type;
	}
	
	public boolean isLow()
	{
		return minimum >= amount;
	}
	
	public int getMinimum()
	{
		return minimum;
	}
	
	public int getOrderAmount()
	{
		return orderAmount;
	}
	
	public int getCookingTime()
	{
		return cookingTime;
	}
	
	public double getPrice()
	{
		return price;
	}
	
	public boolean isGone()
	{
		return amount == 0;
	}
	
	public void order()
	{
		ordered = true;
	}
	
	public boolean ordered()
	{
		return ordered ;
	}
}

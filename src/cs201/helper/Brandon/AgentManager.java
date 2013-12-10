package cs201.helper.Brandon;

import java.util.ArrayList;

import cs201.roles.Role;
import cs201.roles.restaurantRoles.Brandon.RestaurantCashierRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCookRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantCustomerRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantHostRoleBrandon;
import cs201.roles.restaurantRoles.Brandon.RestaurantWaiterRoleBrandon;

/**
 * A class for the management of a restaurant full of agents.
 * Currently allows for only four types of agents and prohibits there being multiple Hosts or Cooks.
 * @author Brandon
 *
 */
public class AgentManager
{
	private static final int HOST_INDEX = 0;
	private static final int COOK_INDEX = 1;
	private static final int WAITER_INDEX = 2;
	private static final int CUSTOMER_INDEX = 3;
	private static final int CASHIER_INDEX = 4;
	private static final int MARKET_INDEX = 5;
	
	private static final int MAX_INDEX = 5;
	
	private ArrayList<ArrayList<Role>> agents;
	
	/**
	 * Constructs a new AgentManager for use in storing agents.
	 */
	public AgentManager()
	{
		agents = new ArrayList<ArrayList<Role>>();
		for(int i = 0; i <= MAX_INDEX; i++)
		{
			agents.add(new ArrayList<Role>());
		}
	}
	
	/**
	 * Adds the given HostAgent into the structure
	 * @raises IllegalArgumentException if there is already one HostAgent
	 * @param agent the agent to be added
	 */
	public void addAgent(RestaurantHostRoleBrandon agent)
	{
		if(agents.get(HOST_INDEX).size() != 0)
		{
			throw new IllegalArgumentException("There can only be one Host!");
		}
		agents.get(HOST_INDEX).add(agent);
	}
	
	/**
	 * Adds the given CookAgent into the structure
	 * @raises IllegalArgumentException if there is already one CookAgent
	 * @param agent the agent to be added
	 */
	public void addAgent(RestaurantCookRoleBrandon agent)
	{
		if(agents.get(COOK_INDEX).size() != 0)
		{
			throw new IllegalArgumentException("There can only be one cook!");
		}
		agents.get(COOK_INDEX).add(agent);
	}
	
	/**
	 * Adds the given CashierAgent into the structure
	 * @raises IllegalArgumentException if there is already one CashierAgent
	 * @param agent the agent to be added
	 */
	public void addAgent(RestaurantCashierRoleBrandon agent)
	{
		if(agents.get(CASHIER_INDEX).size() != 0)
		{
			throw new IllegalArgumentException("There can only be one cashier!");
		}
		agents.get(CASHIER_INDEX).add(agent);
	}
	
	/**
	 * Adds the given CustomerAgent into the structure
	 * @param agent the agent to be added
	 */
	public void addAgent(RestaurantCustomerRoleBrandon agent)
	{
		agents.get(CUSTOMER_INDEX).add(agent);
	}
	
	/**
	 * Adds the given WaiterAgent into the structure
	 * @param agent the agent to be added
	 */
	public void addAgent(RestaurantWaiterRoleBrandon agent)
	{
		agents.get(WAITER_INDEX).add(agent);
	}
	
	/**
	 * Gets the current HostAgent for the restaurant. In this version, there should only be the one.
	 * @raises IndexOutOfBoundsException if there is no host previously added
	 * @return the current HostAgent of the restaurant
	 */
	public RestaurantHostRoleBrandon getHost()
	{
		return (RestaurantHostRoleBrandon)agents.get(HOST_INDEX).get(0);
	}
	
	/**
	 * Gets the current CookAgent for the restaurant. In this version, there should only be the one.
	 * @raises IndexOutOfBoundsException if there is no cook previously added
	 * @return the current CookAgent of the restaurant
	 */
	public RestaurantCookRoleBrandon getCook()
	{
		return (RestaurantCookRoleBrandon)agents.get(COOK_INDEX).get(0);
	}
	
	/**
	 * Gets the current CashierAgent for the restaurant. In this version, there should only be the one.
	 * @raises IndexOutOfBoundsException if there is no cashier previously added
	 * @return the current CashierAgent of the restaurant
	 */
	public RestaurantCashierRoleBrandon getCashier()
	{
		return (RestaurantCashierRoleBrandon)agents.get(CASHIER_INDEX).get(0);
	}
	
	
	
	/**
	 * Finds the first Customer to be added that has the given name.
	 * Error occurs if two customers have the same name.
	 * @param name the name to search for
	 * @return the CustomerAgent to whom the name refers.
	 */
	public RestaurantCustomerRoleBrandon findCustomer(String name)
	{
		for(Role cust : agents.get(CUSTOMER_INDEX))
		{
			RestaurantCustomerRoleBrandon customer = (RestaurantCustomerRoleBrandon)cust;
			if(customer.getName().equals(name))
			{
				return customer;
			}
		}
		return null;
	}
	
	/**
	 * Finds the first Waiter to be added that has the given name.
	 * Error occurs if two waiters have the same name.
	 * @param name the name to search for
	 * @return the WaiterAgent to whom the name refers.
	 */
	public RestaurantWaiterRoleBrandon findWaiter(String name)
	{
		for(Role w : agents.get(WAITER_INDEX))
		{
			RestaurantWaiterRoleBrandon waiter = (RestaurantWaiterRoleBrandon)w;
			if(waiter.getName().equals(name))
			{
				return waiter;
			}
		}
		return null;
	}
	
	/**
	 * Pauses all agents that AgentManager manages
	 */
	public void pauseAgents()
	{
		for(ArrayList<Role> subList : agents)
		{
			for(Role agent : subList)
			{
				//agent.pauseThread();
			}
		}
	}

	/**
	 * Unpauses all agents that AgentManager manages
	 */
	public void resumeAgents() 
	{
		for(ArrayList<Role> subList : agents)
		{
			for(Role agent : subList)
			{
				//agent.resumeThread();
			}
		}
	}

}

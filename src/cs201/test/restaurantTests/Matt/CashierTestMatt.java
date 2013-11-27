package cs201.test.restaurantTests.Matt;

import junit.framework.TestCase;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt;
import cs201.roles.restaurantRoles.Matt.RestaurantCashierRoleMatt.CheckState;
import cs201.test.mock.Matt.MockCustomerMatt;
import cs201.test.mock.Matt.MockHostMatt;
import cs201.test.mock.Matt.MockWaiterMatt;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * 
 *
 * @author Matthew Pohlmann
 */
public class CashierTestMatt extends TestCase
{
        //these are instantiated for each test separately via the setUp() method.
        RestaurantCashierRoleMatt cashier;
        MockWaiterMatt waiter;
        MockCustomerMatt customer;
        MockHostMatt host;
        //MockMarketMatt market;
        //MockMarketMatt market2;
        
        
        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception {
        	System.out.println("----------------------------------------");
        	System.out.println("Setup:");
        	
        	super.setUp();                
            cashier = new RestaurantCashierRoleMatt(null);                
            customer = new MockCustomerMatt("mockcustomer");                
            waiter = new MockWaiterMatt("mockwaiter");
            host = new MockHostMatt("mockhost");
            cashier.host = host;
            //market = new MockMarketMatt("mockmarket1");
            //market2 = new MockMarketMatt("mockmarket2");
            
            System.out.println("----------------------------------------");
        } 
        
        /**
         * This tests the cashier for its normative interaction with a Market (receiving a bill and paying it in full)
         */
        /*public void testNormativeOneMarket() {
        	System.out.println("testNormativeOneMarket:\n");
        	
        	double amount = 25.00;
        	
        	//check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
            
            //step 1 of the test
            cashier.msgPayBillFromMarket(market, amount);
            
            //check postconditions of step 1
            assertEquals("Cashier should have 1 bill in it after receiving msgPayBillFromMarket from Market. It doesn't.", cashier.checks.size(), 1);
            
            assertEquals("The 1 bill in Cashier should be of type market.", cashier.checks.get(0).type, CheckType.market);
            
            assertEquals("The Cashier's only bill should have a market reference equal to the Market in this test.", cashier.checks.get(0).market, market);
            
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            assertEquals("MockMarket should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                                            + market.log.toString(), 1, market.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Market's money should equal the amount it told the Cashier to pay initially.", market.money, amount);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteAnAction());
        }*/
        
        /**
         * This tests the cashier for its interaction with two Markets (receiving two bills from two markets at different times, to be paid in full)
         */
        /*public void testTwoMarketsWithTwoBillsPart1() {
        	System.out.println("testTwoMarketsWithTwoBillsPart1:\n");
        	
        	double amount = 25.00;
        	
        	//check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
            
            //step 1 of the test
            cashier.msgPayBillFromMarket(market, amount);
            
            //check postconditions of step 1
            assertEquals("Cashier should have 1 bill in it after receiving msgPayBillFromMarket from Market mockmarket1. It doesn't.", cashier.checks.size(), 1);
            
            assertEquals("The 1 bill in Cashier should be of type market.", cashier.checks.get(0).type, CheckType.market);
            
            assertEquals("The Cashier's only bill should have a market reference equal to Market mockmarket1.", cashier.checks.get(0).market, market);
            
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            assertEquals("Market mockmarket1 should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                                            + market.log.toString(), 1, market.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Market's money should equal the amount it told the Cashier to pay initially.", market.money, amount);
        	
        	//check preconditions to step 2
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
            
            //step 2 of the test
            cashier.msgPayBillFromMarket(market2, amount);
            
            //check postconditions of step 2
            assertEquals("Cashier should have 1 bill in it after receiving msgPayBillFromMarket from Market mockmarket2. It doesn't.", cashier.checks.size(), 1);
            
            assertEquals("The 1 bill in Cashier should be of type market.", cashier.checks.get(0).type, CheckType.market);
            
            assertEquals("The Cashier's only bill should have a market reference equal to Market mockmarket2.", cashier.checks.get(0).market, market2);
            
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            assertEquals("Market mockmarket2 should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                                            + market2.log.toString(), 1, market2.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Market's money should equal the amount it told the Cashier to pay initially.", market2.money, amount);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteAnAction());
        }*/
        
        /**
         * This tests the cashier for its interaction with two Markets (receiving two bills from two markets simultaneously, to be paid in full)
         */
        /*public void testTwoMarketsWithTwoBillsPart2() {
        	System.out.println("testTwoMarketsWithTwoBillsPart2:\n");
        	
        	double amount = 25.00;
        	double amount2 = 10.00;
        	
        	//check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
            
            //step 1 of the test
            cashier.msgPayBillFromMarket(market, amount);
            cashier.msgPayBillFromMarket(market2, amount2);
            
            //check postconditions of step 1
            assertEquals("Cashier should have 2 bills in it after receiving msgPayBillFromMarket from Market mockmarket1 and mockmarket2. It doesn't.", cashier.checks.size(), 2);
            
            assertEquals("The first bill in Cashier should be of type market.", cashier.checks.get(0).type, CheckType.market);
            
            assertEquals("The second bill in Cashier should be of type market.", cashier.checks.get(1).type, CheckType.market);
            
            assertEquals("The first bill in Cashier should be of amount " + amount, cashier.checks.get(0).amount, amount);
            
            assertEquals("The second bill in Cashier should be of amount " + amount2, cashier.checks.get(1).amount, amount2);
            
            assertEquals("The Cashier's first bill should have a market reference equal to Market mockmarket1.", cashier.checks.get(0).market, market);
            
            assertEquals("The Cashier's second bill should have a market reference equal to Market mockmarket2.", cashier.checks.get(1).market, market2);
            
            //step 2 of the test
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            //check postconditions of step 2
            assertEquals("Market mockmarket1 should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                                            + market.log.toString(), 1, market.log.size());
            
            assertEquals("Cashier checks list should have 1 check in it after running scheduler. It doesn't.", cashier.checks.size(), 1);
            
            assertEquals("Market mockmarket1's money should equal the amount it told the Cashier to pay initially.", market.money, amount);
            
            //step 3 of the test
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            //check postconditions of step 3
            assertEquals("Market mockmarket2 should have 1 item after the Cashier's scheduler is called the second time. Instead, the MockMarket's event log reads: "
                                            + market.log.toString(), 1, market2.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler the second time. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Market mockmarket2's money should equal the amount it told the Cashier to pay initially.", market2.money, amount2);
           
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteAnAction());
        }*/
         
        /**
         * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
         */
        public void testNormativeOneCustomer()
        {
        	System.out.println("testNormativeOneCustomer:\n");
        	
    		customer.cashier = cashier;
            String choice = "Steak";
            double price = 12;
            double customerMoney = 20;
            
            //check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);                
            
            //step 1 of the test
            cashier.msgComputeCheck(waiter, customer, choice); // send message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("Cashier should have 1 bill in it after receiving msgComputeCheck from Waiter. It doesn't.", cashier.checks.size(), 1);
            
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            assertEquals("MockWaiter should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 1, waiter.log.size());
            
            assertEquals("MockCustomer should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                            + waiter.log.toString(), 1, customer.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            //step 2 of the test
            cashier.msgPayCheck(customer, customerMoney, price); // from the customer
            
            //check postconditions for step 2 / preconditions for step 3
            assertTrue("Cashier's checks list should contain a check with state == customerPaying. It doesn't.",
                            cashier.checks.get(0).state == CheckState.customerPaying);

            assertTrue("Cashier's checks list should contain a check of price = $12.00. It contains something else instead: $" 
                            + cashier.checks.get(0).amount, cashier.checks.get(0).amount == price);
            
            assertTrue("Cashier's check list should contain a check with the right customer in it. It doesn't.", 
                                    cashier.checks.get(0).customer == customer);
            
            
            //step 3
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayCheck), but didn't.", 
                                    cashier.pickAndExecuteAnAction());
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"msgHereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer mockcustomer: Received msgHereIsYourChange from Cashier. Change = " + (customerMoney - price)));
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                            cashier.pickAndExecuteAnAction());
        } 
        
        /**
         * This tests the cashier's interaction with a customer who cannot pay his bill in full
         */
        public void testOneCustomerCannotPayInFull()
        {
        	System.out.println("testOneCustomerCannotPayInFull:\n");
        	
    		customer.cashier = cashier;
            String choice = "Steak";
            double price = 12;
            double customerMoney = 10;
            
            //check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);                
            
            //step 1 of the test
            cashier.msgComputeCheck(waiter, customer, choice); // send message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("Cashier should have 1 bill in it after receiving msgComputeCheck from Waiter. It doesn't.", cashier.checks.size(), 1);
            
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            assertEquals("MockWaiter should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 1, waiter.log.size());
            
            assertEquals("MockCustomer should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                            + waiter.log.toString(), 1, customer.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            //step 2 of the test
            cashier.msgPayCheck(customer, customerMoney, price); // from the customer
            
            //check postconditions for step 2 / preconditions for step 3
            assertTrue("Cashier's checks list should contain a check with state == customerPaying. It doesn't.",
                            cashier.checks.get(0).state == CheckState.customerPaying);

            assertTrue("Cashier's checks list should contain a check of price = $12.00. It contains something else instead: $" 
                            + cashier.checks.get(0).amount, cashier.checks.get(0).amount == price);
            
            assertTrue("Cashier's check list should contain a check with the right customer in it. It doesn't.", 
                                    cashier.checks.get(0).customer == customer);
            
            
            //step 3
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayCheck), but didn't.", 
                                    cashier.pickAndExecuteAnAction());
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"msgHereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer mockcustomer: Received msgHereIsYourChange from Cashier. Change = " + 0));
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Host's log should contain 1 item pertaining to the Customer being banned from the restaurant.", host.log.size(), 1);
            
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                            cashier.pickAndExecuteAnAction());
        }
        
        /**
         * This tests the cashier for its interaction with a Customer and Market at the same time (receiving a bill from a market, to be paid in full and a check from a Customer being paid in full)
         */
        /*public void testNormativeOneMarketOneCustomer() {
        	System.out.println("testNormativeOneMarketOneCustomer:\n");
        	
        	double marketAmount = 25.00;
        	customer.cashier = cashier;
            String choice = "Steak";
            double price = 12;
            double customerMoney = 20;
        	
        	//check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
            
            //step 1 of the test
            cashier.msgPayBillFromMarket(market, marketAmount);
            cashier.msgComputeCheck(waiter, customer, choice);
            
            //check postconditions of step 1
            assertEquals("Cashier should have 2 bills in it after receiving msgPayBillFromMarket from Market mockmarket1 and msgComputeCheck from Waiter. It doesn't.", cashier.checks.size(), 2);
            
            assertEquals("The first bill in Cashier should be of type market.", cashier.checks.get(0).type, CheckType.market);
            
            assertEquals("The second bill in Cashier should be of type restaurant.", cashier.checks.get(1).type, CheckType.restaurant);
            
            assertEquals("The first bill in Cashier should be of amount " + marketAmount, cashier.checks.get(0).amount, marketAmount);
                        
            assertEquals("The Cashier's first bill should have a market reference equal to Market mockmarket1.", cashier.checks.get(0).market, market);
            
            assertEquals("The Cashier's second bill should have a customer reference equal to Customer mockcustomer.", cashier.checks.get(1).customer, customer);
            
            //step 2 of the test
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            //check postconditions of step 2
            assertEquals("MockWaiter should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                    + waiter.log.toString(), 1, waiter.log.size());

            assertEquals("MockCustomer should have 1 item after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                    + waiter.log.toString(), 1, customer.log.size());
            
            assertEquals("Cashier checks list should have 1 check in it after running scheduler. It doesn't.", cashier.checks.size(), 1);
            
            //step 3
            cashier.msgPayCheck(customer, customerMoney, price); // from the customer
            
            //check postconditions for step 3 / preconditions for step 4
            assertTrue("Cashier's checks list should contain a check with state == customerPaying. It doesn't.",
                            cashier.checks.get(1).state == CheckState.customerPaying);

            assertTrue("Cashier's checks list should contain a check of price = $12.00. It contains something else instead: $" 
                            + cashier.checks.get(1).amount, cashier.checks.get(1).amount == price);
            
            assertTrue("Cashier's check list should contain a check with the right customer in it. It doesn't.", 
                                    cashier.checks.get(1).customer == customer);
            
            //step 4
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            //check postconditions for step 4 / preconditions for step 5
            assertTrue("MockCustomer should have logged an event for receiving \"msgHereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Customer mockcustomer: Received msgHereIsYourChange from Cashier. Change = " + (customerMoney - price)));
            
            assertEquals("Cashier checks list should have 1 check in it after running scheduler. It doesn't.", cashier.checks.size(), 1);
            
            //step 5
            assertTrue("Cashier's scheduler should have returned true because he processed a Check, but didn't.", cashier.pickAndExecuteAnAction());
            
            //check postconditions for step 5
            assertEquals("Market mockmarket1 should have 1 item after the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                                            + market.log.toString(), 1, market.log.size());
            
            assertEquals("Cashier checks list should have 0 checks in it after running scheduler. It doesn't.", cashier.checks.size(), 0);
            
            assertEquals("Market mockmarket1's money should equal the amount it told the Cashier to pay initially.", market.money, marketAmount);
           
            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                    cashier.pickAndExecuteAnAction());
        }*/
}
team21 README
======

SimCity201 Project Repository for Team 21

##V1 Submission
###Work Load
 + **Matthew Pohlmann:**
   + PersonAgent
   + Role Base Class
   + Structure Base Class
   + CityTime Helper Class
   + CityDirectory Helper Class
   + StructurePanel Base Class
   + Restaurant
     + Restaurant Structure Base Class
     + Restaurant Cashier Role Base Class
     + Restaurant Cook Role Base Class
     + Restaurant Customer Role Base Class
     + Restaurant Host Role Base Class
     + Restaurant Waiter Role Base Class
   + Personal Restaurant Integration
     + RestaurantMatt Structure (and Gui)
     + Restaurant Cashier Role Matt (and Gui)
     + Restaurant Cook Role Matt (and Gui)
     + Restaurant Customer Role Matt (and Gui)
     + Restaurant Host Role Matt (and Gui)
     + Restaurant Waiter Role Matt Base Class (and Gui)
       + Restaurant Waiter Role Matt Normal (A normal waiter)
       + Restaurant Waiter Role Matt Stand (Uses the rotating stand)
     + Restaurant Rotating Stand (and Gui)
   + Initial Wiki setup
   + Initial Package/Directory setup
 + **Brandon Barber:**
   + Team Leader
   + Transit System
     + Vehicle Agent (and Gui)
     + Car (and Gui)
     + Bus (and Gui)
     + TransitRole (and Gui)
     + Bus Stops (and Gui)
     + BFS/logic for making people and vehicles move through the SimCity201
     + Transit Config Panel (Doesn't exist)
     + Unit Testing of Above Code
   + GUI
     + CityPanel (the animation of SimCity201 as a whole)
     + Settings Panel (Holds Config Panels)
       + Config Panel Base Class (Used to display information and configure buildings)
 + **Ben Doherty:**
   + Market
     + Market Structure (and Gui)
     + Market Employee Role (and Gui)
     + Market Manager Role (and Gui)
     + Market Consumer Role (and Gui)
     + Market Config Panel
     + A* Implementation in MarketGUI
     + Unit Testing of Above Code
 + **Skyler Lloyd:**
   + Housing
     + Residence Structure (and Gui)
     + Apartment Complex Structure (and Gui) (Actually just an office for the Landlord Role, but contains a list of Residence that are the "apartments") (Not integrated yet)
     + Landlord Role (and Gui)
     + Renter Role (and Gui) (unused?)
     + Residence Role (and Gui)
     + Residence Config Panel (Doesn't exist)
     + Apartment Complex Config Panel (Doesn't exist)
     + Unit Testing of Above Code
 + **James Lynch:**
   + Bank (None of the below work)
     + Bank Structure (and Gui)
     + Bank Customer Role (and Gui)
     + Bank Guard Role (and Gui)
     + Bank Teller Role (and Gui)
     + Bank Config Panel (Doesn't exist)
     + Unit Testing of Above Code

###Things That Don't Work
 + No Trace Panel
 + No File I/O for configuration
 + No GUI for  dynamic configuration (can't add people/structures during a run of the city)
 + No working Bank
 + Most Structures don't have completed config panels
 + Have not tested ApartmentComplex

###Running Some Scenarios (Things That Do Work)
####Instructions
To compile and run SimCity201 V1:
  + Clone the most recent tag (should be V1.X) into a folder
  + In Eclipse, File -> New -> Java Project
  + Uncheck `Use default location`
  + Click `Browse` and select the repository you downloaded
  + Ensure that `Java SE 7` is selected under JRE
  + Click `Finish`
  + Eclipse will give some errors from the JUnit testing section because JUnit4 is not included in the build path
  + With the current project open, Project -> Properties -> Libraries -> Add Library...
  + Select `JUnit`
  + Click `Next`
  + Under `JUnit library version` select `JUnit 4`
  + Click `Finish`
  + Open `src.cs201.Main.java`
  + Click `Run` to build and run SimCity201 V1
  + **NOTE:** Initially the JFrame that opens will be blank. This is because you need to select a scenario by providing user input in the Console. A simple menu prints that asks what scenario you would like to run. Below are the descriptions of the scenarios you can run.

####Scenarios
 1. **Normative Restaurant:** A normal Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens when all of them have arrived at the Restaurant. At 8:30AM a single Customer comes and a normative Restaurant scenario starts where he orders food and leaves when done. The Restaurant closes at 1:15PM (should be right after the Customer leaves), and all the employees go home.
 2. **Normative Restaurant with Two Customers and Two Waiters:** A normal Waiter and rotating stand Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens when the Host, Cashier, Cook, and at least one Waiter have arrived at the Restaurant. At 8:30AM two Customers come and a normative Restaurant scenario starts where they both order food and leave when done. The Restaurant closes at 1:15PM (should be right after the Customers leave), and all the employees go home.
 3. **Normative Bus:** 
 4. **Normative Walking:**
 5. **Normative Driving:**
 6. **Normative Market-Restaurant Delivery:** A Restaurant Cook and Cashier go to work at 8:00AM. A Market Manager and Employee also go to work at 8:00AM. The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market employee gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring the food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from the cook. It matches, so he gives the cook the delivery and pays the market.
 7. **Normative Residence:** Creates a Residence and a Resident who inhabits that residence. Each morning at 7am, that Resident will eat from his refrigerator to start the day. If he has nothing else to do, he will relax at home until he gets hungry or has something else to do. At 10pm he will go to sleep in his bed.
 8. **Normative Apartment Complex:** Creates a Landlord who lives in a Residence that he owns and a Renter who lives in a Residence that he pays rent on. Every morning at 7am, the Renter will check if he has to pay any rent. The Landlord goes to work at 8am at the ApartmentComplex, where he checks if any of the Renters in his list of properties needs to pay rent. The Renter we've created has to pay his rent on Tuesdays, so the Landlord will notify him of his rent due on Monday. Otherwise, both will act as regular residents in their homes.










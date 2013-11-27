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
     + Apartment Complex Structure (and Gui) (Actually just an office for the Landlord Role, but contains a list of Residence that are the "apartments")
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
 + No GUI for dynamic configuration (can't add people/structures during a run of the city)
 + No working Bank
 + Most Structures don't have completed config panels

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
 **Debug Mode:** In order to view details about the CityPanel view (street driving directions, trigger points on buildings, etc.), enter 0, followed by enter, followed by the scenario you want to view. Entering 0 will toggle Debug Mode on or off.
 1. **Normative Restaurant:** A normal Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens when all of them have arrived at the Restaurant. At 8:30AM a single Customer comes and a normative Restaurant scenario starts where he orders food and leaves when done. The Restaurant closes at 1:15PM (should be right after the Customer leaves), and all the employees go home.
 2. **Normative Restaurant with Two Customers and Two Waiters:** A normal Waiter and rotating stand Waiter, Host, Cashier, and Cook all come to work at 8:00AM. The Restaurant opens when the Host, Cashier, Cook, and at least one Waiter have arrived at the Restaurant. At 8:30AM two Customers come and a normative Restaurant scenario starts where they both order food and leave when done. The Restaurant closes at 1:15PM (should be right after the Customers leave), and all the employees go home.
 3. **Normative Bus:** Creates a person, bus stops, and a bus. The bus will go through the stops on its route in sequential order and loops through these stops.When the person reaches the bus stop (determined by proximity to location and destination), the person adds himself to the bus stop's list of waiting customers.This is accessed by the bus when it arrives at the bus stop, and ends up taking the passenger around the circuit.At each bus stop, the person is told that a stop has been reached. When the bus gets to the proper stop, the person gets off and walks the rest of the way.The bus moves from the very beginning.The person moves at 7:00 AM.
 4. **Normative Walking:** Creates a Person who will walk from the market at 100,100 to the restaurant at 475,225 by way of crosswalks and sidewalks. The route that is taken is defined by arrows shown in Debug mode (viewed by typing zero). When the Person reaches his destination, he goes inside the structure there and performs structure actions. He begins his walk at 7:00 AM.
 5. **Normative Driving:** Creates a Person who will drive from the market at 100,100 to the restaurant at 475,225 by way of roads. The person does this by calling a car, who comes to pick up the person. The person gets into the car, which then drives on a path determined by BFS on a movement map (visible in Debug mode), which takes him or her to the parking location of the building. The person then walks to the sidewalk location and is brought inside the building. This starts at 7:00 AM. He currently moves around the block due to the nature of the sidewalks and his movement priorities.
 6. **Normative Market-Restaurant Delivery:** A Restaurant Cook and Cashier go to work at 8:00AM. A Market Manager and Employee also go to work at 8:00AM. The cook's inventory is forced to 0 for Steak, so he orders 25 steaks from the market. The market employee gets the steaks from the shelves, gives them to the manager, who dispatches a delivery truck to bring the food back to the restaurant. The cashier checks to make sure the delivery matches an invoice he has from the cook. It matches, so he gives the cook the delivery and pays the market. The restaurant ends up with negative money which is okay. Eventually what will happen is the restaurant will have to cover for this by withdrawing from its bank account. If it doesn't have enough, it will take out a loan.
 7. **Normative Market:** A Market Manager, Market Employee, and Market Customer all go to a market at 8 AM when it opens. The market has a forced inventory of 10 Pizzas, 5 Burgers, and 15 Fritos, and the Customer orders two Burgers and a Pizza. The Market Manager conveys these orders to the Employee, who goes and pulls them off the shelf. The Employee then brings them to the front, where they are given through the Manager to the Customer. The Customer pays and leaves, and the Employee returns to the back of the Market.
 8. **Normative Residence:** Creates a Residence and a Resident who inhabits that residence. Each morning at 7am, that Resident will eat from his refrigerator to start the day. If he has nothing else to do, he will relax at home until he gets hungry or has something else to do. At 10pm he will go to sleep in his bed.
 9. **Normative Apartment Complex:** Creates a Landlord who lives in a Residence that he owns and a Renter who lives in a Residence that he pays rent on. Every morning at 7am, the Renter will check if he has to pay any rent. The Landlord goes to work at 8am at the ApartmentComplex, where he checks if any of the Renters in his list of properties needs to pay rent. The Renter we've created has to pay his rent on Tuesdays, so the Landlord will notify him of his rent due on Monday. Otherwise, both will act as regular residents in their homes.

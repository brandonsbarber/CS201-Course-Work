team21 README
======

SimCity201 Project Repository for Team 21

##V1 Submission
####Work Load
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

####Things That Don't Work
 + No Trace Panel
 + No File I/O for configuration
 + No dynamic configuration (can't add people/structures during a run of the city)
 + No working Bank
 + Most Structures don't have completed config panels
 + Have not tested ApartmentComplex

####Running Some Scenarios (Things That Do Work)

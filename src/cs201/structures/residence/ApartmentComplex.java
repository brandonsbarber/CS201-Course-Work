package cs201.structures.residence;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.gui.StructurePanel;
import cs201.gui.roles.residence.LandlordGui;
import cs201.gui.roles.residence.ResidentGui;
import cs201.gui.structures.residence.ApartmentComplexAnimationPanel;
import cs201.gui.structures.residence.ResidenceAnimationPanel;
import cs201.helper.CityTime;
import cs201.interfaces.roles.housing.Landlord;
import cs201.roles.Role;
import cs201.roles.housingRoles.LandlordRole;
import cs201.structures.Structure;

public class ApartmentComplex extends Structure {

	LandlordRole landlord;
	List<Residence> apartments;
	
	public ApartmentComplex (int x, int y, int width, int height, int id, StructurePanel p) {
	    super(x, y, width, height, id, p);
	    
	    landlord = new LandlordRole();
	    apartments = null;
	    
	    LandlordGui lGui = new LandlordGui(landlord);
	    landlord.setGui(lGui);
	    panel.addGui(lGui);
	    ((ApartmentComplexAnimationPanel)panel).informLandlord(lGui);
	}
	
	// Methods
	
	public void setLandlord(LandlordRole l) {
	    landlord = l;
	}
	
	public Landlord getLandlord() {
	    return landlord;
	}
	
	public List<Residence> getApartments() {
	    return apartments;
	}
	
	public List<Residence> getVacantApartments() {
	    List<Residence> vacantApartments = new ArrayList<Residence>();
	    for (Residence r:apartments) {
	    	if (r.getResident()==null) {
	    		vacantApartments.add(r);
	    	}
	    }
	    return vacantApartments;
	}
	
	public void addApartment(Residence r) {
		if(apartments==null) {
			apartments = new ArrayList<Residence>();
		}
		apartments.add(r);
	}

	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		if (role==Intention.ResidenceLandLord) {
			return landlord;
		}
		return null;
	}

	@Override
	public void updateTime(CityTime time) {
		// TODO Auto-generated method stub
		
	}

}

package cs201.structures.residence;

import java.util.ArrayList;
import java.util.List;

import cs201.agents.PersonAgent.Intention;
import cs201.interfaces.Landlord;
import cs201.roles.Role;
import cs201.structures.Structure;

public class ApartmentComplex extends Structure {

	Landlord landlord;
	List<Residence> apartments;
	
	public ApartmentComplex (int x, int y, int width, int height) {
	    super(x, y, width, height, 0 ); //ID??
	    landlord = null;
	}
	
	// Methods
	
	public void setLandlord(Landlord l) {
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

	@Override
	public Role getRole(Intention role) {
		// TODO Auto-generated method stub
		return null;
	}

}

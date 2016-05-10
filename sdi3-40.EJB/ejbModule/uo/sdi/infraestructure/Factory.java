package uo.sdi.infraestructure;

import uo.sdi.business.ServicesFactory;
import uo.sdi.business.impl.LocalEJBServicesLocator;
import uo.sdi.persistence.PersistenceFactory;

public class Factory {

	public static ServicesFactory services = new LocalEJBServicesLocator();
	public static PersistenceFactory persistence = new PersistenceFactory();
	
}

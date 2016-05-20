package uo.sdi.rest;

import java.util.HashSet;
import java.util.Set;

import uo.sdi.rest.impl.ApplicationServicesRestImpl;
import uo.sdi.rest.impl.TripServicesRestImpl;
import uo.sdi.rest.impl.UserServicesRestImpl;

public class Application extends javax.ws.rs.core.Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> res = new HashSet<>();
		res.add(ApplicationServicesRestImpl.class);
		res.add(TripServicesRestImpl.class);
		res.add(UserServicesRestImpl.class);
		return res;
	}
}
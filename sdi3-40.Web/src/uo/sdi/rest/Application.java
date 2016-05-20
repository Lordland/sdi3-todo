package uo.sdi.rest;

import java.util.HashSet;
import java.util.Set;

import uo.sdi.rest.impl.Sdi3ServicesRestImpl;

public class Application extends javax.ws.rs.core.Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> res = new HashSet<>();
		res.add(Sdi3ServicesRestImpl.class);
		return res;
	}
}
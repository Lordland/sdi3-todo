package uo.sdi.rest.impl;

import uo.sdi.business.UserService;
import uo.sdi.infraestructure.Factory;
import uo.sdi.model.User;
import uo.sdi.rest.UserServicesRest;

public class UserServicesRestImpl implements UserServicesRest{

	UserService service = Factory.services.getUserService();
	
	@Override
	public User buscarUsuario(String login) {
		return service.buscaUsuario(login);
	}

}

package uo.sdi.client;

import java.util.List;

import uo.sdi.business.ApplicationService;
import uo.sdi.business.UserService;
import uo.sdi.business.impl.RemoteEJBServicesLocator;
import uo.sdi.model.User;

public class Main {

	private static void run() throws Exception {
		UserService service = new RemoteEJBServicesLocator()
		.getUserService();
		List<User> user = service.getUsers();
		System.out.println("Usuarios:");
		for (User u : user) {
			
			System.out.println(u.getName() + " " + u.getSurname() + " " 
			+ u.getEmail() );
		}
	}
	public static void main(String [ ] args) throws Exception{
		run();
	}
}

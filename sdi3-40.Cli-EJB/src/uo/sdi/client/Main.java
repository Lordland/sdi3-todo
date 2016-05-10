package uo.sdi.client;

import java.util.List;

import uo.sdi.business.ApplicationService;
import uo.sdi.business.impl.RemoteEJBServicesLocator;
import uo.sdi.model.Application;

public class Main {

	private static void run() throws Exception {
		ApplicationService service = new RemoteEJBServicesLocator()
		.getApplicationService();
		List<Application> ap = service.getApplication();
		System.out.println("Aplications");
		for (Application a : ap) {
			System.out.println(a.toString());
		}
	}
	public static void main(String [ ] args) throws Exception{
		run();
	}
}

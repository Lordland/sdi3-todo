package uo.sdi.business;

import java.util.List;
import java.util.Map;

import uo.sdi.model.User;

public interface UserService {

	
	public User buscaUsuario(String login);
	
	public void iniciaSesion(User usuario, Map<String ,Object> session);
	
	public void actualizarUsario(User usuario);
	
	public void cerrarSesion(User usuario, Map<String ,Object> session);
	
	public void crearUsuario(User usuario, String comparaPass) throws Exception;

	public List<User> getUsers();
	
}

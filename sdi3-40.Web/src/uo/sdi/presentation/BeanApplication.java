package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import uo.sdi.infraestructure.Factory;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.User;
@ManagedBean(name = "apuntados")
public class BeanApplication implements Serializable {

	private static final long serialVersionUID = -8390132477462648301L;
	@ManagedProperty("#{viajes}")
	BeanViajes bv;
	List<ListaApuntados> listaApuntadosUsuario;

	List<ListaApuntados> listaApuntadosPromotor;
	ListaApuntados apuntado;

	private boolean renderIframeColumn;
 
	public BeanApplication() {
		if (bv == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ELContext contextoEL = context.getELContext();
			Application apli = context.getApplication();
			ExpressionFactory ef = apli.getExpressionFactory();
			ValueExpression ve = ef.createValueExpression(contextoEL,
					"#{viajes}", BeanViajes.class);
			bv = (BeanViajes) ve.getValue(contextoEL);
		}
		listaApuntadosUsuario = new ArrayList<ListaApuntados>();
		listaApuntadosPromotor = new ArrayList<ListaApuntados>();
		apuntado = new ListaApuntados();
	}

	public boolean isRenderIframeColumn() {
		return renderIframeColumn;
	}

	public void setRenderIframeColumn(boolean renderIframeColumn) {
		this.renderIframeColumn = renderIframeColumn;
	}

	public ListaApuntados getApuntado() {
		return apuntado;
	}

	public void setApuntado(ListaApuntados apuntado) {
		this.apuntado = apuntado;
	}

	public List<ListaApuntados> getListaApuntadosUsuario() {
		return listaApuntadosUsuario;
	}

	public void setListaApuntadosUsuario(
			List<ListaApuntados> listaApuntadosUsuario) {
		this.listaApuntadosUsuario = listaApuntadosUsuario;
	}

	public List<ListaApuntados> getListaApuntadosPromotor() {
		return listaApuntadosPromotor;
	}

	public void setListaApuntadosPromotor(
			List<ListaApuntados> listaApuntadosPromotor) {
		this.listaApuntadosPromotor = listaApuntadosPromotor;
	}

	/**
	 * Acepta un usuario en un viaje y disminuye el nº de plazas disponibles
	 */
	public void aceptar() {
		try {
			Seat asiento = Factory.services.getApplicationService()
					.aceptarUsuario(apuntado);
			// Actualizacion interfaz
			bv.listaViaje();
			for (ListaApuntados ap : listaApuntadosPromotor) {
				if (ap.equals(apuntado)) {
					ap.setAsiento(asiento);
					ap.setRelacionViaje();
				}
			}
		} catch (Exception e) {

		}

	}

	/**
	 * Rechaza a un usuario ya aceptado o no en un viaje en concreto
	 */
	public void cancelar() {
		try {
			// Insercion en BD
			Seat asiento = Factory.services.getApplicationService()
					.cancelarUsuario(apuntado);
			// Actualizacion interfaz
			bv.listaViaje();
			for (ListaApuntados ap : listaApuntadosPromotor) {
				if (ap.equals(apuntado)) {
					ap.setAsiento(asiento);
					ap.setRelacionViaje();
				}
			}
		} catch (Exception e) {

		}
	}

	public void apuntarse(User usuario) {

		ListaApuntados ap = new ListaApuntados();
		ap.setViaje(bv.getViaje());
		ap.setUsuario(usuario);
		ap.setRelacionViaje();
		boolean esta = false;
		for (ListaApuntados apunta : listaApuntadosUsuario) {
			if (apunta.getViaje().equals(ap.getViaje())
					&& apunta.getUsuario().equals(ap.getUsuario())) {
				esta = true;
			}
		}
		if (!esta) {
			// Añadido a la BD
			Factory.services.getApplicationService().apuntarseViaje(ap);
			// Actualizacion de la lista
			if (listaApuntadosUsuario.isEmpty()) {
				listaApuntadosUsuario.add(ap);
			} else {
				listaApuntadosUsuario.add(ap);
			}
		}
	}

	/**
	 * Método que desapunta a un usuario de un viaje. Si estuviera ya aceptado, modifica
	 * el viaje incrementando en 1 el nº de plazas disponibles
	 */
	public void desapuntarse() {
		try {
			// Borrado de la BD
			int borrado = Factory.services.getApplicationService().desapuntarseViaje(apuntado);
			// Actualizacion de la lista
			if (borrado > 0) {
				if (listaApuntadosUsuario.contains(apuntado))
					listaApuntadosUsuario.remove(apuntado);
			}
			bv.listaViajeUsuario(apuntado.getUsuario().getId());
		} catch (Exception e) {

		}

	}

	public void listaApuntadosUsuario(User usuario) {
		listaApuntadosUsuario = new ArrayList<ListaApuntados>();
		setListaApuntadosUsuario(Factory.services.getApplicationService().listaApuntadosUsuario(usuario));
	}

	public void listaApuntadosPromotor() {
		listaApuntadosPromotor = new ArrayList<ListaApuntados>();
		setListaApuntadosPromotor(Factory.services.getApplicationService().listaApuntadosPromotor(bv.getViajesPromotor()));
	}

	public String vistaPromotor() {
		listaApuntadosPromotor();
		return "promotor";
	}
}

package sistema_reservas_archivos.servicio;

import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import sistema_reservas_archivos.dominio.Reserva;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class ServicioReservasActualesLista implements IServicioReservasActuales{
	
	private static List<Reserva> reservasActuales = new ArrayList<>();
	private final static int MAX_RESERVAS_POR_DIA = 10;
	public static boolean esUnica;
	
	public void agregarReserva (Reserva reserva) { //agrega una reserva a la lista en memoria
		if (reserva == null) 
			System.out.println("La reserva no puede ser nula.");
		else if(reservaUnica(reserva.getNombreCliente(), reserva.getApellidoCliente(), reserva.getFecha(), reserva.getHora())) {
				reservasActuales.add(reserva);
				System.out.println("Reserva agregada con éxito.");
			}
		else {
				System.out.println("La reserva ya existe.");
			}
	}
	
	public void mostrarReservas() { //muestra las reservas
		System.out.println("Reservas actuales: ");
		if (reservasActuales.isEmpty()) {
			System.out.println("No hay reservas para mostrar.");
		} 
		else {
			reservasActuales.forEach(System.out::println);
	}
}
	
		public void modificarNombreReserva(int idReserva, String nuevoNombreCliente) { //modifica el nombre de un cliente
			for (Reserva r : reservasActuales) {
				if(r.getIdReserva() == idReserva) {
					r.setNombreCliente(nuevoNombreCliente);
		  }
		}
	  }
	
		public void modificarApellidoReserva(int idReserva, String nuevoApellidoCliente) { //modifica el apellido de un cliente		
				for (Reserva r : reservasActuales) {
					if(r.getIdReserva() == idReserva) {
					    r.setApellidoCliente(nuevoApellidoCliente);
		  }
		}
	  }
	
	public void modificarFechaReserva(int idReserva, String nuevaFechaReserva) { //modifica la fecha de una reserva		
			for (Reserva r : reservasActuales) {
				if(r.getIdReserva() == idReserva) {
					r.setFecha(nuevaFechaReserva);
			}
		}
	}

	public void modificarHoraReserva(int idReserva, String nuevaHoraReserva) { //modifica la hora de una reserva
		for (Reserva r : reservasActuales) {
			if(r.getIdReserva() == idReserva) {
				r.setHora(nuevaHoraReserva);
	  }
	}
  }

	public boolean reservaUnica(String nombreCliente, String apellidoCliente, String fechaReserva, String horaReserva) { //devuelve true si la reserva es unica
		esUnica = true;
		for(var r : reservasActuales) {  
		if(r.getNombreCliente().equalsIgnoreCase(nombreCliente) &&
		   r.getApellidoCliente().equalsIgnoreCase(apellidoCliente) && 
		   r.getFecha().equalsIgnoreCase(fechaReserva) &&
		   r.getHora().equalsIgnoreCase(horaReserva)) {
				esUnica = false; //retorna false porque la reserva NO es unica
				return esUnica;
	}
  }
		return true;
 }
	public List<Reserva> getTotalReservas() { //devuelve las reservas actuales
		return reservasActuales;
	}

//	   public List<Reserva>  eliminarReserva(int idReserva) { //elimina una reserva del sistema
//		   if(clientesConReserva.isEmpty())
//			   System.out.println("No existen Reservas actualmente.");
//		   
//		   Iterator<Reserva> iterador = clientesConReserva.iterator();
//		   while (iterador.hasNext()) {
//			   Reserva r = iterador.next();
//			   if(r.getIdReserva() == idReserva) {
//				   iterador.remove();
//				   System.out.println("Reserva con ID " + idReserva +" eliminada.");
//					return clientesConReserva;
//			   }
//		   }
//			  System.out.println("No se encontro la reserva.");
//			  return clientesConReserva;
//			}
   
   public boolean eliminarReserva(int idReserva) {//elimina una reserva del sistema 
	   if(reservasActuales.isEmpty())
		   System.out.println("No existen Reservas actualmente.");
	   
	    boolean removed = reservasActuales.removeIf(r -> r.getIdReserva() == idReserva);
	    if (removed) {
	        System.out.println("Reserva eliminada.");
	    } else {
	        System.out.println("No se encontró la reserva.");
	    }
	    
	    return true;
	}
   
   public boolean disponibilidadReservas(String fecha) { //devuelve true si hay disponibilidad de reservas para una fecha
	   int cont = 0;
		for(Reserva r : reservasActuales) {
			if(r.getFecha().equalsIgnoreCase(fecha))
				cont++;
		}
		return cont < MAX_RESERVAS_POR_DIA;
	}
   
}
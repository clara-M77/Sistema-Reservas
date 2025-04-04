package sistema_reservas_archivos.servicio;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import sistema_reservas_archivos.dominio.Reserva;

public class ServicioReservasArchivos implements IServicioReservasActuales{

	private static final int MAX_RESERVAS_POR_DIA = 5;
	private final String NOMBRE_ARCHIVO = "reservas.txt";
	private static int contadorId = 0;
	private List<Reserva> reservas = new ArrayList<>();
	//Crear la lista de reservas 
	//Constructor
	public ServicioReservasArchivos() {
		//Creamos el archivo si no existe
		var archivo = new File(NOMBRE_ARCHIVO); //objeto que hace referencia al archivo 
		var existe = false;
		try {
			existe = archivo.exists();
			if(existe) {
				this.reservas = obtenerReservas(); //si existe el archivo, entonces cargamos la info de reservas
			}
			else {//si no existe, creamos el archivo
				var salida = new PrintWriter(new FileWriter(archivo));
				salida.close(); //Guarda el archivo en el disco
				//System.out.println("Se ha creado el archivo.");
			}
		} catch(Exception e) {
			System.out.println("Error al crear el archivo. " + e.getMessage());
		}
		//Si el archivo no existe, entonces cargamos algunas reservas iniciales 
		if(!existe) {
			cargarReservasIniciales();
		}
	}
	
	private void cargarReservasIniciales() { //tiene que guardar las reservas en el archivo
		this.agregarReserva(new Reserva("Juan Pablo", "Gimenez", "10/04/2025", "21:00"));
		this.agregarReserva(new Reserva("Lucia", "Lopez", "25/04/2025", "21:30"));
		this.agregarReserva(new Reserva("Lucas", "Gonzalez", "26/04/2025", "20:30"));
	}
	
	private List<Reserva> obtenerReservas() { //lee reservas del archivo
		var reservas = new ArrayList<Reserva>(); //variable temporal
		try {
			List<String> lineas = Files.readAllLines(Paths.get(NOMBRE_ARCHIVO));
			for(String linea : lineas) {
				String[] lineaReserva = linea.split(",");
				var idReserva = lineaReserva[0]; //No se usa
				var nombreCliente = lineaReserva[1];
				var apellidoCliente = lineaReserva[2];
				var fechaReserva = lineaReserva[3];
				var horaReserva = lineaReserva[4];
				var reserva = new Reserva(nombreCliente, apellidoCliente, fechaReserva, horaReserva);
				reservas.add(reserva); //agregamos la reserva leida a la lista
			} 
		}catch(Exception e) {
			System.out.println("Error al leer reservas del archivo. " + e.getMessage());
			e.printStackTrace();
		}
		return reservas;
	}
 	
	@Override
	public void agregarReserva(Reserva reserva) {
			//Agregamos la nueva reserva, 1. a la lista en memoria
			this.reservas.add(reserva);
			//2. guardamos datos de la reserva en el archivo
			this.agregarReservaArchivo(reserva);
	}
	
	private void agregarReservaArchivo(Reserva reserva) { //escribe la reserva en el archivo sin sobreescribir el contenido
		boolean anexar = false;
		var archivo = new File(NOMBRE_ARCHIVO);
		try {
			anexar = archivo.exists();
			var salida = new PrintWriter(new FileWriter(archivo, anexar));
			salida.println(reserva.escribirReserva());
			salida.close(); //se escrib la info en el archivo
		}catch(Exception e) {
			System.out.println("Error al agregar reserva. " + e.getMessage());
		}
	}
	
	@Override
	public void mostrarReservas() {
		System.out.println("** Reservas Actuales **");
		//Mostramos la lista de reservas en el archivo
		var inventarioReservas = "";
		for(var reserva : reservas) {
			inventarioReservas += reserva.toString() + "\n"; 
		}
		System.out.println(inventarioReservas);
		
		if(reservas.isEmpty()) 
			System.out.println("  No hay Reservas para mostrar. ");
	}

	@Override
	public void modificarNombreReserva(int idReserva, String nuevoNombreCliente) { //buscar la linea q quiero modificar 
		 try {
			 reservas = reservas.stream()
		 						.map(reserva -> { //filtramos por ID
		 							if(reserva.getIdReserva() == idReserva) {
		 							  reserva.setNombreCliente(nuevoNombreCliente);
		 							}
		 								return reserva; //mantenemos sin modificar		 							
		 						})
		 						.collect(Collectors.toList()); 
		 //Guardamos los cambios en el archivo
		 guardarReservasEnArchivo(reservas);			 
		 System.out.println("Nombre del cliente actualizado. ");
		 } catch (Exception e) {
			System.out.println("Error al modificar reserva. " + e.getMessage());
			e.printStackTrace();
		}		 
	}
	
	public void modificarApellidoReserva(int idReserva, String nuevoApellidoCliente) {
		//Modifica el apellido del cliente que corresponde al id
		try {
			reservas = reservas.stream() 
					.map(reserva -> { //filtramos por ID
						if(reserva.getIdReserva() == idReserva) {
						  reserva.setApellidoCliente(nuevoApellidoCliente);
						}
							return reserva; //mantenemos sin modificar	 							
					})
					.collect(Collectors.toList()); 
			//Guardamos los cambios en el archivo
		guardarReservasEnArchivo(reservas);
		System.out.println("Apellido del cliente actualizado. ");
		} catch (Exception e) {
			System.out.println("Error al modificar reserva. " + e.getMessage());
			e.printStackTrace();
		}
	}

    public void modificarFechaReserva(int idReserva, String nuevaFechaReserva) {
    	try {
    		reservas = reservas.stream()
					.map(reserva -> { //filtramos por ID
						if(reserva.getIdReserva() == idReserva) {
						  reserva.setFecha(nuevaFechaReserva);
						}
						return reserva;				 							
					})
					.collect(Collectors.toList()); 
			//Guardamos los cambios en el archivo
		guardarReservasEnArchivo(reservas);
		System.out.println("Fecha de reserva actualizada. ");
    	}catch (Exception e) {
    		System.out.println("Error al modificar reserva. " + e.getMessage());
    		e.printStackTrace();
    	}
    }

    public void modificarHoraReserva(int idReserva, String nuevaHoraReserva) {
			try {
				reservas = reservas.stream()
						.map(reserva -> { //filtramos por ID
							if(reserva.getIdReserva() == idReserva) {
							  reserva.setHora(nuevaHoraReserva);
							}
							return reserva;
						})
						.collect(Collectors.toList()); 
				//Guardamos los cambios en el archivo
			guardarReservasEnArchivo(reservas);
			System.out.println("Hora de reserva actualizada. ");
			}catch (Exception e) {
				System.out.println("Error al modificar reserva. " + e.getMessage());
	    		e.printStackTrace();
			}
	}
    
	private void guardarReservasEnArchivo(List<Reserva> reservas) { //Guarda las reservas de la memoria al archivo
		try (PrintWriter salida = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO))) { //crea un archivo
			for(Reserva reserva : reservas) {
				salida.println(reserva.escribirReserva()); //se guarda en el archivo
			}
		}catch(Exception e) {
			System.out.println("Error al guardar reservas en el archivo. " + e.getMessage());
		}
	}
    
	@Override
	public boolean reservaUnica(String nombreCliente, String apellidoCliente, String fechaReserva, String horaReserva) { //no pueden existir dos reservas identicas
		return reservas.stream() 
			   .noneMatch(r -> r.getNombreCliente().equalsIgnoreCase(nombreCliente) &&
			   				   r.getApellidoCliente().equalsIgnoreCase(apellidoCliente) &&
			   				   r.getFecha().equalsIgnoreCase(fechaReserva) &&
			   				   r.getHora().equalsIgnoreCase(horaReserva));
	}
		
	@Override
	public List<Reserva> getTotalReservas() { //retorna la lista de reservas actuales
		return this.reservas;
	}

	@Override
	public boolean eliminarReserva(int idReserva) { //elimina una reserva del archivo y de memoria
		 boolean reservaEliminada = false;
		try {
			// Eliminar la reserva
	    	reservaEliminada = reservas.removeIf(reserva -> reserva.getIdReserva() == idReserva);

	        if (reservaEliminada) {
	            System.out.println("Reserva eliminada.");
	            guardarReservasEnArchivo(reservas); //Guarda las reservas actualizadas en el archivo
	            return reservaEliminada;
	        } else {
	            System.out.println("La reserva no existe.");
	            return reservaEliminada;
	        }
	     }catch (Exception e) {
			System.out.println("Error al eliminar la reserva. " + e.getMessage());
			e.printStackTrace();
			return reservaEliminada;
		}
	}
	
		@Override
	public boolean disponibilidadReservas (String fechaReserva) { //devuelve true si hay disponibilidad de reservas para una fecha
			int cont = 0;
			for(var reserva : reservas) {
				if(reserva.getFecha().equals(fechaReserva))
					cont++;
			}
			return cont < MAX_RESERVAS_POR_DIA;
		}


}


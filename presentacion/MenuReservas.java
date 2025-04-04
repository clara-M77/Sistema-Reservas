package sistema_reservas_archivos.presentacion;

import java.util.*;

import sistema_reservas_archivos.dominio.Reserva;
import sistema_reservas_archivos.servicio.IServicioReservasActuales;
import sistema_reservas_archivos.servicio.ServicioReservasActualesLista;
import sistema_reservas_archivos.servicio.ServicioReservasArchivos;

import java.nio.file.LinkOption;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MenuReservas {
	private static final Scanner consola = new Scanner(System.in);
	//creamos el objeto para obtener el servicio de reservas (lista)
	//IServicioReservasActuales servicioReservas = new ServicioReservasActualesLista();
	IServicioReservasActuales servicioReservas = new ServicioReservasArchivos();
	private List<Reserva> reservas = new ArrayList<>();
	
	private static int mostrarMenu(Scanner consola) {
        System.out.println();  	//crear una excepcion para que ingrese unicamente numeros desde el 1 al 5
		System.out.printf("""
				Menu:
				1. Hacer una reserva
				2. Actualizar una Reserva
				3. Eliminar reserva
				4. Mostrar Reservas Actuales
				5. Salir
				Seleccione una opcion: """);
				System.out.println();
		return Integer.parseInt(consola.nextLine());
}
	public boolean ejecutarMenu() {		
		boolean salir = false;
		System.out.println(" ** Sistema de Reservas ** ");
		
		while(!salir) {
			try {
		var opcion = mostrarMenu(consola);	
	  switch(opcion) {
		case 1 -> hacerReserva();	
		case 2 -> actualizarReserva();
		case 3 -> borrarReserva();
		case 4 -> servicioReservas.mostrarReservas(); 
		case 5 -> {
			System.out.println("Saliendo del sistema ");
			salir = true;
		}
		default -> { 
			System.out.println("La opcion ingresada no es correcta "); 
			}
		}
			}catch(Exception e) {
				System.out.println("Ocurrio un error. " + e.getMessage());
			}
			finally {
				System.out.println();
				}
	}
		return false;
}	
	private void hacerReserva() { //carga una reserva si hay disponibilidad para un dia. 
		System.out.println();
		String nombreCliente = validarNombre("Nombre del cliente: ");
		String apellidoCliente = validarNombre("Apellido del cliente: ");
		String fechaReserva = validarFecha("Fecha de la Reserva (dd/MM/yyyy): "); 
		String horaReserva = validarHora("Hora de la reserva (hh:mm): ");
				
		if (servicioReservas.disponibilidadReservas(fechaReserva)) {
				Reserva reserva = new Reserva(nombreCliente, apellidoCliente, fechaReserva, horaReserva);
				if(servicioReservas.reservaUnica(nombreCliente, apellidoCliente, fechaReserva, horaReserva)) {
				servicioReservas.agregarReserva(reserva);
				System.out.println("Reserva agregada. ");
				} else {
						System.out.println("La reserva ya existe. ");
					}
			} else {
			System.out.println("No hay disponibilidad para la fecha seleccionada.");
		}
	}
	
	private void actualizarReserva() { //actualiza los datos solicitados de una reserva existente
		boolean salir = false;	
		System.out.print("Ingrese el ID de la reserva que desea actualizar: ");
		int idReserva = Integer.parseInt(consola.nextLine()); 
		boolean existe = servicioReservas.getTotalReservas().stream()
																.anyMatch(reserva -> reserva.getIdReserva() == idReserva);
		if(!existe) {
			System.out.println("El ID es incorrecto. ");
			return;
		}
		
		while(!salir) {
		try { 					
			System.out.println("""
									Elige una opcion: 
									1. Modificar nombre del cliente.
									2. Modificar apellido del cliente.
									3. Modificar fecha de la reserva.
									4. Modificar hora de la reserva.
									5. Salir.
									""");
				int opcion = Integer.parseInt(consola.nextLine().trim()); 
				
				switch(opcion) {
				case 1 -> {
					String nuevoNombreCliente = validarNombre("Nuevo nombre del cliente: ");
					servicioReservas.modificarNombreReserva(idReserva, nuevoNombreCliente);			
					}
				case 2 -> {
					String nuevoApellidoCliente = validarNombre("Nuevo apellido del cliente: ");
					servicioReservas.modificarApellidoReserva(idReserva, nuevoApellidoCliente);
					}
				case 3 -> {			
					String nuevaFechaReserva = validarFecha("Nueva fecha de la reserva (dd/MM/yyyy): ");
					if(servicioReservas.disponibilidadReservas(nuevaFechaReserva)) {
					   servicioReservas.modificarFechaReserva(idReserva, nuevaFechaReserva);
					}else {
						System.out.println("No hay disponibilidad para la nueva fecha.");
								}
					}
				case 4 -> {
					String nuevaHoraReserva = validarHora("Nueva hora de la reserva: ");
					servicioReservas.modificarHoraReserva(idReserva, nuevaHoraReserva);
					}
				case 5 -> {
					System.out.print("Saliendo del menu.");
					salir = true;
					}
				default -> System.out.print("Opcion Invalida."); 
					}
		}catch (Exception e) {
						System.out.print("Ocurrio un error." + e.getMessage());
						e.printStackTrace();
						}
					}
	}
	
	private void borrarReserva() {
		var reservaBorrada = false;
		try{
			while(!reservaBorrada) {
			 if (servicioReservas.getTotalReservas().isEmpty()) {  //no se pueden eliminar reservas si es vacio, reservs en memoria o en el archivo
					System.out.println("No hay reservas para eliminar.");
					break;
			 }
			System.out.print("Ingrese el ID de la reserva: ");
			var idReserva = Integer.parseInt(consola.nextLine().trim());
			reservaBorrada = servicioReservas.eliminarReserva(idReserva);
			}
			}catch(NumberFormatException e) {
				System.out.println("El ID no es correcto " + e.getMessage());
			}
		}
	
	private static String validarNombre(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String nombre = consola.nextLine().trim();

            // Validación para solo letras, espacios y acentos
            if (nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                return nombre;
            } 
            else {
                System.out.println("Error: El nombre y apellido solo puede contener letras. Intente nuevamente.");
            }
        }
    }
	
	private static String validarFecha(String mensaje) { //deberia retornar un String
		while(true) {
			System.out.print(mensaje);
			String fechaStr = consola.nextLine().trim();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //da a la fecha el formato: "dd/MM/yyyy" 
                LocalDate fecha = LocalDate.parse(fechaStr, formatter); //parsea la fechaStr de String a LocalDate 
                LocalDate hoy = LocalDate.now();  //representa la fecha actual del sistema

                if (fecha.isBefore(hoy)) {
                    System.out.println("Error: No se pueden hacer reservas en fechas pasadas.");
                } else {
                    return fechaStr = fecha.format(formatter); //parsea de LocalDate a String
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Use dd/MM/yyyy.");
            }
        }
    }
	
	private static String validarHora(String mensaje) {
		while(true) {
			System.out.print(mensaje);
			String hora = consola.nextLine().trim();
			
			if(hora.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
				return hora;
			} 
			else {
				System.out.println("Error: La hora debe estar en formato hh:mm (24 horas). Intente nuevamente.");
			}	
		}
	}
	
}
	


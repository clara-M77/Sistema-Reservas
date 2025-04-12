package sistema_reservas_archivos.servicio;

import java.time.LocalDate;
import java.util.List;

import sistema_reservas_archivos.dominio.Reserva;

public interface IServicioReservasActuales {
	void agregarReserva(Reserva reserva);
	void mostrarReservas();
	void modificarNombreReserva(int idReserva, String nombreCliente);
	void modificarApellidoReserva(int idReserva, String apellidoCliente);
	void modificarFechaReserva(int idReserva, String nuevaFechaReserva);
	void modificarHoraReserva(int idReserva, String nuevaHoraReserva);
	boolean reservaUnica(String nombreCliente, String apellidoCliente, String fechaReserva, String horaReserva);
	List<Reserva> getTotalReservas();
	boolean eliminarReserva(int idReserva);
	boolean disponibilidadReservas(String fechaReserva);
}

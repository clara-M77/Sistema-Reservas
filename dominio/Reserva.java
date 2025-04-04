package sistema_reservas_archivos.dominio;

import java.io.Serializable;
import java.util.Objects;
import java.util.Date;

public class Reserva implements Serializable{

	private static int totalReservas = 0;
	private int idReserva;
	private String nombreCliente;
	private String apellidoCliente;
	private String fecha;
	private String hora;	
	
	public Reserva() {
		this.idReserva = ++totalReservas;
	}
	
	public Reserva(String nombreCliente, String apellidoCliente, String fecha, String hora) {
		this();
		this.nombreCliente = nombreCliente;
		this.apellidoCliente = apellidoCliente;
		this.fecha = fecha;
		this.hora = hora;
	}
	
	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getApellidoCliente() {
		return apellidoCliente;
	}

	public void setApellidoCliente(String apellidoCliente) {
		this.apellidoCliente = apellidoCliente;
	}
	
	public static int getTotalReservas() {
		return totalReservas;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
	
	public String escribirReserva() {
		return idReserva + "," + nombreCliente + "," + apellidoCliente + "," + fecha + "," + hora;
	}
	
	@Override
	public String toString() {
		return "Reserva [idReserva=" + idReserva + ", nombreCliente=" + nombreCliente + ", apellidoCliente="
				+ apellidoCliente + ", fecha=" + fecha + ", hora=" + hora + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidoCliente, fecha, hora, idReserva, nombreCliente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		return Objects.equals(apellidoCliente, other.apellidoCliente) && Objects.equals(fecha, other.fecha)
				&& Objects.equals(hora, other.hora) && idReserva == other.idReserva
				&& Objects.equals(nombreCliente, other.nombreCliente);
	}	
}

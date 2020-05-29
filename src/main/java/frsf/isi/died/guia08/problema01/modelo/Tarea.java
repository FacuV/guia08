package frsf.isi.died.guia08.problema01.modelo;

import frsf.isi.died.guia08.problema01.excepciones.TareaException;

import java.time.LocalDateTime;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Boolean facturada;
	
	public void asignarEmpleado(Empleado e) throws TareaException{
		if(fechaFin != null && empleadoAsignado != null){throw new TareaException("La tarea tiene un empleado asignado y fue finalizada");}
		e.getTareasAsignadas().add(this);
		empleadoAsignado = e;
	}

	public Tarea(Integer id, String descripcion, Integer duracionEstimada, Empleado empleadoAsignado) {
		this.id = id;
		this.descripcion = descripcion;
		this.duracionEstimada = duracionEstimada;
		this.empleadoAsignado = empleadoAsignado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.facturada = facturada;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmpleadoAsignado(Empleado empleadoAsignado) {this.empleadoAsignado = empleadoAsignado;}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public void setFacturada(Boolean facturada) {
		this.facturada = facturada;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	public String asCsv(){
		return (this.id + ";" + this.descripcion + ";" + this.duracionEstimada + ";" + this.empleadoAsignado.getCuil());
	}
}

package frsf.isi.died.guia08.problema01.modelo;

import frsf.isi.died.guia08.problema01.excepciones.TareaException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Empleado {

	public enum Tipo {CONTRATADO,EFECTIVO};
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;

	private DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	public Empleado(Integer cuil, String nombre, Tipo tipo, Double costoHora){
		this.cuil = cuil;
		this.nombre = nombre;
		this.tipo = tipo;
		this.costoHora = costoHora;
	}

	public Integer getCuil() {return cuil;}

	public void setCuil(Integer cuil) {this.cuil = cuil;}

	public String getNombre() {return nombre;}

	public void setNombre(String nombre) {this.nombre = nombre;}

	public Tipo getTipo() {return tipo;}

	public void setTipo(Tipo tipo) {this.tipo = tipo;}

	public Double getCostoHora() {return costoHora;}

	public void setCostoHora(Double costoHora) {this.costoHora = costoHora;}

	public List<Tarea> getTareasAsignadas() {return tareasAsignadas;}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {this.tareasAsignadas = tareasAsignadas;}

	public Function<Tarea, Double> getCalculoPagoPorTarea(){return calculoPagoPorTarea;}

	public void setCalculoPagoPorTarea(Function<Tarea, Double> calculoPagoPorTarea){this.calculoPagoPorTarea = calculoPagoPorTarea;}

	public Predicate<Tarea> getPuedeAsignarTarea(){return puedeAsignarTarea;}

	public void setPuedeAsignarTarea(Predicate<Tarea> puedeAsignarTarea){this.puedeAsignarTarea = puedeAsignarTarea;}

	public Double salario() {
		double salario = 0;
		int dias;
		ArrayList<Tarea> tareasNoFacturadas = (ArrayList) this.getTareasAsignadas().stream().filter(t -> t.getFacturada() == false && t.getFechaFin() != null).collect(Collectors.toList());
		for(Tarea t:tareasNoFacturadas){
			dias = (int) Duration.between(t.getFechaInicio(),t.getFechaFin()).toDays() * 4;
			if(this.getTipo() == Tipo.CONTRATADO){
				if(dias < t.getDuracionEstimada()){salario += t.getDuracionEstimada()*costoHora*1.30;}
				else if(dias >= t.getDuracionEstimada() + 8){salario += t.getDuracionEstimada()*costoHora*0.75;}
			}else{
				if(dias < t.getDuracionEstimada()){salario += t.getDuracionEstimada()*costoHora*1.20;}
			}
			t.setFacturada(true);
		}
		return salario;
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		return 0.0;
	}
		
	public Boolean asignarTarea(Tarea t) throws TareaException {

		if(t.getEmpleadoAsignado() != null){
			throw new TareaException("Ya hay un empleado asignado a la tarea. Seleccione otra tarea");
		}
		if(t.getFechaFin() != null){
			throw new TareaException("La tarea ha finalizado ya. Seleccione otra tarea");
		}

		if (this.getTipo() == Tipo.CONTRATADO) {
			if (tareasAsignadas.stream().filter(tr -> tr.getFechaFin() == null).count() < 5) {
				tareasAsignadas.add(t);
				return true;
			}else return false;
		} else {
			if ((tareasAsignadas.stream().filter(tr -> tr.getFechaFin() == null).map(j -> j.getDuracionEstimada()).collect(Collectors.summingInt(Integer::intValue)) + t.getDuracionEstimada()) <= 15) {
				tareasAsignadas.add(t);
				return true;
			}else return false;
		}

	}
	
	public void comenzar(Integer idTarea) throws TareaException{
		List<Tarea> aux = tareasAsignadas.stream().filter(tar -> tar.getId() == idTarea).collect(Collectors.toList());
		if(aux.isEmpty()) throw new TareaException("La tarea no esta asignada a este empleado.");
		aux.get(0).setFechaInicio(LocalDateTime.now());
	}
	
	public void finalizar(Integer idTarea) throws TareaException{
		List<Tarea> aux = tareasAsignadas.stream().filter(tar -> tar.getId() == idTarea).collect(Collectors.toList());
		if(aux.isEmpty()) throw new TareaException("La tarea no esta asignada a este empleado.");
		aux.get(0).setFechaFin(LocalDateTime.now());
	}


	public void comenzar(Integer idTarea,String fecha) throws TareaException {
		List<Tarea> aux = tareasAsignadas.stream().filter(tar -> tar.getId() == idTarea).collect(Collectors.toList());
		if (aux.isEmpty()) throw new TareaException("La tarea no esta asignada a este empleado.");
		aux.get(0).setFechaInicio(LocalDateTime.parse(fecha,formater));
	}
	
	public void finalizar(Integer idTarea,String fecha) throws TareaException {
		List<Tarea> aux = tareasAsignadas.stream().filter(tar -> tar.getId() == idTarea).collect(Collectors.toList());
		if (aux.isEmpty()) throw new TareaException("La tarea no esta asignada a este empleado.");
		aux.get(0).setFechaFin(LocalDateTime.parse(fecha,formater));
	}
}

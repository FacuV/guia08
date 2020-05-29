package frsf.isi.died.guia08.problema01;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import frsf.isi.died.guia08.problema01.excepciones.TareaException;
import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Tarea;

public class AppRRHH {

	private List<Empleado> empleados;
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		empleados.add(new Empleado(cuil,nombre,Empleado.Tipo.CONTRATADO,costoHora));
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		empleados.add(new Empleado(cuil,nombre,Empleado.Tipo.EFECTIVO,costoHora));
	}
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil() == cuil);
		if(emp.isPresent()){
			emp.get().getTareasAsignadas().add(new Tarea(idTarea,descripcion,duracionEstimada,emp.get()));
		}
	}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil() == cuil);
		if(emp.isPresent()){
			try {emp.get().comenzar(idTarea);
			}catch(TareaException t){
				System.out.println(t.getMessage());
			}
		}
	}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		Optional<Empleado> emp = buscarEmpleado(e -> e.getCuil() == cuil);
		if(emp.isPresent()){
			try {emp.get().finalizar(idTarea);
			}catch(TareaException t){
				System.out.println(t.getMessage());
			}
		}
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo){
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					agregarEmpleadoContratado(Integer.valueOf(fila[0]),fila[1],Double.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) {
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					agregarEmpleadoEfectivo(Integer.valueOf(fila[0]),fila[1],Double.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarTareasCSV(String nombreArchivo) {
		FileInputStream fis;
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
					asignarTarea(Integer.valueOf(fila[3]),Integer.valueOf(fila[0]),fila[1],Integer.valueOf(fila[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void guardarTareasTerminadasCSV() {
		ArrayList<String> csv = (ArrayList) empleados.stream().map(e -> e.getTareasAsignadas()).flatMap(List::stream).filter(t -> t.getFechaFin() != null && !t.getFacturada()).map(t -> t.asCsv()).collect(Collectors.toList());
		try(Writer fileWriter= new FileWriter("tareas_terminadas_no_facutadas.csv",true)) {
			try(BufferedWriter out = new BufferedWriter(fileWriter)) {
				for (String t : csv) {
					out.write(t + System.getProperty("line.separator"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}

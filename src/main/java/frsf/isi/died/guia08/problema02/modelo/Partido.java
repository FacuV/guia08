package frsf.isi.died.guia08.problema02.modelo;

import java.util.ArrayList;
import java.util.List;

public class Partido {

	private Equipo local;
	private Equipo visitante;
	private List<Evento> eventos;
	
	public void addEvento(Evento e) {
		if(this.eventos ==null) this.eventos = new ArrayList<Evento>();
		this.eventos.add(e);
	}
	
	
	public List<Evento> getEventos() {
		return eventos;
	}


	public Integer golesLocal() {
		return 0;
	}

	public Integer golesVisitante() {
		return 0;
	}

	public Integer golesPuntosLocal() {
		return 0;
	}

	public Integer golesPuntosVisitante() {
		return 0;
	}

	public Equipo getLocal() {
		return local;
	}

	public void setLocal(Equipo local) {
		this.local = local;
	}

	public Equipo getVisitante() {
		return visitante;
	}

	public void setVisitante(Equipo visitante) {
		this.visitante = visitante;
	}

}

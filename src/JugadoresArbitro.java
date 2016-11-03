class Constantes {
	public final static int MAXIMO = 10000;
}

class Arbitro {
	
	private int numJugadores;
	private int turno;
	private int objetivo;
	private boolean acertado;
	
	public Arbitro(int numJugadores) {
		this.numJugadores = numJugadores;
		this.objetivo = 1 + (int)(Constantes.MAXIMO * Math.random());
		this.acertado = false;
		this.turno = 1 + (int)(numJugadores * Math.random());
		System.out.println("El arbitro ha sacado el número: " + this.objetivo);
	}
	
	public synchronized boolean seAcabo() {
		return acertado;
	}
	
	public synchronized int esTurnoDe() {
		return turno;
	}

	public synchronized void jugar(int jugador, int jugada) {
		if (jugador == turno) {
			System.out.println("Es turno del jugador " + jugador + " y dice que es el " + jugada);
			if (jugada == objetivo) {
				acertado = true;
				System.out.println("El jugador " + jugador + " ha ganado");
			}
			else {
				turno = ((turno++) % numJugadores) + 1;
				notifyAll();
			}
		}
		
		else {
			System.out.println("El jugador " + jugador + " hace trampas y se ha colado");		
		}
	}
}

class Jugador extends Thread {
	
	private int dorsal;
	private Arbitro arbitro;
	
	public Jugador(int dorsal, Arbitro arbitro) {
		this.dorsal = dorsal;
		this.arbitro = arbitro;	
	}
	
	@Override
	public void run() {
		
		while (!arbitro.seAcabo()) {		
			if (arbitro.esTurnoDe() == this.dorsal) {
				int jugada = 1 + (int)(Constantes.MAXIMO * Math.random());
				arbitro.jugar(dorsal, jugada);
			}	
			else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


public class JugadoresArbitro {

	public static void main(String[] args) throws InterruptedException {
		Arbitro collina = new Arbitro(3);
		
		Jugador jugador1 = new Jugador(1, collina);
		Jugador jugador2 = new Jugador(2, collina);
		Jugador jugador3 = new Jugador(3, collina);
		Jugador jugador4 = new Jugador(4, collina);
		Jugador jugador5 = new Jugador(5, collina);
		Jugador jugador6 = new Jugador(6, collina);
		
	}

}

package syntax;

import java.util.Set;
import java.util.TreeSet;

/**
 * classe Chauffeur representant les donnees des chauffeurs
 * 
 * @author Girard, Grazon, Masson
 *
 *         janvier 2025
 */
public class Chauffeur {

	/**
	 * numChauf = numero identifiant du chauffeur
	 * bj = quantite totale de vin beaujolais livree
	 * bg = quantite totale de vin bourgogne livree
	 * ordin = quantite totale de vin ordinaire livree
	 */
	public int numChauf, bj, bg, ordin,capMax;

	/** magdif = ensemble des magasins livres */
	public Set<Integer> magDif;

	public Chauffeur(int numchauf, int bj, int bg, int ordin, Set<Integer> magDif) {
		this.numChauf = numchauf;
		this.bj = bj;
		this.bg = bg;
		this.ordin = ordin;
		this.magDif = new TreeSet<Integer>(magDif);
		this.capMax = 100;
	}

	public Chauffeur copie() {
		return new Chauffeur(numChauf, bj, bg, ordin, magDif);
	}

	public Chauffeur getChauffeur() {
		return this;
	}

	public int getNumChauf() {
		return this.numChauf;
	}

	public void setNumChauf(int numChauf) {
		this.numChauf = numChauf;
	}

	public int getBj() {
		return this.bj;
	}

	public void setBj(int bj) {
		this.bj = bj;
	}

	public int getBg() {
		return this.bg;
	}

	public void setBg(int bg) {
		this.bg = bg;
	}

	public int getOrdin() {
		return this.ordin;
	}

	public void setOrdin(int ordin) {
		this.ordin = ordin;
	}

	public Set<Integer> getMagDif() {
		return this.magDif;
	}

	public void setMagDif(Set<Integer> magDif) {
		this.magDif = magDif;
	}
	public void setCapMax(int qte){
		this.capMax = qte;
	}
	public int getCapMax(){
		return this.capMax;
	}

}

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

	/*
	 * prends en paramètre un String correspondant soit à un type de vin et la qté
	 * livrée soit mag avec une qté correspondant à son indice dans la tabIdent*/
	/* ne retourne rien mais met à jour les informations du chauffeur */
	public void update(String nom, int qte) {
		switch (nom) {
			case "bj":								//met à jour bj
				this.bj = this.bj + qte;
				break;
			case "bg":								//met à jour bg
				this.bg = this.bg + qte;
				break;
			case "ordi":							//met à jour ordin
				this.ordin = this.ordin + qte;
				break;
			case "mag":								//met à jour magDif
				this.magDif.add(qte);
				break;
				case "cap":							//met à jour ordin
				this.capMax = qte;
				break;
			default:
				break;
		}
	}
}

package syntax;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import lex.*;
import utils.*;

/**
 * La classe ActVin met en oeuvre les actions de l'automate d'analyse syntaxique
 * de l'application Vin
 * des fiches de livraison de vin
 * 
 * @author JAFFRE Paul ADJI Chene
 * 
 *         janvier 2025
 */
public class ActVin extends AutoVin {

	/** table des actions */
    private final int[][] ACTION =
    {/* Etat        BJ    BG   IDENT  NBENT VIRG PTVIRG BARRE AUTRES  */
	/* 0 */      { -1,   -1,    1,     -1,   -1,    -1,     9,    -1   },
	/* 1 */      {  3,    4,    5,      2,   -1,    -1,    -1,    -1   },
	/* 2 */      {  3,    4,    5,     -1,   -1,    -1,    -1,    -1   },
	/* 3 */      { -1,   -1,    5,     -1,   -1,    -1,    -1,    -1   },
	/* 4 */      { -1,   -1,   -1,      6,   -1,    -1,    -1,    -1   },
	/* 5 */      { -1,   -1,    5,     -1,    7,     8,    -1,    -1   },
	/* 6 */      { -1,   -1,   -1,     -1,   -1,    -1,    -1,    -1   },

			/* Rappel conventions : action -1 = action vide, pas de ligne pour etatFinal */
	};

	/**
	 * constructeur classe ActVintab
	 * 
	 * @param flot : donnee a analyser
	 */
	public ActVin(InputStream flot) {
		super(flot);
	}

	/**
	 * definition de la methode abstraite getAction de Automate
	 * 
	 * @param etat    : code de l'etat courant
	 * @param itemLex : code de l'item lexical courant
	 * @return code de l'action suivante
	 **/
	public int getAction(int etat, int itemLex) {
		return ACTION[etat][itemLex];
	}

	/**
	 * definition methode abstraite initAction de Automate
	 */
	public void initAction() {
		// Correspond a l'action 0 a effectuer a l'init
		initialisations();
	}

	/**
	 * definition de la methode abstraite faireAction de Automate
	 * 
	 * @param etat    : code de l'etat courant
	 * @param itemLex : code de l'item lexical courant
	 * @return code de l'etat suivant
	 **/
	public void faireAction(int etat, int itemLex) {
		executer(ACTION[etat][itemLex]);
	}

	/** types d'erreurs detectees */
	private static final int FATALE = 0, NONFATALE = 1;

	/**
	 * gestion des erreurs
	 * 
	 * @param tErr    type de l'erreur (FATALE ou NONFATALE)
	 * @param messErr message associe a l'erreur
	 */
	private void erreur(int tErr, String messErr) {
		Lecture.attenteSurLecture(messErr);
		switch (tErr) {
			case FATALE:
				errFatale = true;
				break;
			case NONFATALE:
				etatCourant = etatErreur;
				break;
			default:
				Lecture.attenteSurLecture("parametre incorrect pour erreur");
		}
	}

	/**
	 * acces a un attribut lexical
	 * cast pour preciser que analyseurLexical est ici de type LexVin
	 * @return valEnt associe a l'unite lexicale NBENTIER
	 */
	private int valEnt() {
		return ((LexVin) analyseurLexical).getValEnt();
	}

	/**
	 * acces a un attribut lexical
	 * cast pour preciser que analyseurLexical est de type LexVin
	 * 
	 * @return numId associe a l'unite lexicale IDENT
	 */
	private int numIdCourant() {
		return ((LexVin) analyseurLexical).getNumIdCourant();
	}

	/** taille d'une colonne pour affichage ecran */
	private static final int MAXLGID = 20;
	/** nombre maximum de chauffeurs */
	private static final int MAXCHAUF = 10;
	/** tableau des chauffeurs et resume des livraison de chacun */
	private Chauffeur[] tabChauf = new Chauffeur[MAXCHAUF];

	/**
	 * utilitaire d'affichage a l'ecran
	 * 
	 * @param ch est une chaine de longueur quelconque
	 * @return chaine ch cadree a gauche sur MAXLGID caracteres
	 */
	private String chaineCadrageGauche(String ch) {
		int lgch = Math.min(MAXLGID, ch.length());
		String chres = ch.substring(0, lgch);
		for (int k = lgch; k < MAXLGID; k++)
			chres = chres + " ";
		return chres;
	}

	/**
	 * affichage de tout le tableau de chauffeurs a l'ecran
	 */
	private void afficherChauf() {
		Ecriture.ecrireStringln("");
		String titre = "CHAUFFEUR                   BJ        BG       ORD     NBMAG\n"
				+ "---------                   --        --       ---     -----";
		Ecriture.ecrireStringln(titre);
		for (int i = 0; i <= indChauf; i++) {
			String idChaufCourant = ((LexVin) analyseurLexical).chaineIdent(tabChauf[i].numChauf);
			Ecriture.ecrireString(chaineCadrageGauche(idChaufCourant));
			Ecriture.ecrireInt(tabChauf[i].bj, 10);
			Ecriture.ecrireInt(tabChauf[i].bg, 10);
			Ecriture.ecrireInt(tabChauf[i].ordin, 10);
			Ecriture.ecrireInt(tabChauf[i].magDif.size(), 10);
			Ecriture.ecrireStringln("");
		}
	}

	/** indice courant du nombre de chauffeurs dans le tableau tabChauf */
	private int indChauf;
	/* !!! TODO : DELARATIONS A COMPLETER !!! */
	/* Variables */
	private int capaciteCiterne;
	private int indChauffeur;
	private int bj;
	private int bg;
	private int ordi;
	private int qte;
	private int mag;
	private String typeVin;
	private Set<Integer> magDif = new HashSet<>();;
	private Chauffeur ajtChauff;
	private boolean dejaAjt = false;

	/**
	 * initialisations a effectuer avant les actions
	 */
	private void initialisations() {
		indChauf = -1;

		/* !!! TODO : A COMPLETER SI BESOIN !!! */
	}

	/**
	 * execution d'une action
	 * 
	 * @param numact numero de l'action a executer
	 */
	public void executer(int numAct) {

		switch (numAct) {
			case -1: // action vide
				break;

			case 0:
				initAction();
				break;

			case 1:
				indChauffeur = numIdCourant();

			case 2:
				int valeur = valEnt();
				capaciteCiterne = capaciteVerif(valeur);
				break;

			case 3:
				typeVin = "bg";
				break;

			case 4:
				typeVin = "bj";
				break;

			case 5:
				mag = numIdCourant();
				magDif.add(mag);
				break;

			case 6:
				qte = valEnt();
				break;

			case 7:
				if (!dejaAjt) { // si le chauffeur n'est pas encore créé
					if (typeVin.compareTo("bj") == 0) {
						ajtChauff = new Chauffeur(indChauffeur, qte, 0, 0, magDif);
						ajtChauff.setCapMax(capaciteCiterne);
					} else {
						if (typeVin.compareTo("bg") == 0) {
							ajtChauff = new Chauffeur(indChauffeur, 0, qte, 0, magDif);
							ajtChauff.setCapMax(capaciteCiterne);
						} else {
							ajtChauff = new Chauffeur(indChauffeur, 0, 0, qte, magDif);
							ajtChauff.setCapMax(capaciteCiterne);
						}

					}
					for (int i = 0; i < tabChauf.length; i++) {
						if (tabChauf[i] == null) { // si l'élément courant est vide.
							tabChauf[i] = ajtChauff; // Ajouter le chauffeur dans le tableau.
							break; // Sortir de la boucle une fois l'ajout effectué.
						}
					}
					dejaAjt = true;
				} else { // si le chauffeur est déjà créé
					ajtChauff.update(typeVin, qte);
					ajtChauff.setMagDif(magDif);
				}
				break;
			case 8: // Créer le nouvel objet Chauffeur selon sa livraison
				if (dejaAjt = false) { // si le chauffeur n'est pas encore créé
					if (typeVin.compareTo("bj") == 0) {
						ajtChauff = new Chauffeur(indChauffeur, qte, 0, 0, magDif);
						ajtChauff.setCapMax(capaciteCiterne);
					} else {
						if (typeVin.compareTo("bg") == 0) {
							ajtChauff = new Chauffeur(indChauffeur, 0, qte, 0, magDif);
							ajtChauff.setCapMax(capaciteCiterne);
						} else {
							ajtChauff = new Chauffeur(indChauffeur, 0, 0, qte, magDif);
							ajtChauff.setCapMax(capaciteCiterne);
						}

					}
					for (int i = 0; i < tabChauf.length; i++) {
						if (tabChauf[i] == null) { // Si l'élément est vide.
							tabChauf[i] = ajtChauff; // Ajouter le chauffeur dans le tableau.
							break; // Sortir de la boucle une fois l'ajout effectué.
						}
					}
				} else { // si le chauffeur est déjà créé
					ajtChauff.update(typeVin, qte);
					ajtChauff.setMagDif(magDif);
				}
				break;
			case 9:
				break;
			default:
				Lecture.attenteSurLecture("action " + numAct + " non prevue");
		}
	}

	public int capaciteVerif(int capaciteCiterne) {
		if (capaciteCiterne >= 100 && capaciteCiterne <= 200) {
			return capaciteCiterne;
		} else {
			System.out.println("il y a erreur sur cette quantité");
			return 100;
		}

	}

}

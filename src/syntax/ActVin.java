package syntax;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	 * 
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
	/* !!! TODO : DELARATIONS A COMPLETER !!! */
	/* Variables */
	private int capaciteCiterne;
	private int indChauf;// quand il n'y a pas de chauffeur je l'initialise à -1 pour ne pas le confondre
	private int bj;
	private int bg;
	private int ordi;
	private int qte;
	private int mag;
	private int qualiteCourante;
	// 0 : BEAUJOLAIS
	// 1 : BOURGOGNE
	// 2 : ORDINAIRE
	private Chauffeur ajtChauff;
	private boolean dejaAjt = false;
	private int nbFicheTotale;
	private boolean ficheCorrect;
	private int nbFicheCorrect;

	/**
	 * initialisations a effectuer avant les actions
	 */
	private void initialisations() {

		/* !!! TODO : A COMPLETER SI BESOIN !!! */
		indChauf = -1;
		qualiteCourante = 2;
		nbFicheTotale = 0;
		ficheCorrect = true;
		nbFicheCorrect = 0;

	}

	/**
	 * execution d'une action
	 * 
	 * @param numact numero de l'action a executer
	 */
	public void executer(int numAct) {

		switch (numAct) {
			case -1: // action vide
				erreur(NONFATALE, "Erreur syntaxique détectée");
				ficheCorrect = false;
				break;

			case 0:
				initAction();
				break;

			case 1:
				nbFicheTotale++;
				boolean found = false;
				if (indChauf >= MAXCHAUF - 1) {
					erreur(FATALE, "Nombre maximum de chauffeurs dépassé");
					ficheCorrect = false;
				} else {
					for (int i = 0; i <= MAXCHAUF - 1; i++) {
						if (tabChauf[i] != null && tabChauf[i].numChauf == numIdCourant()) {
							found = true;
							indChauf = i;
							break;
						}

					}
					if (!found && indChauf < MAXCHAUF) {
						Chauffeur chauf = new Chauffeur(numIdCourant(), 0, 0, 0, new TreeSet<>());
						tabChauf[indChauf + 1] = chauf;
						indChauf++;
					}
				}
				break;
			case 2:
				capaciteCiterne = valEnt();
				if (capaciteCiterne < 100 && capaciteCiterne > 200) {
					System.out.println("il y a erreur sur cette quantité :" + capaciteCiterne);
					ficheCorrect = false;
					capaciteCiterne = 100;
				}
				tabChauf[indChauf].setCapMax(capaciteCiterne);
				break;

			case 3:
				qualiteCourante = 0;
				break;

			case 4:
				qualiteCourante = 1;
				break;

			case 5:
				mag = numIdCourant();
				tabChauf[indChauf].magDif.add(mag);
				break;

			case 6:
				qte = valEnt();
				if (qte == 0) {
					erreur(NONFATALE, "Error ; quantité livré nulle");
					ficheCorrect = false;
				} else if (qte > capaciteCiterne) {
					erreur(NONFATALE, "Error : capacité citerne insuffisante");
					ficheCorrect = false;
				}

				switch (qualiteCourante) {
					case 0:
						tabChauf[indChauf].bj += qte;
						break;
					case 1:
						tabChauf[indChauf].bg += qte;
						break;
					case 2:
						tabChauf[indChauf].ordin += qte;
						break;
					default:
						break;
				}
				break;

			case 7:
				qualiteCourante = 2;// sinon au cas où l'on ait de l'ordinaire juste après on garderait le type de
									// vin précédent
				break;
			case 8:
				nbFicheCorrect++;
				Ecriture.ecrireStringln("		à la fin de la fiche " + nbFicheTotale + " de livraison  :");
				afficherChauf();
				Ecriture.ecrireStringln("FIN de la fiche " + nbFicheTotale + "\n");

				break;
			case 9:

				Ecriture.ecrireStringln("\nFIN de l'analyse ");
				afficheMagMax();
				Ecriture.ecrireStringln(
						"Fiches correctes : " + nbFicheCorrect + "Nombre total de fiches : " + nbFicheTotale);

				break;
			default:
				Lecture.attenteSurLecture("action " + numAct + " non prevue");
		}
	}

	public void afficheMagMax() {
		int maxMagC = -1;
		int id = -1;

		for (int i = 0; i < MAXCHAUF; i++) {
			if (tabChauf[i] != null && tabChauf[i].magDif.size() > maxMagC) {
				id = i;
				maxMagC = tabChauf[i].magDif.size();
			}
		}
		String idChaufCourant = ((LexVin) analyseurLexical).chaineIdent(tabChauf[id].numChauf);
		Ecriture.ecrireStringln("\n" + idChaufCourant + " a livré  " + maxMagC + " magasins différents");

	}

}

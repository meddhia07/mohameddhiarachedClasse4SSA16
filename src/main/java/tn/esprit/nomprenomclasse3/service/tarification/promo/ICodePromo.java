package tn.esprit.nomprenomclasse3.service.tarification.promo;

/**
 * Atelier 7 - Mission 3 : patron STRATEGIE (Strategy).
 * Ce qui CHANGE d'un code promo a l'autre : son libelle et sa regle de reduction.
 * Ce qui NE CHANGE PAS : le calcul du tarif qui choisit la strategie et l'applique.
 * Ajouter un code = ajouter une classe @Component, sans modifier le code existant (OCP).
 */
public interface ICodePromo {

    /** Code saisi par le client (ex. "ETE10"). */
    String code();

    /** Applique la reduction au montant ; le resultat n'est jamais negatif. */
    double appliquer(double montant);
}

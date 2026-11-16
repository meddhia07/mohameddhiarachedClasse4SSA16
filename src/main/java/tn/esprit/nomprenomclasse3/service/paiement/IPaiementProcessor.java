package tn.esprit.nomprenomclasse3.service.paiement;

import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

/** Choisit le bon mode et paie. */
public interface IPaiementProcessor {

    Paiement payer(ModePaiement mode, double montant);
}

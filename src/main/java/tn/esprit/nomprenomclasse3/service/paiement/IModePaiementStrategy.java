package tn.esprit.nomprenomclasse3.service.paiement;

import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

/** Une classe par mode de paiement : carte, virement, especes... */
public interface IModePaiementStrategy {

    ModePaiement mode();

    Paiement payer(double montant);
}

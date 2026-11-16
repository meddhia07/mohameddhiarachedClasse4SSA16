package tn.esprit.nomprenomclasse3.service.confirmation;

import tn.esprit.nomprenomclasse3.domain.Contrat;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;

/** Orchestre la confirmation d'une reservation. */
public interface IConfirmationReservationService {

    Contrat confirmer(Long reservationId, ModePaiement mode, String codePromo, String canal);
}

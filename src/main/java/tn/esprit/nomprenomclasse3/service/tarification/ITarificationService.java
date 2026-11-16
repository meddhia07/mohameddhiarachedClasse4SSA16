package tn.esprit.nomprenomclasse3.service.tarification;

import tn.esprit.nomprenomclasse3.domain.Reservation;

public interface ITarificationService {

    double calculerMontant(Reservation reservation, String codePromo);
}

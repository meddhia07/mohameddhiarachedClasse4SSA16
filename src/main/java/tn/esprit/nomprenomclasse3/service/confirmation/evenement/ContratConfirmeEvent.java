package tn.esprit.nomprenomclasse3.service.confirmation.evenement;

import tn.esprit.nomprenomclasse3.domain.Client;

import java.math.BigDecimal;

/**
 * Atelier 7 - Mission 4 : evenement publie par le SUJET (la confirmation) a chaque contrat confirme.
 * Objet immuable (record) : il ne transporte que les donnees utiles aux observateurs.
 */
public record ContratConfirmeEvent(Long contratId, Long reservationId, Client client,
                                   BigDecimal montant, String canal) {
}

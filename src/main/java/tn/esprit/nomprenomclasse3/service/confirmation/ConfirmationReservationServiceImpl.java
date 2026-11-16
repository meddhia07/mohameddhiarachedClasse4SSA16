package tn.esprit.nomprenomclasse3.service.confirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.nomprenomclasse3.domain.Contrat;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;
import tn.esprit.nomprenomclasse3.domain.Reservation;
import tn.esprit.nomprenomclasse3.domain.StatutReservation;
import tn.esprit.nomprenomclasse3.service.IContratService;
import tn.esprit.nomprenomclasse3.service.IReservationService;
import tn.esprit.nomprenomclasse3.service.confirmation.evenement.ContratConfirmeEvent;
import tn.esprit.nomprenomclasse3.service.paiement.IPaiementProcessor;
import tn.esprit.nomprenomclasse3.service.tarification.ITarificationService;

import java.math.BigDecimal;

/**
 * Orchestre la confirmation : tarif -> paiement -> contrat -> statut -> evenement.
 * Mission 4 (patron OBSERVATEUR) : la classe ne connait plus ceux qui reagissent a un contrat
 * confirme ; elle PUBLIE un ContratConfirmeEvent et Spring le diffuse a tous les @EventListener.
 * Mission 5 : IPaiementProcessor injecte = le decorateur chronometre (@Primary), en toute transparence.
 */
@Service
@RequiredArgsConstructor
public class ConfirmationReservationServiceImpl implements IConfirmationReservationService {

    private final IReservationService reservationService;
    private final ITarificationService tarificationService;
    private final IPaiementProcessor paiementProcessor;
    private final IContratService contratService;
    private final ApplicationEventPublisher evenements;

    @Override
    @Transactional
    public Contrat confirmer(Long reservationId, ModePaiement mode, String codePromo, String canal) {
        Reservation r = reservationService.findById(reservationId);
        if (r.getStatut() == StatutReservation.CONFIRMEE) {
            throw new IllegalArgumentException("La reservation " + reservationId + " est deja confirmee");
        }
        double montant = tarificationService.calculerMontant(r, codePromo);
        Paiement paiement = paiementProcessor.payer(mode, montant);

        Contrat c = Contrat.builder()
                .dateSignature(paiement.getDatePaiement())
                .montantTotal(BigDecimal.valueOf(montant))
                .valide(true)
                .build();
        c.ajouterPaiement(paiement);
        Contrat cree = contratService.create(c);

        r.setStatut(StatutReservation.CONFIRMEE);
        reservationService.update(r.getIdReservation(), r);

        // Avant : notificationService.notifier(r.getClient(), canal, "Contrat ... confirme");
        evenements.publishEvent(new ContratConfirmeEvent(
                cree.getIdContrat(), reservationId, r.getClient(), cree.getMontantTotal(), canal));
        return cree;
    }
}

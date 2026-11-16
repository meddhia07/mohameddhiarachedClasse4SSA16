package tn.esprit.nomprenomclasse3.service.confirmation.evenement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/** Observateur 4 (nouveau) : trace d'audit. */
@Slf4j
@Component
public class AuditListener {

    @TransactionalEventListener(fallbackExecution = true)
    public void onContratConfirme(ContratConfirmeEvent event) {
        log.info("[AUDIT] reservation {} -> contrat {} (canal {})",
                event.reservationId(), event.contratId(), event.canal());
    }
}

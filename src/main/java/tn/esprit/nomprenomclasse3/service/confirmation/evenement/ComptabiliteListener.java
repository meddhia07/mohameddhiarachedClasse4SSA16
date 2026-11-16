package tn.esprit.nomprenomclasse3.service.confirmation.evenement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/** Observateur 2 (nouveau) : le comptable est prevenu. Ajoute SANS modifier la confirmation. */
@Slf4j
@Component
public class ComptabiliteListener {

    @TransactionalEventListener(fallbackExecution = true)
    public void onContratConfirme(ContratConfirmeEvent event) {
        log.info("[COMPTABILITE] contrat {} a enregistrer : {} TND", event.contratId(), event.montant());
    }
}

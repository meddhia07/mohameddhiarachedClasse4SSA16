package tn.esprit.nomprenomclasse3.service.confirmation.evenement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import tn.esprit.nomprenomclasse3.service.notification.INotificationService;

/** Observateur 1 : le client est notifie (ancien appel code en dur dans la confirmation). */
@Component
@RequiredArgsConstructor
public class NotificationClientListener {

    private final INotificationService notificationService;

    // AFTER_COMMIT (par defaut) : on ne previent personne si la transaction est annulee
    @TransactionalEventListener(fallbackExecution = true)
    public void onContratConfirme(ContratConfirmeEvent event) {
        notificationService.notifier(event.client(), event.canal(),
                "Contrat " + event.contratId() + " confirme");
    }
}

package tn.esprit.nomprenomclasse3.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.nomprenomclasse3.domain.Client;

/** Envoi simule (journal) : le vrai fournisseur SMS / e-mail est hors perimetre de l'atelier. */
@Slf4j
@Service
public class NotificationServiceImpl implements INotificationService {

    @Override
    public void notifier(Client client, String canal, String message) {
        String destinataire = client == null ? "client inconnu" : client.getPrenom() + " " + client.getNom();
        log.info("[NOTIFICATION] canal={} destinataire={} message=\"{}\"", canal, destinataire, message);
    }
}

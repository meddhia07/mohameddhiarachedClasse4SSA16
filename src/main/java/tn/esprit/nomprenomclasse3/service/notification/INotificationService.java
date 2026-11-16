package tn.esprit.nomprenomclasse3.service.notification;

import tn.esprit.nomprenomclasse3.domain.Client;

public interface INotificationService {

    void notifier(Client client, String canal, String message);
}

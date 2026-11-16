package tn.esprit.nomprenomclasse3.service.paiement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

/** Strategie de paiement CARTE. La fabrication du Paiement est deleguee a PaiementFactory (Mission 2). */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaiementCarte implements IModePaiementStrategy {

    private final PaiementFactory paiementFactory;

    @Override
    public ModePaiement mode() {
        return ModePaiement.CARTE;
    }

    @Override
    public Paiement payer(double montant) {
        log.debug("Autorisation demandee au TPE bancaire : {}", montant);
        return paiementFactory.creer(mode(), montant);
    }
}

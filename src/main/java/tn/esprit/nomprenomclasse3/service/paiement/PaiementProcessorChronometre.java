package tn.esprit.nomprenomclasse3.service.paiement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

/**
 * Atelier 7 - Mission 5 : patron DECORATEUR (Decorator, famille structure).
 * Meme interface que l'objet decore (IPaiementProcessor), il l'enveloppe et ajoute un comportement
 * (journal : mode, montant, duree) AVANT/APRES la delegation, sans modifier PaiementProcessorImpl.
 *
 * Spring connait desormais DEUX beans IPaiementProcessor :
 *  - sans precaution, l'injection dans ConfirmationReservationServiceImpl echoue au demarrage
 *    (NoUniqueBeanDefinitionException : expected single matching bean but found 2) ;
 *  - @Primary designe le decorateur comme choix par defaut pour tous les clients ;
 *  - @Qualifier("paiementProcessorImpl") lui donne, a lui seul, le vrai processeur
 *    (sans qualificateur il recevrait... lui-meme : dependance circulaire).
 */
@Slf4j
@Service
@Primary
public class PaiementProcessorChronometre implements IPaiementProcessor {

    private final IPaiementProcessor delegue;

    public PaiementProcessorChronometre(@Qualifier("paiementProcessorImpl") IPaiementProcessor delegue) {
        this.delegue = delegue;
    }

    @Override
    public Paiement payer(ModePaiement mode, double montant) {
        long debut = System.nanoTime();
        boolean succes = false;
        try {
            Paiement paiement = delegue.payer(mode, montant);
            succes = true;
            return paiement;
        } finally {
            double dureeMs = (System.nanoTime() - debut) / 1_000_000.0;
            log.info("[PAIEMENT] mode={} montant={} duree={} ms succes={}",
                    mode, montant, String.format("%.3f", dureeMs), succes);
        }
    }
}

package tn.esprit.nomprenomclasse3.service.confirmation.evenement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/** Observateur 3 (nouveau) : statistique de la direction (compteurs thread-safe, cf. Mission 1). */
@Slf4j
@Component
public class StatistiquesListener {

    private final AtomicLong nombreContrats = new AtomicLong();
    private final AtomicReference<BigDecimal> chiffreAffaires = new AtomicReference<>(BigDecimal.ZERO);

    @TransactionalEventListener(fallbackExecution = true)
    public void onContratConfirme(ContratConfirmeEvent event) {
        long n = nombreContrats.incrementAndGet();
        BigDecimal ca = chiffreAffaires.accumulateAndGet(event.montant(), BigDecimal::add);
        log.info("[STATISTIQUES] contrats confirmes={} chiffre d'affaires={} TND", n, ca);
    }
}

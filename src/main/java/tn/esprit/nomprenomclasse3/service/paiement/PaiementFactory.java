package tn.esprit.nomprenomclasse3.service.paiement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;

/**
 * Atelier 7 - Mission 2 : patron FABRIQUE (Factory, famille creation).
 * Un seul endroit sait construire un Paiement (montant arrondi, mode, date issue du Clock injecte),
 * au lieu de repeter "new Paiement() + setDatePaiement(LocalDate.now())" dans chaque mode de paiement.
 */
@Component
@RequiredArgsConstructor
public class PaiementFactory {

    private final Clock clock;

    public Paiement creer(ModePaiement mode, double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant d'un paiement ne peut pas etre negatif");
        }
        return Paiement.builder()
                .modePaiement(mode)
                .montant(BigDecimal.valueOf(montant).setScale(2, RoundingMode.HALF_UP))
                .datePaiement(LocalDate.now(clock))
                .build();
    }
}

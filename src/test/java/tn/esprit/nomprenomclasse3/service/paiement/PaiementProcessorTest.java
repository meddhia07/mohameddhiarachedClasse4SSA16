package tn.esprit.nomprenomclasse3.service.paiement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Atelier 7 : Missions 2 (fabrique + horloge) et 5 (decorateur). */
class PaiementProcessorTest {

    private static final ZoneId ZONE = ZoneId.of("Africa/Tunis");
    private final Clock le15Aout = Clock.fixed(LocalDate.of(2026, 8, 15).atStartOfDay(ZONE).toInstant(), ZONE);
    private final PaiementFactory fabrique = new PaiementFactory(le15Aout);
    private final PaiementProcessorImpl processeur = new PaiementProcessorImpl(List.of(
            new PaiementCarte(fabrique), new PaiementVirement(fabrique), new PaiementEspeces(fabrique)));

    @Test
    @DisplayName("Mission 2 : la date du paiement vient du Clock (15 aout simule)")
    void dateControlee() {
        Paiement p = processeur.payer(ModePaiement.CARTE, 150);
        assertThat(p.getDatePaiement()).isEqualTo(LocalDate.of(2026, 8, 15));
        assertThat(p.getModePaiement()).isEqualTo(ModePaiement.CARTE);
        assertThat(p.getMontant()).isEqualByComparingTo(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Mission 2 : chaque mode est resolu vers sa strategie")
    void tousLesModes() {
        for (ModePaiement mode : ModePaiement.values()) {
            assertThat(processeur.payer(mode, 10).getModePaiement()).isEqualTo(mode);
        }
    }

    @Test
    @DisplayName("Mission 2 : deux strategies pour le meme mode sont refusees")
    void doublonRefuse() {
        assertThatThrownBy(() -> new PaiementProcessorImpl(List.of(new PaiementCarte(fabrique), new PaiementCarte(fabrique))))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Mission 5 : le decorateur delegue au processeur existant et renvoie son resultat")
    void decorateurDelegue() {
        int[] appels = {0};
        IPaiementProcessor espion = (mode, montant) -> {
            appels[0]++;
            return processeur.payer(mode, montant);
        };
        PaiementProcessorChronometre chrono = new PaiementProcessorChronometre(espion);
        Paiement p = chrono.payer(ModePaiement.VIREMENT, 330);
        assertThat(appels[0]).isEqualTo(1);
        assertThat(p.getModePaiement()).isEqualTo(ModePaiement.VIREMENT);
    }

    @Test
    @DisplayName("Mission 5 : une erreur du processeur decore est propagee (et journalisee)")
    void decorateurPropageErreur() {
        PaiementProcessorChronometre chrono = new PaiementProcessorChronometre(processeur);
        assertThatThrownBy(() -> chrono.payer(ModePaiement.CARTE, -5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

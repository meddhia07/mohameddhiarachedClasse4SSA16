package tn.esprit.nomprenomclasse3.service.tarification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tn.esprit.nomprenomclasse3.domain.Reservation;
import tn.esprit.nomprenomclasse3.domain.Vehicule;
import tn.esprit.nomprenomclasse3.service.tarification.promo.ICodePromo;
import tn.esprit.nomprenomclasse3.service.tarification.promo.PromoBienvenue;
import tn.esprit.nomprenomclasse3.service.tarification.promo.PromoEte10;
import tn.esprit.nomprenomclasse3.service.tarification.promo.PromoRentree;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/** Atelier 7 : tests unitaires des Missions 1, 2 et 3 (sans base de donnees ni contexte Spring). */
class TarificationServiceImplTest {

    private static final ZoneId ZONE = ZoneId.of("Africa/Tunis");
    private static final List<ICodePromo> PROMOS = List.of(new PromoBienvenue(), new PromoEte10(), new PromoRentree());

    private static Clock horloge(String date) {
        return Clock.fixed(LocalDate.parse(date).atStartOfDay(ZONE).toInstant(), ZONE);
    }

    private static Reservation reservation(int jours, String tarif) {
        Vehicule v = Vehicule.builder().tarifJournalier(new BigDecimal(tarif)).build();
        LocalDate debut = LocalDate.of(2026, 12, 1);
        return Reservation.builder().dateDebut(debut).dateFin(debut.plusDays(jours)).vehicule(v).build();
    }

    private final TarificationServiceImpl service = new TarificationServiceImpl(PROMOS, horloge("2026-11-16"));

    @Test
    @DisplayName("Mission 2 : tarif de base = nombre de jours x tarif journalier")
    void tarifDeBase() {
        assertThat(service.calculerMontant(reservation(3, "100"), null)).isEqualTo(300.0);
    }

    @Test
    @DisplayName("Mission 2 : le 15 aout est simule grace au Clock injecte (+20 %)")
    void quinzeAout() {
        TarificationServiceImpl le15Aout = new TarificationServiceImpl(PROMOS, horloge("2026-08-15"));
        assertThat(le15Aout.calculerMontant(reservation(3, "100"), null)).isEqualTo(360.0);
    }

    @Test
    @DisplayName("Mission 3 : chaque code promo est une strategie")
    void codesPromo() {
        Reservation r = reservation(3, "100");
        assertThat(service.calculerMontant(r, "ETE10")).isEqualTo(270.0);
        assertThat(service.calculerMontant(r, "RENTREE")).isEqualTo(255.0);
        assertThat(service.calculerMontant(r, "BIENVENUE")).isEqualTo(280.0);
        assertThat(service.calculerMontant(r, "INCONNU")).isEqualTo(300.0);
    }

    @Test
    @DisplayName("Mission 3 : un nouveau code s'ajoute sans modifier TarificationServiceImpl")
    void nouveauCodeSansModification() {
        ICodePromo noel = new ICodePromo() {
            public String code() { return "NOEL25"; }
            public double appliquer(double montant) { return montant * 0.75; }
        };
        List<ICodePromo> promos = new ArrayList<>(PROMOS);
        promos.add(noel);
        TarificationServiceImpl avecNoel = new TarificationServiceImpl(promos, horloge("2026-11-16"));
        assertThat(avecNoel.calculerMontant(reservation(4, "100"), "NOEL25")).isEqualTo(300.0);
    }

    @Test
    @DisplayName("Mission 1 : le singleton sans etat donne le bon prix a chaque requete concurrente")
    void pasDeMelangeEnConcurrence() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(16);
        try {
            List<Callable<Boolean>> requetes = new ArrayList<>();
            for (int i = 1; i <= 2000; i++) {
                int tarif = 10 + i % 97;
                requetes.add(() -> service.calculerMontant(reservation(2, String.valueOf(tarif)), null) == 2.0 * tarif);
            }
            for (Future<Boolean> f : pool.invokeAll(requetes)) {
                assertThat(f.get()).isTrue();
            }
        } finally {
            pool.shutdownNow();
        }
    }
}

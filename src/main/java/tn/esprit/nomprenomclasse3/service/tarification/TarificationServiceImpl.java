package tn.esprit.nomprenomclasse3.service.tarification;

import org.springframework.stereotype.Service;
import tn.esprit.nomprenomclasse3.domain.Reservation;
import tn.esprit.nomprenomclasse3.service.tarification.promo.ICodePromo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service de tarification.
 * <ul>
 *   <li>Mission 1 : bean SINGLETON (une seule instance partagee par tous les threads) donc SANS ETAT
 *       mutable : tous les calculs se font dans des variables locales (pile du thread).</li>
 *   <li>Mission 2 : la date du jour vient du Clock injecte (LocalDate.now(clock)), jamais de LocalDate.now().</li>
 *   <li>Mission 3 : les codes promo sont des strategies ICodePromo injectees par Spring.</li>
 * </ul>
 */
@Service
public class TarificationServiceImpl implements ITarificationService {

    /** Jour ferie a tarif special : +20 %. */
    static final double MAJORATION_JOUR_FERIE = 1.20;
    private static final Set<MonthDay> JOURS_FERIES = Set.of(
            MonthDay.of(1, 1), MonthDay.of(3, 20), MonthDay.of(5, 1), MonthDay.of(7, 25), MonthDay.of(8, 15));

    // Champs final, fixes a la construction : ils ne changent jamais apres -> partage sans risque
    private final Map<String, ICodePromo> codesPromo;
    private final Clock clock;

    public TarificationServiceImpl(List<ICodePromo> codesPromo, Clock clock) {
        this.codesPromo = codesPromo.stream()
                .collect(Collectors.toUnmodifiableMap(ICodePromo::code, Function.identity()));
        this.clock = clock;
    }

    @Override
    public double calculerMontant(Reservation reservation, String codePromo) {
        if (reservation.getVehicule() == null || reservation.getVehicule().getTarifJournalier() == null) {
            throw new IllegalArgumentException("La reservation doit designer un vehicule avec un tarif journalier");
        }
        // variables LOCALES : chaque requete (thread) a les siennes
        long jours = Math.max(1, ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin()));
        double montant = jours * reservation.getVehicule().getTarifJournalier().doubleValue();

        if (estJourFerie(LocalDate.now(clock))) {
            montant *= MAJORATION_JOUR_FERIE;
        }
        if (codePromo != null) {
            ICodePromo promo = codesPromo.get(codePromo.trim().toUpperCase());
            if (promo != null) {
                montant = promo.appliquer(montant);
            }
        }
        return BigDecimal.valueOf(montant).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static boolean estJourFerie(LocalDate date) {
        return JOURS_FERIES.contains(MonthDay.from(date));
    }
}

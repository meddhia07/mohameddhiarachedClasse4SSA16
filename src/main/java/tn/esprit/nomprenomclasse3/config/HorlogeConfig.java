package tn.esprit.nomprenomclasse3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Atelier 7 - Mission 2 : la date du jour devient une DEPENDANCE injectee (java.time.Clock),
 * fabriquee a un seul endroit. En production : horloge systeme. Pour les testeurs :
 * autoloc.horloge.date-fixe=2026-08-15 (fichier application-test15aout.properties,
 * profil "test15aout") fige la date sans toucher au code.
 */
@Configuration
public class HorlogeConfig {

    @Bean
    public Clock clock(@Value("${autoloc.horloge.date-fixe:}") String dateFixe) {
        ZoneId zone = ZoneId.systemDefault();
        if (dateFixe == null || dateFixe.isBlank()) {
            return Clock.system(zone);
        }
        return Clock.fixed(LocalDate.parse(dateFixe).atStartOfDay(zone).toInstant(), zone);
    }
}

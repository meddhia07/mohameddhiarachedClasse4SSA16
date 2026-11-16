package tn.esprit.nomprenomclasse3.service.tarification.promo;

import org.springframework.stereotype.Component;

/** BIENVENUE : -20 (montant fixe), l'ancienne regle ecrite en dur dans TarificationServiceImpl. */
@Component
public class PromoBienvenue implements ICodePromo {

    private static final double REMISE = 20;

    @Override
    public String code() {
        return "BIENVENUE";
    }

    @Override
    public double appliquer(double montant) {
        return Math.max(0, montant - REMISE);
    }
}

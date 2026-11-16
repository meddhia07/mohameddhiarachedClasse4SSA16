package tn.esprit.nomprenomclasse3.service.tarification.promo;

import org.springframework.stereotype.Component;

/** ETE10 : -10 %. */
@Component
public class PromoEte10 extends PromoPourcentage {

    public PromoEte10() {
        super("ETE10", 10);
    }
}

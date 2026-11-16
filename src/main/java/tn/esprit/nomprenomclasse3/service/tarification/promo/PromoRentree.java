package tn.esprit.nomprenomclasse3.service.tarification.promo;

import org.springframework.stereotype.Component;

/** RENTREE : -15 %. */
@Component
public class PromoRentree extends PromoPourcentage {

    public PromoRentree() {
        super("RENTREE", 15);
    }
}

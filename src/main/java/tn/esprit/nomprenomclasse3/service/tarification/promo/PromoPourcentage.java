package tn.esprit.nomprenomclasse3.service.tarification.promo;

/**
 * Base commune des codes promo exprimes en pourcentage (ETE10, RENTREE...).
 */
public abstract class PromoPourcentage implements ICodePromo {

    private final String code;
    private final double pourcentage;

    protected PromoPourcentage(String code, double pourcentage) {
        this.code = code;
        this.pourcentage = pourcentage;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public double appliquer(double montant) {
        return montant * (1 - pourcentage / 100.0);
    }
}

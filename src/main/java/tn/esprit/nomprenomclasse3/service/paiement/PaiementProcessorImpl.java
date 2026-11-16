package tn.esprit.nomprenomclasse3.service.paiement;

import org.springframework.stereotype.Service;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.domain.Paiement;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Choisit la strategie correspondant au mode et paie.
 * Mission 2 : la table mode -> classe n'est plus construite ici avec des "new" : Spring fabrique
 * toutes les strategies (@Component) et les injecte ; on se contente de les indexer par mode.
 * Ajouter un mode (WALLET...) = ajouter une classe, sans rouvrir ce processeur.
 */
@Service
public class PaiementProcessorImpl implements IPaiementProcessor {

    private final Map<ModePaiement, IModePaiementStrategy> strategies;

    public PaiementProcessorImpl(List<IModePaiementStrategy> modes) {
        Map<ModePaiement, IModePaiementStrategy> table = new EnumMap<>(ModePaiement.class);
        for (IModePaiementStrategy strategie : modes) {
            if (table.put(strategie.mode(), strategie) != null) {
                throw new IllegalStateException("Deux strategies declarees pour le mode " + strategie.mode());
            }
        }
        this.strategies = Collections.unmodifiableMap(table);
    }

    @Override
    public Paiement payer(ModePaiement mode, double montant) {
        IModePaiementStrategy strategie = strategies.get(mode);
        if (strategie == null) {
            throw new IllegalArgumentException("Mode de paiement non pris en charge : " + mode);
        }
        return strategie.payer(montant);
    }
}

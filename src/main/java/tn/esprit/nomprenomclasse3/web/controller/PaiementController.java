package tn.esprit.nomprenomclasse3.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.nomprenomclasse3.domain.Paiement;
import tn.esprit.nomprenomclasse3.service.IPaiementService;

import java.util.List;

/**
 * API REST des paiements, en LECTURE SEULE : un paiement est une composition du Contrat
 * (cascade ALL + orphanRemoval). Il est cree, modifie et supprime a travers son contrat.
 * Aucune methode POST/PUT/DELETE : Spring MVC repond 405 Method Not Allowed.
 */
@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (lecture seule : ils sont gérés par leur contrat)")
public class PaiementController {

    private final IPaiementService paiementService;

    @Operation(summary = "Lister les paiements", description = "Renvoie tous les paiements, quel que soit leur contrat.")
    @ApiResponse(responseCode = "200", description = "Liste des paiements")
    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @Operation(summary = "Consulter un paiement")
    @ApiResponse(responseCode = "200", description = "Paiement trouvé")
    @ApiResponse(responseCode = "404", description = "Paiement introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Paiement findById(@Parameter(description = "Identifiant du paiement") @PathVariable Long id) {
        return paiementService.findById(id);
    }
}

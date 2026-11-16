package tn.esprit.nomprenomclasse3.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.nomprenomclasse3.domain.Contrat;
import tn.esprit.nomprenomclasse3.service.IContratService;

import java.util.List;

/**
 * API REST des contrats : /api/contrats (le prefixe /api vient de server.servlet.context-path).
 * Controleur mince : il depend uniquement de l'interface IContratService, injectee par constructeur.
 */
@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location et de leurs paiements (composition)")
public class ContratController {

    private final IContratService contratService;

    @Operation(summary = "Lister les contrats", description = "Renvoie tous les contrats (tableau JSON, éventuellement vide).")
    @ApiResponse(responseCode = "200", description = "Liste des contrats")
    @GetMapping
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @Operation(summary = "Consulter un contrat")
    @ApiResponse(responseCode = "200", description = "Contrat trouvé")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Contrat findById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        return contratService.findById(id);
    }

    @Operation(summary = "Créer un contrat", description = "Sans identifiant. Les paiements fournis dans le corps sont enregistrés en cascade (CascadeType.ALL).")
    @ApiResponse(responseCode = "201", description = "Contrat créé")
    @ApiResponse(responseCode = "400", description = "Montant total négatif ou identifiant fourni", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @Operation(summary = "Modifier un contrat", description = "Recopie les attributs modifiables (date, montant, validité) ; les paiements existants sont conservés.")
    @ApiResponse(responseCode = "200", description = "Contrat modifié")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @ApiResponse(responseCode = "400", description = "Montant total négatif", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PutMapping("/{id}")
    public Contrat update(@Parameter(description = "Identifiant du contrat") @PathVariable Long id,
                      @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @Operation(summary = "Supprimer un contrat", description = "Les paiements du contrat sont supprimés avec lui (cascade REMOVE).")
    @ApiResponse(responseCode = "204", description = "Contrat supprimé, réponse sans corps")
    @ApiResponse(responseCode = "404", description = "Contrat introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du contrat") @PathVariable Long id) {
        contratService.deleteById(id);
    }
}

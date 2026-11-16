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
import tn.esprit.nomprenomclasse3.domain.Agence;
import tn.esprit.nomprenomclasse3.service.IAgenceService;

import java.util.List;

/**
 * API REST des agences : /api/agences (le prefixe /api vient de server.servlet.context-path).
 * Controleur mince : il depend uniquement de l'interface IAgenceService, injectee par constructeur.
 */
@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences de location")
public class AgenceController {

    private final IAgenceService agenceService;

    @Operation(summary = "Lister les agences", description = "Renvoie tous les agences (tableau JSON, éventuellement vide).")
    @ApiResponse(responseCode = "200", description = "Liste des agences")
    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @Operation(summary = "Consulter une agence")
    @ApiResponse(responseCode = "200", description = "Agence trouvée")
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Agence findById(@Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        return agenceService.findById(id);
    }

    @Operation(summary = "Créer une agence", description = "Le corps ne doit pas contenir d'identifiant : il est généré par la base.")
    @ApiResponse(responseCode = "201", description = "Agence créée")
    @ApiResponse(responseCode = "400", description = "Nom de l'agence vide ou identifiant fourni", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence create(@RequestBody Agence agence) {
        return agenceService.create(agence);
    }

    @Operation(summary = "Modifier une agence", description = "Recopie les attributs modifiables sur l'agence existante.")
    @ApiResponse(responseCode = "200", description = "Agence modifiée")
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @ApiResponse(responseCode = "400", description = "Nom de l'agence vide", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PutMapping("/{id}")
    public Agence update(@Parameter(description = "Identifiant de l'agence") @PathVariable Long id,
                      @RequestBody Agence agence) {
        return agenceService.update(id, agence);
    }

    @Operation(summary = "Supprimer une agence")
    @ApiResponse(responseCode = "204", description = "Agence supprimée, réponse sans corps")
    @ApiResponse(responseCode = "404", description = "Agence introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de l'agence") @PathVariable Long id) {
        agenceService.deleteById(id);
    }
}

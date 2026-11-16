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
import tn.esprit.nomprenomclasse3.domain.Vehicule;
import tn.esprit.nomprenomclasse3.service.IVehiculeService;

import java.util.List;

/**
 * API REST des véhicules : /api/vehicules (le prefixe /api vient de server.servlet.context-path).
 * Controleur mince : il depend uniquement de l'interface IVehiculeService, injectee par constructeur.
 */
@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "VehiculesDhia", description = "Gestion des véhicules AutoLoc")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les véhicules", description = "Renvoie tous les véhicules (tableau JSON, éventuellement vide).")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules")
    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Vehicule findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Créer un véhicule", description = "Le corps ne doit pas contenir d'identifiant : il est généré par la base.")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = "Tarif journalier négatif ou identifiant fourni", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @Operation(summary = "Modifier un véhicule", description = "Recopie les attributs modifiables sur le véhicule existant.")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @ApiResponse(responseCode = "400", description = "Tarif journalier négatif", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PutMapping("/{id}")
    public Vehicule update(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
                      @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé, réponse sans corps")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }
}

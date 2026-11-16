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
import tn.esprit.nomprenomclasse3.domain.Reservation;
import tn.esprit.nomprenomclasse3.service.IReservationService;

import java.util.List;

/**
 * API REST des réservations : /api/reservations (le prefixe /api vient de server.servlet.context-path).
 * Controleur mince : il depend uniquement de l'interface IReservationService, injectee par constructeur.
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des réservations")
public class ReservationController {

    private final IReservationService reservationService;

    @Operation(summary = "Lister les réservations", description = "Renvoie tous les réservations (tableau JSON, éventuellement vide).")
    @ApiResponse(responseCode = "200", description = "Liste des réservations")
    @GetMapping
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @Operation(summary = "Consulter une réservation")
    @ApiResponse(responseCode = "200", description = "Réservation trouvée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Reservation findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.findById(id);
    }

    @Operation(summary = "Créer une réservation", description = "Le corps ne doit pas contenir d'identifiant : il est généré par la base.")
    @ApiResponse(responseCode = "201", description = "Réservation créée")
    @ApiResponse(responseCode = "400", description = "Date de fin antérieure à la date de début, ou identifiant fourni", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    @Operation(summary = "Modifier une réservation", description = "Recopie les attributs modifiables sur la réservation existante.")
    @ApiResponse(responseCode = "200", description = "Réservation modifiée")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @ApiResponse(responseCode = "400", description = "Date de fin antérieure à la date de début", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PutMapping("/{id}")
    public Reservation update(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
                      @RequestBody Reservation reservation) {
        return reservationService.update(id, reservation);
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponse(responseCode = "204", description = "Réservation supprimée, réponse sans corps")
    @ApiResponse(responseCode = "404", description = "Réservation introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
    }
}

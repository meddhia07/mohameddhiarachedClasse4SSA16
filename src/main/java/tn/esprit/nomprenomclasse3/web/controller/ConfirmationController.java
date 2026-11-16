package tn.esprit.nomprenomclasse3.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.nomprenomclasse3.domain.Contrat;
import tn.esprit.nomprenomclasse3.domain.ModePaiement;
import tn.esprit.nomprenomclasse3.service.IReservationService;
import tn.esprit.nomprenomclasse3.service.confirmation.IConfirmationReservationService;
import tn.esprit.nomprenomclasse3.service.tarification.ITarificationService;

import java.util.Map;

/** Atelier 7 : expose la confirmation d'une reservation et la simulation de prix. */
@RestController
@RequestMapping("/reservations/{id}")
@RequiredArgsConstructor
@Tag(name = "Confirmation (Atelier 7)", description = "Tarification, paiement, contrat et notifications")
public class ConfirmationController {

    private final IConfirmationReservationService confirmationService;
    private final ITarificationService tarificationService;
    private final IReservationService reservationService;

    @Operation(summary = "Simuler le prix d'une réservation", description = "Même calcul qu'à la confirmation, sans payer ni notifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Montant calculé"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    })
    @GetMapping("/prix")
    public Map<String, Object> simulerPrix(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
                                           @RequestParam(required = false) String codePromo) {
        double montant = tarificationService.calculerMontant(reservationService.findById(id), codePromo);
        return Map.of("reservationId", id, "codePromo", codePromo == null ? "" : codePromo, "montant", montant);
    }

    @Operation(summary = "Confirmer une réservation", description = "Calcule le tarif, paie, crée le contrat et publie ContratConfirmeEvent.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class))),
            @ApiResponse(responseCode = "400", description = "Réservation déjà confirmée ou sans véhicule", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    })
    @PostMapping("/confirmation")
    @ResponseStatus(HttpStatus.CREATED)
    public Contrat confirmer(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
                             @RequestParam ModePaiement mode,
                             @RequestParam(required = false) String codePromo,
                             @RequestParam(defaultValue = "SMS") String canal) {
        return confirmationService.confirmer(id, mode, codePromo, canal);
    }
}

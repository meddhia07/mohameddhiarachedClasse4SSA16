package tn.esprit.nomprenomclasse3.web.controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Corps JSON renvoye par GlobalExceptionHandler pour les erreurs 400 et 404.
 */
@Schema(description = "Corps d'erreur renvoyé par l'API (400, 404)")
public record ErreurApi(
        @Schema(description = "Code de statut HTTP", example = "404") int statut,
        @Schema(description = "Libellé du statut", example = "Not Found") String erreur,
        @Schema(description = "Message métier", example = "Contrat introuvable avec l'identifiant 9999") String message,
        @Schema(description = "Date et heure de l'erreur", example = "2026-11-16T20:10:00.123") String horodatage) {
}

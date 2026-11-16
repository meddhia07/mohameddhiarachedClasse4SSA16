package tn.esprit.nomprenomclasse3.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tn.esprit.nomprenomclasse3.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Traduit les exceptions metier levees par la couche Service en reponses HTTP lisibles :
 * ResourceNotFoundException -> 404, IllegalArgumentException -> 400.
 * Corps JSON : statut, erreur, message, horodatage. Les controleurs ne produisent jamais ces codes eux-memes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Sonar java:S8688 : fuseau horaire explicite pour l'horodatage des erreurs
    private static final ZoneId ZONE = ZoneId.of("Africa/Tunis");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErreurApi> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErreurApi> handleBadRequest(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private static ResponseEntity<ErreurApi> build(HttpStatus status, String message) {
        ErreurApi body = new ErreurApi(status.value(), status.getReasonPhrase(), message,
                LocalDateTime.now(ZONE).toString());
        return ResponseEntity.status(status).body(body);
    }
}

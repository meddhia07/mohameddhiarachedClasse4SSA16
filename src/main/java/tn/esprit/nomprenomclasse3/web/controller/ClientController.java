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
import tn.esprit.nomprenomclasse3.domain.Client;
import tn.esprit.nomprenomclasse3.service.IClientService;

import java.util.List;

/**
 * API REST des clients : /api/clients (le prefixe /api vient de server.servlet.context-path).
 * Controleur mince : il depend uniquement de l'interface IClientService, injectee par constructeur.
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final IClientService clientService;

    @Operation(summary = "Lister les clients", description = "Renvoie tous les clients (tableau JSON, éventuellement vide).")
    @ApiResponse(responseCode = "200", description = "Liste des clients")
    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @Operation(summary = "Consulter un client")
    @ApiResponse(responseCode = "200", description = "Client trouvé")
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @GetMapping("/{id}")
    public Client findById(@Parameter(description = "Identifiant du client") @PathVariable Long id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Créer un client", description = "Le corps ne doit pas contenir d'identifiant : il est généré par la base.")
    @ApiResponse(responseCode = "201", description = "Client créé")
    @ApiResponse(responseCode = "400", description = "Nom ou e-mail du client vide, ou identifiant fourni", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        return clientService.create(client);
    }

    @Operation(summary = "Modifier un client", description = "Recopie les attributs modifiables sur le client existant.")
    @ApiResponse(responseCode = "200", description = "Client modifié")
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @ApiResponse(responseCode = "400", description = "Nom ou e-mail du client vide", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @PutMapping("/{id}")
    public Client update(@Parameter(description = "Identifiant du client") @PathVariable Long id,
                      @RequestBody Client client) {
        return clientService.update(id, client);
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponse(responseCode = "204", description = "Client supprimé, réponse sans corps")
    @ApiResponse(responseCode = "404", description = "Client introuvable", content = @Content(schema = @Schema(implementation = ErreurApi.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du client") @PathVariable Long id) {
        clientService.deleteById(id);
    }
}

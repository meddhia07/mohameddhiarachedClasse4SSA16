# API REST AutoLoc — Atelier 5 (Spring MVC & SpringDoc)

Package `tn.esprit.nomprenomclasse3.web.controller` : neuf contrôleurs REST, `GlobalExceptionHandler`
(+ `ErreurApi`, corps des erreurs) et `OpenApiConfig`. Chaque contrôleur est annoté `@RestController`,
`@RequestMapping("/<ressources>")` (sans `/api`), `@RequiredArgsConstructor`, et ne dépend que d'un
champ `final` typé par l'interface `I…Service` (injection par constructeur, aucun accès au repository).

- Préfixe commun défini une seule fois : `server.servlet.context-path=/api` (port `8081`, le 8080 étant
  occupé sur le poste par le listener Oracle).
- Documentation : `http://localhost:8081/api/swagger-ui.html` et `http://localhost:8081/api/v3/api-docs`.
- Collection Postman : `docs/postman/AutoLoc.postman_collection.json` (10 dossiers, 83 requêtes avec
  assertions `pm.test`) et environnement `docs/postman/local.postman_environment.json` (`baseUrl`).
- Captures d'écran : `docs/screenshots/`.

> Remarque sur le modèle : dans notre projet, seule l'association **Contrat ↔ Paiement** est mappée
> (Atelier 2). Les corps imbriqués décrits dans l'énoncé (`"agence": {"id": 1}` pour un employé,
> `client`/`vehicule` pour une réservation, `vehicule` pour une maintenance) ne s'appliquent donc pas
> encore : les règles 400 testées sont celles réellement codées dans les services de l'Atelier 4.

## A. Endpoints

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
|---|---|---|---|---|
| ContratController | GET /api/contrats | `IContratService.findAll()` | 200 | — |
| ContratController | GET /api/contrats/{id} | `findById(id)` | 200 | 404 |
| ContratController | POST /api/contrats | `create(contrat)` (paiements en cascade) | 201 | 400 (id fourni, montant < 0) |
| ContratController | PUT /api/contrats/{id} | `update(id, contrat)` | 200 | 404, 400 (montant < 0) |
| ContratController | DELETE /api/contrats/{id} | `deleteById(id)` (cascade REMOVE) | 204 | 404 |
| PaiementController | GET /api/paiements | `IPaiementService.findAll()` | 200 | — |
| PaiementController | GET /api/paiements/{id} | `findById(id)` | 200 | 404 |
| PaiementController | POST/PUT/DELETE /api/paiements… | aucune méthode | — | 405 |
| AgenceController | GET /api/agences | `IAgenceService.findAll()` | 200 | — |
| AgenceController | GET /api/agences/{id} | `findById(id)` | 200 | 404 |
| AgenceController | POST /api/agences | `create(agence)` | 201 | 400 (nom vide, id fourni) |
| AgenceController | PUT /api/agences/{id} | `update(id, agence)` | 200 | 404, 400 |
| AgenceController | DELETE /api/agences/{id} | `deleteById(id)` | 204 | 404 |
| EmployeController | GET /api/employes | `IEmployeService.findAll()` | 200 | — |
| EmployeController | GET /api/employes/{id} | `findById(id)` | 200 | 404 |
| EmployeController | POST /api/employes | `create(employe)` | 201 | 400 (nom ou rôle manquant) |
| EmployeController | PUT /api/employes/{id} | `update(id, employe)` | 200 | 404, 400 |
| EmployeController | DELETE /api/employes/{id} | `deleteById(id)` | 204 | 404 |
| VehiculeController | GET /api/vehicules | `IVehiculeService.findAll()` | 200 | — |
| VehiculeController | GET /api/vehicules/{id} | `findById(id)` | 200 | 404 |
| VehiculeController | POST /api/vehicules | `create(vehicule)` | 201 | 400 (tarif journalier < 0) |
| VehiculeController | PUT /api/vehicules/{id} | `update(id, vehicule)` | 200 | 404, 400 |
| VehiculeController | DELETE /api/vehicules/{id} | `deleteById(id)` | 204 | 404 |
| EquipementController | GET /api/equipements | `IEquipementService.findAll()` | 200 | — |
| EquipementController | GET /api/equipements/{id} | `findById(id)` | 200 | 404 |
| EquipementController | POST /api/equipements | `create(equipement)` | 201 | 400 (libellé vide) |
| EquipementController | PUT /api/equipements/{id} | `update(id, equipement)` | 200 | 404, 400 |
| EquipementController | DELETE /api/equipements/{id} | `deleteById(id)` | 204 | 404 |
| ClientController | GET /api/clients | `IClientService.findAll()` | 200 | — |
| ClientController | GET /api/clients/{id} | `findById(id)` | 200 | 404 |
| ClientController | POST /api/clients | `create(client)` | 201 | 400 (nom ou e-mail vide) |
| ClientController | PUT /api/clients/{id} | `update(id, client)` | 200 | 404, 400 |
| ClientController | DELETE /api/clients/{id} | `deleteById(id)` | 204 | 404 |
| ReservationController | GET /api/reservations | `IReservationService.findAll()` | 200 | — |
| ReservationController | GET /api/reservations/{id} | `findById(id)` | 200 | 404 |
| ReservationController | POST /api/reservations | `create(reservation)` | 201 | 400 (date de fin antérieure) |
| ReservationController | PUT /api/reservations/{id} | `update(id, reservation)` | 200 | 404, 400 |
| ReservationController | DELETE /api/reservations/{id} | `deleteById(id)` | 204 | 404 |
| MaintenanceController | GET /api/maintenances | `IMaintenanceService.findAll()` | 200 | — |
| MaintenanceController | GET /api/maintenances/{id} | `findById(id)` | 200 | 404 |
| MaintenanceController | POST /api/maintenances | `create(maintenance)` | 201 | 400 (date de fin antérieure) |
| MaintenanceController | PUT /api/maintenances/{id} | `update(id, maintenance)` | 200 | 404, 400 |
| MaintenanceController | DELETE /api/maintenances/{id} | `deleteById(id)` | 204 | 404 |

Les 404 proviennent de `ResourceNotFoundException`, les 400 de `IllegalArgumentException` : les deux
sont levées par la couche Service et traduites par `GlobalExceptionHandler` en un corps JSON
`{statut, erreur, message, horodatage}` (record `ErreurApi`). Le 405 est produit par Spring MVC lui-même
(aucune méthode `@PostMapping` dans `PaiementController`). Les 83 cas de la collection ont été rejoués
contre l'application démarrée : 83/83 conformes.

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
|---|---|---|
| `Contrat.paiements` (`@OneToMany`, cascade ALL, orphanRemoval) | `@JsonManagedReference("contrat-paiements")` | Composition : les paiements naissent dans le JSON de leur contrat. Côté « parent », la liste est sérialisée normalement et, à la lecture, Jackson renseigne automatiquement `paiement.contrat` pour chaque élément. |
| `Paiement.contrat` (`@ManyToOne` LAZY, côté propriétaire) | `@JsonBackReference("contrat-paiements")` | Côté « enfant » : la référence n'est jamais écrite dans le JSON (fin de la boucle Contrat → Paiement → Contrat → … qui provoquait `StackOverflowError`), mais elle est reconstituée à la lecture. Sans elle, les paiements créés en cascade auraient `contrat_id_contrat = NULL` en base. Elle évite aussi la sérialisation du proxy Hibernate LAZY (`ByteBuddyInterceptor`). |
| Autres entités | aucune | Le modèle n'a pas d'autre association : aucune boucle ni proxy à traiter. |

**Différence `@JsonBackReference` / `@JsonIgnore`.** Les deux empêchent l'écriture de la propriété
dans la réponse. Mais `@JsonBackReference` fonctionne en **paire** avec `@JsonManagedReference` (même
nom) : à la **désérialisation**, Jackson rebranche chaque enfant sur son parent. `@JsonIgnore` ignore la
propriété dans les deux sens : elle n'est ni écrite, ni lue. On l'emploie quand l'enfant est créé seul
et désigne son parent par un identifiant (ex. `Agence.vehicules` ignoré, `Vehicule.agence` reçu sous la
forme `{"agence": {"id": 1}}`). Il ne faut pas mettre `@JsonBackReference` sur un attribut qu'on doit
recevoir seul dans un POST (ex. `Reservation.client`) : il serait ignoré à la lecture.

## C. Postman et Swagger

| Outil | Ce qu'il apporte | Exemple concret rencontré |
|---|---|---|
| Postman | Client HTTP pour **tester** : requêtes rejouables, environnement (`{{baseUrl}}`), variables (`contratId` mémorisé après le POST), assertions `pm.test` (onglet *Scripts › After response* / *Tests*). | Scénario de cascade : POST d'un contrat avec deux paiements (201), GET /api/paiements (les deux paiements apparaissent sans avoir été créés séparément), DELETE du contrat (204), puis GET /api/paiements/1 → **404** : la cascade REMOVE est vérifiée automatiquement. POST /api/paiements → **405**. |
| SpringDoc / Swagger UI | **Documente** et permet d'**explorer** l'API : le contrat OpenAPI est généré depuis le code (`/api/v3/api-docs`) et reste synchronisé avec lui ; *Try it out* fournit la commande `curl`, le code et le corps. | Sans aucune annotation, les 9 contrôleurs étaient déjà listés. Après `@Tag`/`@Operation`/`@ApiResponses`, chaque endpoint affiche son résumé et ses codes ; les 404/400 référencent le schéma `ErreurApi`. On y voit aussi un effet de l'exposition des entités : l'exemple de Paiement contient un champ `contrat`, alors qu'il n'est jamais renvoyé (référence arrière). |

Les deux outils sont complémentaires : Postman vérifie le comportement (non-régression), Swagger décrit le
contrat pour les consommateurs de l'API.

## D. Anomalies SonarQube for IDE

Analyse « Analyze All Project Files » de SonarQube for IDE 13.2 : **52 anomalies dans 11 fichiers**
avant correction, **16** après (voir `screenshots/a5_40…a5_45`).

| # | Règle | Où | Explication | Correction |
|---|---|---|---|---|
| 1 | `java:S1710` — *Annotation repetitions should not be wrapped* (maintenabilité) | 8 contrôleurs CRUD + `PaiementController` (33 occurrences) | Depuis Java 8, `@ApiResponse` est `@Repeatable` : le conteneur `@ApiResponses({ … })` est inutile et alourdit la lecture. | Remplacé par des `@ApiResponse` répétés directement sur chaque méthode. La documentation générée est identique (vérifié dans `/api/v3/api-docs` : 200/404, 200/400/404, 201/400). |
| 2 | `java:S8688` — *Time-based `.now()` methods should specify a ZoneId or a Clock* (fiabilité) | `GlobalExceptionHandler` (horodatage des erreurs), `ContratCrudDemo` (2 occurrences) | `now()` sans argument dépend du fuseau de la machine : résultat différent selon le poste ou le serveur, tests non reproductibles. | Fuseau explicite : `LocalDateTime.now(ZONE)` / `LocalDate.now(ZONE)` avec `ZONE = ZoneId.of("Africa/Tunis")`. (L'Atelier 7, mission 2, va plus loin avec un `Clock` injecté.) |
| 3 | `java:S4684` — *Persistent entities should not be used as arguments of `@RequestMapping` methods* (sécurité, bloquant : *mass assignment*) | paramètres `@RequestBody` des 8 contrôleurs CRUD (16 occurrences) | Le client peut envoyer n'importe quel attribut de l'entité JPA (identifiant, `paiements`, …) qui serait recopié en base ; l'API est couplée au schéma. | **Non corrigé volontairement** : l'énoncé impose d'exposer provisoirement les entités. Le risque est limité par les services (identifiant refusé à la création, `update()` ne recopie que les attributs modifiables). Correction prévue : DTO + validation (Chapitre 10). |

Aucune anomalie « import inutile » ni « injection par attribut » : tous les contrôleurs utilisent un champ
`final` injecté par constructeur (`@RequiredArgsConstructor`).

## E. Questions

1. **Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode à appeler ?**
   Le `DispatcherServlet` (front controller de Spring MVC). Il interroge les `HandlerMapping` :
   `RequestMappingHandlerMapping` a indexé au démarrage toutes les méthodes `@GetMapping`, `@PostMapping`…
   des `@RestController` (verbe + chemin, après le context-path `/api`). Le `HandlerAdapter` appelle la
   méthode en résolvant `@PathVariable`/`@RequestBody`, puis le `HttpMessageConverter` Jackson écrit la
   valeur de retour en JSON.
2. **Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de
   `ContratServiceImpl` ?** Respect des couches : la logique métier (id interdit à la création, montant
   positif, recopie sélective dans `update`) est dans le service ; passer par le repository la
   contournerait. Dépendre de l'interface (et non de l'implémentation) applique l'inversion des
   dépendances : couplage faible, implémentation remplaçable (mock en test, autre version) sans toucher au
   contrôleur, injection par constructeur gérée par le conteneur.
3. **Pourquoi POST → 201 et DELETE → 204 ? Pourquoi 404 et non 500 ?** 201 *Created* signale qu'une
   ressource a été créée (le corps contient la ressource et son identifiant) ; 204 *No Content* indique un
   succès sans corps à renvoyer. Une ressource absente est une erreur **du client** (mauvais identifiant),
   pas une panne du serveur : `ResourceNotFoundException` est traduite en 404 par le
   `GlobalExceptionHandler`. Sans ce handler, l'exception non gérée donnerait un 500.
4. **Pourquoi `PaiementController` n'expose-t-il que des GET ? Réponse à un POST /api/paiements ?**
   Paiement est une composition de Contrat (cascade ALL + orphanRemoval) : un paiement n'existe pas sans
   son contrat et se crée/supprime à travers lui ; `IPaiementService` est en lecture seule. Un POST
   /api/paiements reçoit **405 Method Not Allowed** (la route existe, mais pas pour ce verbe).
5. **Risques de l'exposition directe des entités ; `@JsonManagedReference` vs `@JsonIgnore`.**
   Couplage fort API ↔ schéma de base (renommer une colonne casse les clients), champs internes visibles,
   *mass assignment* (un client peut envoyer des champs qu'il ne devrait pas modifier, ex. un id),
   boucles de sérialisation et proxys LAZY, sur/sous-chargement des associations, pas de validation
   dédiée. `Contrat.paiements` utilise `@JsonManagedReference` car c'est une **composition** : les
   paiements sont créés dans le JSON du contrat et doivent être rebranchés sur lui à la lecture.
   `Agence.vehicules` utilise `@JsonIgnore` car un véhicule est créé **seul** et désigne son agence par un
   identifiant : la liste côté inverse n'a pas à être lue ni écrite. Les DTO (Chapitre 10) supprimeront
   ces palliatifs.

## Captures d'écran (`docs/screenshots/`)

| Fichier | Contenu |
|---|---|
| `a5_01_demarrage_intellij.jpg` | Démarrage sans erreur : Tomcat sur 8081, context path `/api` |
| `a5_10` à `a5_16` | CRUD Contrat dans Postman : GET 200, POST 201, POST avec id 400, GET 9999 404, PUT 200 (480 → 520), DELETE 204, DELETE à nouveau 404 |
| `a5_17`, `a5_19` | Cascade : paiements créés avec le contrat, puis 404 après suppression du contrat |
| `a5_18` | POST /api/paiements → 405 Method Not Allowed |
| `a5_30`, `a5_31` | Autres contrôleurs : véhicule avec tarif négatif (400), création d'un employé (201) |
| `a5_20` à `a5_27` | Swagger UI : groupes, Contrats/Paiements, *Try it out* POST 201 et GET 404, schémas, `/api/v3/api-docs` |
| `a5_40` à `a5_45` | SonarQube for IDE : rapport avant (52), règles S8688, S1710, S4684, rapport après (16) |

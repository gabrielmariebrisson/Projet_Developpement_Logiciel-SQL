# client-serveur

Tester avec Firefox ESR version : 102.8.0
Sous environnement : Debian GNU/Linux 10/Windows 11

La base de données se nomme pdl. Les informations de connexion à la base de données sont dans le fichier sqlConnection.java.

Un exemple d'utilisateur (Admin) :
Mail : lol@gmail.com
Mot de passe : lol

Un exemple d'utilisateur classique :
Mail : obelix@gmail.com
Mot de passe : lol

## Installation et exécution

1. Accédez au répertoire pdl-client-serveur : `cd pdl-client-serveur`
2. Compilez et installez les dépendances : `mvn clean install`
3. Lancez l'application : `mvn --projects backend spring-boot:run`

## Base de données

Pour accéder à la base de données, utilisez la commande suivante :
<br>
mysql -u root -p
<br>
<img src="readme/sql.png" width="300px"> 

## Cahier des besoins
Ce projet vise à développer une application de traitement d'image avec une architecture client-serveur. Le document présenté décrit les besoins nécessaires à la réalisation du projet.

Noyau commun :
Chaque groupe doit implémenter le fonctionnement central de l'application client-serveur, tel que décrit dans la section 2. Cette partie du code fera l'objet d'une livraison intermédiaire.

Extensions :
Des suggestions d'extensions seront proposées en cours, telles que la répartition des charges entre client et serveur, l'amélioration de l'interface utilisateur, l'ajout de traitements d'image avancés et la généricité des algorithmes.

Serveur :

Besoin 1 : Initialiser un ensemble d'images présentes sur le serveur.
<br>
Besoin 2 : Gérer les images présentes sur le serveur.
<br>
Besoin 3 : Appliquer un algorithme de traitement d'image.
<br>
<img src="readme/filter.png" width="300px"> 
<br>

Communication :

Besoin 4 : Transférer la liste des images existantes.
<br>
Besoin 5 : Ajouter une image.
<br>
<img src="readme/upload_image.png" width="300px">
<br> 
Besoin 6 : Récupération d'images.
<br>
Besoin 7 : Suppression d'image.
<br>
Besoin 8 : Exécution d'algorithmes par le serveur.
<br>

Client :

Besoin 9 : Parcourir les images disponibles sur le serveur (public).
<br>
Besoin 10 : Parcourir les images disponibles sur le serveur (utilisateur connecté).
<br>
Besoin 11 : Sélectionner une image et lui appliquer un effet.
<br>
Besoin 12 : Enregistrer une image sur disque.
<br>
Besoin 13 : Ajouter une image aux images disponibles sur le serveur.
<br>
Besoin 14 : Suppression d'image.
<br>

Traitement d'images :

Besoin 15 : Réglage de la luminosité.
<br>
Besoin 16 : Égalisation d'histogramme.
<br>
Besoin 17 : Filtre coloré.
<br>
Besoin 18 : Filtres de flou.
<br>
Besoin 19 : Filtre de contour.
<br>
Besoins non-fonctionnels :

Besoin 20 : Compatibilité du serveur.
<br>
Besoin 21 : Compatibilité du client.
<br>
Besoin 22 : Documentation d'installation et de test.
<br>

Résumé des ajouts :

Authentification et autorisation des utilisateurs.
<br>
<img src="readme/login.png" width="300px"> 
<br>
<img src="readme/register.png" width="300px"> 
<br>
Gestion des galeries d'images.
<br>
Système de "coins".
<br>
Profil utilisateur.
<br>
<img src="readme/profil.png" width="300px"> 
<br>
Classements.
<br>
Liker.
<br>
<img src="readme/like.png" width="300px"> 
<br>
Vérification du profil.
<br>
<img src="readme/user_control.png" width="300px"> 
<br>
Modification du statut public/privé.
<br>

Veuillez vous référer au cahier des besoins complet pour plus de détails.
<br>

Assurez-vous de remplacer "readme" par le chemin réel vers les images dans votre référentiel GitHub.

## Auteurs

- Clement Delmas
- Gabriel Marie-Brisson

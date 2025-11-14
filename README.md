# Mini-projet POO – 1ère année  
## Gestion d’objets connectés dans un hôpital

Ce projet est un mini-projet de Programmation Orientée Objet (POO) en Java réalisé en 1ère année à l’EPISEN.  
L’objectif est de simuler la gestion d’objets connectés (IoT) dans un hôpital : capteurs médicaux, patients, abonnements et alertes.

---

## 🎯 Objectifs pédagogiques

- Mettre en pratique les principes de POO en Java :
  - encapsulation
  - héritage
  - classes abstraites
  - polymorphisme
- Manipuler des collections Java (`List`, etc.)
- Gérer la persistance avec des fichiers (CSV + log texte)
- Concevoir une petite application avec :
  - une version console
  - une interface graphique Swing

---

## 🏥 Description fonctionnelle

L’application permet de :

- **Gérer des capteurs connectés** :
  - Tensiomètre
  - Balance connectée
  - Pilulier connecté
  - Oxymètre connecté
  - Glucomètre connecté  
  Chaque capteur possède : un identifiant, un nom, une unité, des seuils minimum/maximum, et une indication si un abonnement est requis.

- **Gérer des patients** :
  - Identifiant
  - Nom
  - Prénom

- **Gérer des abonnements** patient ↔ capteur :
  - Un patient peut être abonné à plusieurs capteurs.
  - Les abonnements sont sauvegardés dans un fichier `abonnements.csv`.

- **Prendre des mesures** :
  - Simulation de mesures (valeurs aléatoires mais cohérentes avec le type de capteur).
  - Vérification automatique des seuils.
  - Affichage d’un message **OK** ou **ALERTE**.
  - Les alertes sont enregistrées dans un fichier `alertes.log`.

---

## 🧱 Architecture du projet

Le code est organisé en plusieurs packages :

### `model`

Contient les classes métier :

- `CapteurConnecte` (classe abstraite)
- `Tensiometre`
- `BalanceConnectee`
- `PilulierConnecte`
- `OxymetreConnecte`
- `GlucometreConnecte`
- `Patient`
- `Abonnement`

### `service`

Services techniques :

- `ServiceAbonnementFichier`  
  - Sauvegarde et chargement des abonnements dans `abonnements.csv`.
- `ServiceAlerte`  
  - Vérifie les seuils, affiche les messages et log les alertes dans `alertes.log`.

### `app`

Classes de lancement :

- `GestionIoTHopital`  
  → version **console** (menu texte).
- `GestionIoTHopitalUI`  
  → version **interface graphique Swing** (onglets pour patients, capteurs, abonnements et mesures).

---

## 💻 Lancement du projet

### Avec IntelliJ IDEA (recommandé)

1. Cloner le dépôt :

   ```bash
   git clone git@github.com:DjibSan/Mini-projet-POO-1ere-Ann-e.git

# 🍽️ Bati-Cuisine - Estimation des Coûts de Construction des Cuisines

## Description
Bati-Cuisine est une application Java destinée aux professionnels de la construction et de la rénovation de cuisines. Elle permet de calculer le coût total des travaux en tenant compte des matériaux utilisés et du coût de la main-d'œuvre, cette dernière étant facturée à l'heure.

## Contexte
L'application vise à offrir aux professionnels un outil puissant pour estimer avec précision les coûts et gérer efficacement les projets de rénovation de cuisines. Elle inclut des fonctionnalités avancées telles que la gestion des clients, la génération de devis personnalisés, et une vue d'ensemble sur les aspects financiers et logistiques des projets.

## ⚙️ Fonctionnalités Principales
- **Gestion des Projets** : Ajouter des clients associés aux projets, gérer les composants (matériaux et main-d'œuvre), et associer un devis pour estimer les coûts.
- **Gestion des Composants** : Gérer les coûts des matériaux et de la main-d'œuvre, en tenant compte des taxes (TVA) et des remises.
- **Gestion des Clients** : Enregistrer les informations de base d’un client, avec différenciation entre clients professionnels et particuliers.
- **Création de Devis** : Générer un devis détaillé incluant une estimation des coûts des matériaux, de la main-d'œuvre, et des taxes.

### Détails des Composants :
- **Models** : Représentent les entités de l'application, telles que `Client`, `Projet`, `Composant`, `Materiel`, `Devis`, et `MainOeuvre`. Chaque modèle encapsule les données et les comportements associés à une entité spécifique.
  
- **Repository** : Centralisation des opérations de récupération et de gestion des données, assurant l'interaction avec la base de données.

- **Service** : Contient la logique métier, gère les appels aux repositories, et traite les règles spécifiques à l'application.

Cette structure permet une évolutivité et une maintenance facilitées, tout en respectant les bonnes pratiques de développement.


## 📂 Contenu de l'Application
- **Projets** : Ajout de clients, matériaux, et calcul des coûts.
- **Matériaux & Main-d'œuvre** : Gestion des coûts et calculs des marges.
- **Clients** : Gestion des informations clients, avec application de remises spécifiques.
- **Devis** : Génération de devis détaillés avant les travaux.

## 🚀 Exécution du Projet
1. Cloner le dépôt et exécuter le projet :
   ```bash
   git clone https://github.com/asmaabarj/BatiCuisine.git
Naviguer vers le répertoire de sortie :
bash
Copier le code
cd Bati-Cuisine/out/artifacts/Bati_Cuisine_jar
Exécuter le fichier .jar :
bash
Copier le code
java -jar .\out\artifacts\BatiCuisine_jar\BatiCuisine.jar

📚 Exemple d'Utilisation
markdown
Copier le code
=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===

=== Menu Principal ===
1. Créer un nouveau projet
2. Afficher les projets existants
3. Calculer le coût d'un projet
4. Quitter

📅 Planification
Pour suivre la planification et les tâches en cours, veuillez consulter notre tableau de bord Jira : 
https://asmaabarj5.atlassian.net/jira/software/projects/BAT/boards/34/


# Santé Locale 🩺

**Santé Locale** est une application mobile native Android conçue pour aider les personnes âgées en Haïti à gérer le pré-diabète. L'application est optimisée pour une utilisation hors-ligne (offline-first) et offre une interface simple avec de grands éléments visuels adaptés aux seniors.

## 🌟 Caractéristiques

- **Suivi de la Glycémie :** Enregistrement facile des taux de sucre avec un pavé numérique personnalisé.
- **Journal d'Activité :** Suivi des minutes d'activité physique quotidiennes.
- **Guide Alimentaire :** Système de "Feu de Signalisation" (Vert, Jaune, Rouge) pour aider à choisir des aliments locaux sains.
- **Historique complet :** Visualisation de toutes les mesures passées.
- **Export PDF :** Génération de rapports de santé professionnels à partager avec un médecin.
- **Rappels Quotidiens :** Notifications programmables pour ne pas oublier de prendre ses mesures.
- **Mode Hors-ligne :** Toutes les données sont stockées localement sur l'appareil.

## 🛠 Tech Stack

- **Langage :** Kotlin
- **UI :** Jetpack Compose (Material 3)
- **Base de données :** Room (Persistence locale)
- **Préférences :** DataStore
- **Images :** Coil (Optimisation du chargement d'images)
- **Export :** Android Graphics PDF

## 🚀 Installation

### Prérequis

1. **Android Studio** (Version Ladybug 2024.2.1 ou plus récente recommandée).
2. **JDK 17** installé et configuré dans Android Studio.
3. Un appareil Android physique ou un émulateur (API 26 / Android 8.0 "Oreo" minimum).

### Étapes

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/votre-utilisateur/sante-locale.git
   cd sante-locale
   ```

2. **Ouvrir le projet :**
   - Lancez Android Studio.
   - Cliquez sur **Open** et sélectionnez le dossier racine du projet `sante-locale`.

3. **Synchronisation Gradle :**
   - Android Studio va automatiquement détecter les fichiers Gradle et commencer la synchronisation.
   - Attendez que le message "Gradle sync finished" apparaisse.

4. **Lancer l'application :**
   - Sélectionnez votre appareil/émulateur dans la barre d'outils.
   - Cliquez sur l'icône **Run** (Flèche verte) ou appuyez sur `Shift + F10`.

## 🍎 Guide Alimentaire (Traffic Light System)

L'application utilise un système de couleurs simple pour aider les utilisateurs à faire des choix sains :

- **🟢 VERT (Bon pour la santé) :** Aliments à index glycémique bas. Ils peuvent être consommés régulièrement sans impact majeur sur le taux de sucre.
- **🟡 JAUNE (Petite portion) :** Aliments à consommer avec modération. Ils contiennent des glucides mais restent acceptables en quantités contrôlées.
- **🔴 ROUGE (Attention danger) :** Aliments à éviter ou à consommer très rarement. Ils provoquent une montée rapide de la glycémie.

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails (si applicable).
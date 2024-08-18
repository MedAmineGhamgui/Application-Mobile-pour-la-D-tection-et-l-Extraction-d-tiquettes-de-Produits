# Détection et Extraction des Données des Étiquettes de Produits en Temps Réel

**Description :**

Ce projet consiste en le développement d'une application pour la détection et l'extraction des données des étiquettes de produits en temps réel. Le système utilise des modèles de détection d'objets YOLO pour localiser les étiquettes des produits et des modèles de reconnaissance optique de caractères (OCR) pour extraire les informations des étiquettes. Le backend est développé avec Flask et l'application mobile est réalisée en utilisant Android Studio avec Java.

**Technologies utilisées :**

- **YOLO (You Only Look Once)** : Modèle de détection d'objets pour localiser les étiquettes des produits dans les images.
- **OCR (Reconnaissance Optique de Caractères)** : Modèle pour extraire les textes des étiquettes des produits.
- **Flask** : Framework web léger pour le backend afin de gérer les requêtes et l'intégration des modèles de détection et de reconnaissance.
- **Android Studio** : IDE pour le développement d'applications Android.
- **Java** : Langage de programmation pour le développement de l'application mobile Android.

**Fonctionnalités principales :**

- **Détection des Étiquettes** : Utilisation des modèles YOLO pour identifier et localiser les étiquettes des produits dans les images capturées en temps réel.
- **Extraction de Données** : Application des modèles OCR pour extraire les informations textuelles des étiquettes des produits.
- **Application Mobile** : Interface utilisateur développée en Java avec Android Studio pour capturer les images et afficher les informations extraites en temps réel.
- **Backend Flask** : Serveur Flask pour traiter les images, appliquer les modèles de détection et de reconnaissance, et renvoyer les résultats à l'application mobile.

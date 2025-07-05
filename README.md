# Guide de déploiement Heroku - Discord2-back

## 📋 Structure du projet
```
javadiscord/
├── back/           # Code Spring Boot + Dockerfile
├── docker/         # docker-compose.yml (BDD locale)
└── desktop/        # Application frontend
```

## 🚀 Commandes exécutées

### 1. Vérification de l'installation Heroku
```bash
heroku --version
```
Vérifie que Heroku CLI est installé (version 10.0.0 détectée)

### 2. Connexion à Heroku
```bash
cd .\docker\
heroku login
```
Se connecte au compte Heroku (doniczka.quentin.game@gmail.com)

### 3. Création de l'application
```bash
heroku create discord2-back --region eu
```
Crée l'app "discord2-back" dans la région Europe avec URL générée automatiquement

### 4. Ajout de la base de données PostgreSQL
```bash
heroku addons:create heroku-postgresql:essential-0 -a discord2-back
```
Ajoute une base PostgreSQL payante ($5/mois max) après échec des plans gratuits

### 5. Vérification de la base de données
```bash
heroku config:get DATABASE_URL -a discord2-back
```
Tente de récupérer l'URL de la base (vide au début, DB en cours de création)

### 6. Configuration des variables d'environnement
```bash
cd ..\back
heroku config:set DB_USER="ufj0o60ed44g3v" DB_PASSWORD="p85187bdef4e4374..." JWT_SECRET="votre_secret_jwt" -a discord2-back
```
Configure les variables pour la connexion DB et la sécurité JWT

### 7. Connexion au registre Docker d'Heroku
```bash
heroku container:login
```
S'authentifie pour pouvoir push des images Docker vers Heroku

### 8. Changement vers stack Container
```bash
heroku stack:set container -a discord2-back
```
Configure l'app pour utiliser Docker au lieu du buildpack par défaut

### 9. Tentative de push Docker (échec)
```bash
heroku container:push web -a discord2-back
```
Erreur "No images to push" - le Dockerfile n'a pas été buildé localement

## 🔧 Prochaines étapes nécessaires

### 10. Build et push de l'image Docker
```bash
heroku container:push web -a discord2-back
```
Build automatiquement l'image depuis le Dockerfile et la push

### 11. Release de l'application
```bash
heroku container:release web -a discord2-back
```
Déploie l'image pushée et démarre l'application

### 12. Surveillance des logs
```bash
heroku logs --tail -a discord2-back
```
Surveille les logs en temps réel pour vérifier le bon démarrage

## 📍 URL finale
https://discord2-back-17feecd5a4fc.herokuapp.com/
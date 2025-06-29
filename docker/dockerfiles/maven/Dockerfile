# Étape 1 : Build avec Maven et Java 21
FROM maven:3.9.9-amazoncorretto-21 AS build

WORKDIR /app

# Copie du pom.xml en premier pour optimiser le cache Docker
COPY pom.xml .

# Téléchargement des dépendances
RUN mvn dependency:go-offline -B

# Copie du code source
COPY ../../../back/src ./src

# Construction du projet
RUN mvn clean package -DskipTests

# Étape 2 : Runtime avec JRE léger
FROM amazoncorretto:21-alpine AS runtime

WORKDIR /app

# Copie du JAR généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Création d'un utilisateur non-root pour la sécurité
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Port exposé
EXPOSE 8080

# Point d'entrée
ENTRYPOINT ["java", "-jar", "app.jar"]
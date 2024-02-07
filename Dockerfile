# Utiliser une image de base officielle Java 17
FROM openjdk:17-jdk-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar compilé dans l'image
COPY target/spring-boot-jpa-h2-0.0.1-SNAPSHOT.jar app.jar

# Exécuter l'application
CMD ["java", "-jar", "app.jar"]

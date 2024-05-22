# Usar la imagen oficial de OpenJDK 21 como base
FROM openjdk:21-jdk

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/4chat-backend.jar /app/4chat-backend.jar

# Copiar el archivo de configuración de properties
COPY src/main/resources/application-docker.properties /config/application-docker.properties

# Copiar el archivo de configuración de Logback
COPY src/main/resources/logback-spring.xml /app/logback-spring.xml

# Exponer el puerto que utiliza tu aplicación
EXPOSE 8080

# Definir un volumen para los logs
VOLUME /app/logs

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/4chat-backend.jar", "--spring.config.location=/config/application-docker.properties"]


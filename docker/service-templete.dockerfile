FROM maven:3.9.14-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy the dependency specifications
COPY pom.xml pom.xml
COPY common/pom.xml common/pom.xml
COPY service-templete/pom.xml service-templete/pom.xml
COPY maui-backend/pom.xml maui-backend/pom.xml

# Install parent POM to local repository
RUN mvn -q -ntp -B -N install

# Resolve dependencies for `common` module, e.g., shared libraries
# Also build all the required projects needed by the common module.
# In this case, it will also resolve dependencies for the `root` module.
RUN mvn -q -ntp -B -pl common -am dependency:go-offline
# Copy full sources for `common` module
COPY common common
# Install the common module in the local Maven repo (`.m2`)
# This will also install the `root` module.
# See: `la -lat ~/.m2/repository/org/example/*/*`
RUN mvn -q -B -pl common install

# Resolve dependencies for the main application
RUN mvn -q -ntp -B -pl service-templete -am dependency:go-offline
# Copy sources for main application
COPY service-templete service-templete

# RUN mvn -q -ntp -B -pl service-templete clean package -DskipTests
# Package the common and application modules together
RUN mvn -q -ntp -B -pl common,service-templete package -DskipTests

RUN mkdir -p /jar-layers
WORKDIR /jar-layers
# Extract JAR layers
RUN java -Djarmode=layertools -jar /build/service-templete/target/*.jar extract

FROM eclipse-temurin:21-jre-alpine

RUN mkdir -p /app
WORKDIR /app

# Copy JAR layers, layers that change more often should go at the end
#COPY --from=builder /jar-layers/dependencies/ ./
#COPY --from=builder /jar-layers/spring-boot-loader/ ./
#COPY --from=builder /jar-layers/snapshot-dependencies/ ./
#COPY --from=builder /jar-layers/application/ ./

COPY --from=builder /build/service-templete/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
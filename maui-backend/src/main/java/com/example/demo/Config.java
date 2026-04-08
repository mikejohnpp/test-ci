package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class Config implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    // cho phép hiển thị id
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {


        config.exposeIdsFor(entityManager.getMetamodel().getEntities()
                .stream()
                .map(EntityType::getJavaType)
                .toArray(Class[]::new));

    }

}

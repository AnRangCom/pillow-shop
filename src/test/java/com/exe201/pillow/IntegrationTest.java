package com.exe201.pillow;

import com.exe201.pillow.config.AsyncSyncConfiguration;
import com.exe201.pillow.config.DatabaseTestcontainer;
import com.exe201.pillow.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        PillowShopApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.exe201.pillow.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}

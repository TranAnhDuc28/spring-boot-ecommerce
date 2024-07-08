package com.demo.ecommerce.config;

import com.demo.ecommerce.entity.Country;
import com.demo.ecommerce.entity.Product;
import com.demo.ecommerce.entity.ProductCategory;
import com.demo.ecommerce.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private final EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*");

        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        // disable HTTP methods for Product: PUT, POST and DELETE
        disableHttpMethod(Product.class, config, theUnsupportedActions);
        disableHttpMethod(ProductCategory.class, config, theUnsupportedActions);
        disableHttpMethod(Country.class, config, theUnsupportedActions);
        disableHttpMethod(State.class, config, theUnsupportedActions);

        // call an internal helper method
        exposeIds(config);
    }

    /**
     *  disable HTTP methods for entity
     *
     * @param theClass
     * @param config
     * @param theUnsupportedActions
     */
    private void disableHttpMethod(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass) // áp dụng cho entity nào
                // phương thức HTTP nào sẽ bị tắt trên các mục cụ thể của entity.
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                // phương thức HTTP nào sẽ bị tắt trên tập hợp của entity.
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    /**
     * Cấu hình Spring Data REST để tự động lộ diện các ID của các entity trong các endpoint REST.
     * Điều này có thể rất hữu ích khi muốn các ID của các entity được bao gồm trong phản hồi JSON của API REST.
     *
     * @param config
     */
    private void exposeIds(RepositoryRestConfiguration config) {
        // expose entity ids
        // get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // get the entity types for the entities
        for (EntityType tempEntityType: entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);

        // cấu hình để Spring Data REST bao gồm ID của các entity này trong các phản hồi REST của chúng
        config.exposeIdsFor(domainTypes);
    }
}

package me.michalwozniak.noteswebservice;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Tweaked version of:
 * https://github.com/spring-projects/spring-hateoas/commit/a56392ac7c48b57322c71ef9c2592dcffe2a1354#diff-993b5fa03433e8b16dec8b012358079b
 */
public interface DtoResourceAssembler<T, D extends ResourceSupport> extends ResourceAssembler<T, D> {

    /**
     * Converts the given entity into a {@link D}.
     *
     * @param entity must not be {@literal null}.
     * @return {@link D}
     */
    default D toResource(T entity) {
        Assert.notNull(entity, "entity must not be null!");
        D resource = convertToDto(entity);
        addLinks(resource);
        return resource;
    }

    /**
     * Converts all given entities into resources and wraps the collection as a resource as well.
     *
     * @see #toResource(Object)
     * @param entities must not be {@literal null}.
     * @return {@link Resources} containing {@link D}.
     */
    default Resources<D> toResources(Iterable<? extends T> entities) {

        Assert.notNull(entities, "entities must not be null!");
        List<D> resourceList = new ArrayList<>();

        for (T entity : entities) {
            resourceList.add(toResource(entity));
        }

        Resources<D> resources = new Resources<>(resourceList);
        addLinks(resources);
        return resources;
    }

    /**
     * Define links to add to every individual {@link D}.
     *
     * @param resource
     */
    void addLinks(D resource);

    /**
     * Define links to add to the {@link Resources<D>} collection.
     *
     * @param resources
     */
    void addLinks(Resources<D> resources);

    /**
     * Performs conversion from {@link T} to {@link D}
     *
     * @param entity never {@literal null}.
     * @return {@link D}
     */
    D convertToDto(T entity);
}

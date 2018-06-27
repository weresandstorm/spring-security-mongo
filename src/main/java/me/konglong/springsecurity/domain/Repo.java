/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

import java.util.List;
import java.util.Optional;

public interface Repo<T extends Entity<ID>, ID> {

  /**
   * Saves the given entity. If the entity is missing an identifier, the underlying implementation
   * should generate one.
   */
  T save(T entity);

  /**
   * Updates the matched entity according to the given {@code newEntity}'s id.
   */
  boolean update(T newEntity);

  /**
   * Deletes the entity with given {@code id}.
   */
  boolean delete(ID id);

  /**
   * Retrieves the entity with given {@code id}. If not exist, throw an {@link
   * RepositoryException}.
   */
  T get(ID id);

  /**
   * Retrieves the entity with given {@code id}, and whether or not the entity exists, wrap the
   * result in an {@link Optional}.
   */
  Optional<T> getOptional(ID id);

  List<T> findAll();

}

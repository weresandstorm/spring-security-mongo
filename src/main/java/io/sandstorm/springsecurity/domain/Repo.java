/*
 * Copyright (C) 2018 The Sandstorm Org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sandstorm.springsecurity.domain;

import java.util.List;
import java.util.Optional;

public interface Repo<T extends Entity<ID>, ID> {

  /**
   * Saves the given entity. If the entity is missing an identifier, the underlying implementation
   * should generate one.
   */
  T save(T entity);

  /** Updates the matched entity according to the given {@code newEntity}'s id. */
  boolean update(T newEntity);

  /** Deletes the entity with given {@code id}. */
  boolean delete(ID id);

  /**
   * Retrieves the entity with given {@code id}. If not exist, throw an {@link RepositoryException}.
   */
  T get(ID id);

  /**
   * Retrieves the entity with given {@code id}, and whether or not the entity exists, wrap the
   * result in an {@link Optional}.
   */
  Optional<T> getOptional(ID id);

  List<T> findAll();
}

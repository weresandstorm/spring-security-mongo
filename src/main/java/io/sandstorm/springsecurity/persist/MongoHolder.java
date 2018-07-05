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

package io.sandstorm.springsecurity.persist;

import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoHolder {

  private MongoDatabase targetDb;

  @Autowired
  public MongoHolder(MongoProvider mongoProvider) {
    targetDb = mongoProvider.targetDb();
  }

  /**
   * Returns the holded target {@link MongoDatabase} to store all security-related data into.
   *
   * @return a {@link MongoDatabase}
   */
  public MongoDatabase targetDb() {
    return targetDb;
  }
}

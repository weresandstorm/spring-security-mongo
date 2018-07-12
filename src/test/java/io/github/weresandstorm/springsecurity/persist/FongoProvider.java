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

package io.github.weresandstorm.springsecurity.persist;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PreDestroy;

public class FongoProvider implements MongoProvider {

  private Fongo fongo;
  private MongoClient mongoClient;

  public FongoProvider() {
    this.fongo = new Fongo("test", Fongo.V3_3_SERVER_VERSION, CodecRegistryProvider.compositeAll());
    this.mongoClient = fongo.getMongo();
  }

  @Override
  public MongoDatabase targetDb() {
    return fongo.getDatabase("identity-test");
  }

  @PreDestroy
  public void cleanUp() {
    mongoClient.close();
  }
}

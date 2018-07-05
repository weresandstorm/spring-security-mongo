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

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PreDestroy;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealMongoProvider implements MongoProvider {

  private MongoConfProps mongoConfProps;
  private MongoClient mongoClient;

  @Autowired
  public RealMongoProvider(MongoConfProps mongoConfProps) {
    this.mongoConfProps = mongoConfProps;
    CodecRegistry codecRegistry =
        fromRegistries(
            CodecRegistryProvider.fromCustomCodecs(),
            CodecRegistryProvider.fromCustomCodecProviders(),
            MongoClient.getDefaultCodecRegistry(),
            CodecRegistryProvider.fromPojoCodecProvider());
    this.mongoClient =
        new MongoClient(
            mongoConfProps.host, MongoClientOptions.builder().codecRegistry(codecRegistry).build());
  }

  @Override
  public MongoDatabase targetDb() {
    return mongoClient.getDatabase(mongoConfProps.database);
  }

  @PreDestroy
  public void cleanUp() {
    mongoClient.close();
  }
}

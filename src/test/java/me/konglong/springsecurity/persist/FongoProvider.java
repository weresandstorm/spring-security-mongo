/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PreDestroy;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FongoProvider implements MongoProvider {

  private MongoConfProps mongoConfProps;
  private MongoClient mongoClient;

  @Autowired
  public FongoProvider(MongoConfProps mongoConfProps) {
    this.mongoConfProps = mongoConfProps;
    CodecRegistry pojoCodecRegistry =
        fromRegistries(
            MongoClient.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    Fongo fongo =
        new Fongo(mongoConfProps.database, Fongo.DEFAULT_SERVER_VERSION, pojoCodecRegistry);
    this.mongoClient = fongo.getMongo();
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

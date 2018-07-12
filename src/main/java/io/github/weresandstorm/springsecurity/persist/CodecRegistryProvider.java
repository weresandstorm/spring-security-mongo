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

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public final class CodecRegistryProvider {

  private CodecRegistryProvider() {}

  public static final CodecRegistry fromCustomCodecs() {
    return fromCodecs(new LocalDateTimeCodec(), new ObjectCodec());
  }

  public static final CodecRegistry fromCustomCodecProviders() {
    return fromProviders(new GrantedAuthorityCodecProvider());
  }

  public static final CodecRegistry fromPojoCodecProvider() {
    PojoCodecProvider pojoCodecProvider =
        PojoCodecProvider.builder()
            .register("me.konglong.springsecurity.domain")
            .automatic(true)
            .build();
    return fromProviders(pojoCodecProvider);
  }

  public static final CodecRegistry compositeAll() {
    return fromRegistries(
        CodecRegistryProvider.fromCustomCodecs(),
        CodecRegistryProvider.fromCustomCodecProviders(),
        MongoClient.getDefaultCodecRegistry(),
        CodecRegistryProvider.fromPojoCodecProvider());
  }
}

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

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class GrantedAuthorityCodec implements Codec<GrantedAuthority> {

  @Override
  public GrantedAuthority decode(BsonReader reader, DecoderContext decoderContext) {
    reader.readStartDocument();
    String role = reader.readString("role");
    reader.readEndDocument();
    return new SimpleGrantedAuthority(role);
  }

  @Override
  public void encode(BsonWriter writer, GrantedAuthority value, EncoderContext encoderContext) {
    writer.writeStartDocument();
    writer.writeString("role", value.getAuthority());
    writer.writeEndDocument();
  }

  @Override
  public Class getEncoderClass() {
    return GrantedAuthority.class;
  }
}

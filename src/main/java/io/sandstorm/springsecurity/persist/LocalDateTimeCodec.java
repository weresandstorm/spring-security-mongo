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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class LocalDateTimeCodec implements Codec<LocalDateTime> {

  @Override
  public LocalDateTime decode(BsonReader reader, DecoderContext decoderContext) {
    Instant instant = Instant.ofEpochMilli(reader.readDateTime());
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
  }

  @Override
  public void encode(BsonWriter writer, LocalDateTime value, EncoderContext encoderContext) {
    writer.writeDateTime(value.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
  }

  @Override
  public Class<LocalDateTime> getEncoderClass() {
    return LocalDateTime.class;
  }
}

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

package io.sandstorm.springsecurity.config;

import io.sandstorm.springsecurity.persist.MongoConfProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Note: @EnableConfigurationProperties must be used with annotation @Configuration,
// and @EnableConfigurationProperties not only registers the specified ConfigurationProperties bean
// which
// is annotated by @ConfigurationProperties, but also enables populating the specified
// config-props-class bean
// with properties from property sources.

@Configuration
@ComponentScan(basePackages = {"io.sandstorm.springsecurity.persist"})
@EnableConfigurationProperties(MongoConfProps.class)
public class PersistConf {}

/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.persist;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Note: @EnableConfigurationProperties must be used with annotation @Configuration,
// and @EnableConfigurationProperties not only registers the specified ConfigurationProperties bean which
// is annotated by @ConfigurationProperties, but also enables populating the specified config-props-class bean
// with properties from property sources.

@Configuration
@ComponentScan(basePackages = {"me.konglong.springsecurity.persist"})
@EnableConfigurationProperties(MongoConfProps.class)
public class PersistConf {}

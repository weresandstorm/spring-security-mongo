/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package me.konglong.springsecurity.domain;

public class RepositoryException extends RuntimeException {

  public RepositoryException(String message) {
    super(message);
  }

  public RepositoryException(Throwable cause) {
    super(cause);
  }

  public RepositoryException(String message, Throwable cause) {
    super(cause);
  }

}

package edu.ifmg.produtos.services.exceptions;

public class EmailException extends RuntimeException {
  public EmailException(String message){
    super(message);
  }
}
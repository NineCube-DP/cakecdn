/* (C)2023 */
package pl.ninecube.oss.cakecdn.exception;

public class BusinessException extends RuntimeException {

  public BusinessException(String description) {
    super(description);
  }
}

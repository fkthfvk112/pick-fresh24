package mart.fresh.com.error.exception;

import lombok.Builder;
import mart.fresh.com.error.ErrorCode;

public class CartProductException extends BusinessException {
	public CartProductException(ErrorCode errorCode) {
		super(errorCode);
	}
}

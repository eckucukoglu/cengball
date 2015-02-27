package utils;

import utils.Enums.ExceptionType;

public final class CengBallException extends Exception {
	private ExceptionType exceptionType;
	
	private static final long serialVersionUID = 1L;

	public CengBallException(ExceptionType exception) {
		setExceptionType(exception);
	}
	
	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	
}

package eap.exception;

/**
 * <p> Title: </p>
 * <p> Description: </p>
 * @作者 chiknin@gmail.com
 * @创建时间 
 * @版本 1.00
 * @修改记录
 * <pre>
 * 版本       修改人         修改时间         修改内容描述
 * ----------------------------------------
 * 
 * ----------------------------------------
 * </pre>
 */
public class RollbackableBizException extends BizException {
	
	public RollbackableBizException() {
		super();
	}
	
	public RollbackableBizException(BizException e) {
		super(e.getMessage(), e);
		this.errorMsg = e.errorMsg;
		this.setModel(e.getModel());
	}
	
	public RollbackableBizException(String message) {
		super(message);
	}
	public RollbackableBizException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public RollbackableBizException(String msgCode, String[] msgParams, String message, Throwable cause) {
		super(message, cause);
		this.errorMsg = new ErrorMsg(msgCode, msgParams);
	}
	
	public RollbackableBizException(String msgCode, String message, Throwable cause) {
		super(message, cause);
		this.errorMsg = new ErrorMsg(msgCode);
	}
	
	public RollbackableBizException(String msgCode, String message) {
		super(message);
		this.errorMsg = new ErrorMsg(msgCode);
	}
	
	public RollbackableBizException(String msgCode, String msgParam, String message, Throwable cause) {
		super(message, cause);
		this.errorMsg = new ErrorMsg(msgCode, msgParam);
	}
	public RollbackableBizException(String msgCode, String msgParam, String message) {
		super(message);
		this.errorMsg = new ErrorMsg(msgCode, msgParam);
	}
	
	public static void throwRollbackableBizException(Exception e) throws RollbackableBizException {
		if (e instanceof RollbackableBizException) {
			throw (RollbackableBizException) e;
		} 
		else if (e instanceof BizException) {
			throw new RollbackableBizException((BizException) e);
		} 
		else {
			throw new RollbackableBizException(e.getMessage(), e);
		}
	}
}
package de.mhus.micro.core.api;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.operation.OperationDescription;

public class MicroResult {

    public static final int OK = 0;
    
    private OperationDescription description;
    private IConfig result;
    private int rc;
    private String msg;
    private boolean transportSuccessful;
	private Throwable error;

    public MicroResult(boolean transportSuccessful, int rc, String msg, OperationDescription description, IConfig result) {
        this.transportSuccessful = transportSuccessful;
        this.rc = rc;
        this.msg = msg;
        this.description = description;
        this.result = result;
    }
    public MicroResult(OperationDescription desc, Throwable t) {
        this.transportSuccessful = false;
        this.rc = -500;
        this.msg = t.toString();
        this.description = desc;
        this.result = null;
        this.error = t;
	}
    
	public OperationDescription getDescription() {
        return description;
    }
    public IConfig getResult() {
        return result;
    }
    public int getReturnCode() {
        return rc;
    }

    public String getMessage() {
        return msg;
    }
    public boolean isTransportSuccessful() {
        return transportSuccessful;
    }
    public Throwable getError() {
    	return error;
    }
    
    
    @Override
    public String toString() {
        return transportSuccessful + "," + rc + "," + msg + "," + (description != null ? description.getPath() : "?") + "," + result;
    }
}

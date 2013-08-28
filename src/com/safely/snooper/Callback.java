package com.safely.snooper;

public interface Callback <DataType>{
	public void onSuccess(DataType type);
	
	public void onFailure();
}

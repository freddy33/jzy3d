package org.jzy3d.demos;

public abstract class AbstractDemo implements IDemo{
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String getPitch(){
		return "";
	}
}

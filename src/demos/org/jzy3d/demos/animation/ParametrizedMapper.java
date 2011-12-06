package org.jzy3d.demos.animation;

import org.jzy3d.plot3d.builder.Mapper;

public abstract class ParametrizedMapper extends Mapper{
	public ParametrizedMapper(double p){
		this.p = p;
	}
	
	public void setParam(double p){
		this.p = p;
	}
	
	public double getParam(){
		return p;
	}
	
	protected double p;
}

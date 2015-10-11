/**
 * 
 */
package com.bnsf.drools.poc.model;

import java.io.Serializable;

/**
 * Represents a persistable entity
 * 
 * @author rakesh
 *
 */
public interface BNSFModel<ID extends Serializable> {
	/**
	 * @return unique id for the model
	 */
	public abstract ID getId();
}

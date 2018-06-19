package com.trgr.dockets.core.util;

import org.apache.log4j.Logger;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDFactory;

/**
 * Factory class to create a UUID in a thread-safe way.
 */
public final class UUIDGenerator {
	
	private static final Logger log = Logger.getLogger(UUIDGenerator.class);
    private static final UUIDFactory uuidFactory;
    
    static {
    	try {
			uuidFactory = new UUIDFactory();
		} catch (Exception e) {
            String errorMessage = "Failed to construct " + UUIDFactory.class;
            throw new RuntimeException(errorMessage, e);
		}
    }
    
	/**
	 * Generate a UUID in a thread-safe way.
	 * @return a generated UUID
	 */
	public static synchronized UUID createUuid()  {
		UUID rawUuid = null;
		try {
			rawUuid = uuidFactory.getUUIDThreadSafe();
		} catch (Exception e) {
			log.error("UUID generation error: " + e.getMessage());
		}
		return rawUuid;
	}
}

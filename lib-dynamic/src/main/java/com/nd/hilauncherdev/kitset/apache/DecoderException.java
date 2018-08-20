/**
 * Create Date:2011-8-24下午03:14:38
 */
package com.nd.hilauncherdev.kitset.apache;

/**
 * Thrown when a Decoder has encountered a failure condition during a decode. 
 * 
 * @author Apache Software Foundation
 * @version $Id: DecoderException.java,v 1.9 2004/02/29 04:08:31 tobrien Exp $
 */
@SuppressWarnings("serial")
public class DecoderException extends Exception {

    /**
     * Creates a DecoderException
     * 
     * @param pMessage A message with meaning to a human
     */
    public DecoderException(String pMessage) {
        super(pMessage);
    }

}

/**
 * Create Date:2011-8-24下午03:16:11
 */
package com.nd.hilauncherdev.kitset.apache;

/**
 * Thrown when there is a failure condition during the encoding process.  This
 * exception is thrown when an Encoder encounters a encoding specific exception
 * such as invalid data, inability to calculate a checksum, characters outside of the 
 * expected range.
 * 
 * @author Apache Software Foundation
 * @version $Id: EncoderException.java,v 1.10 2004/02/29 04:08:31 tobrien Exp $
 */
@SuppressWarnings("serial")
public class EncoderException extends Exception {

    /**
     * Creates a new instance of this exception with an useful message.
     * 
     * @param pMessage a useful message relating to the encoder specific error.
     */
    public EncoderException(String pMessage) {
        super(pMessage);
    }
}

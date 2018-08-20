package com.felink.corelib.net.base;

/**
 * Created by dingdj on 2016/6/1.
 */
public class CharArrayBuffer {

    private char[] buffer;
    private int len;


    /**
     * Creates an instance of {@link CharArrayBuffer} with the given initial
     * capacity.
     *
     * @param capacity the capacity
     */
    public CharArrayBuffer(final int capacity) {
        super();
        this.buffer = new char[capacity];
    }


    /**
     * Appends <code>len</code> chars to this buffer from the given source
     * array starting at index <code>off</code>. The capacity of the buffer
     * is increased, if necessary, to accommodate all <code>len</code> chars.
     *
     * @param   b        the chars to be appended.
     * @param   off      the index of the first char to append.
     * @param   len      the number of chars to append.
     * @throws IndexOutOfBoundsException if <code>off</code> is out of
     * range, <code>len</code> is negative, or
     * <code>off</code> + <code>len</code> is out of range.
     */
    public void append(final char[] b, final int off, final int len) {
        if (b == null) {
            return;
        }
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) < 0) || ((off + len) > b.length)) {
            throw new IndexOutOfBoundsException("off: "+off+" len: "+len+" b.length: "+b.length);
        }
        if (len == 0) {
            return;
        }
        final int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            expand(newlen);
        }
        System.arraycopy(b, off, this.buffer, this.len, len);
        this.len = newlen;
    }

    private void expand(final int newlen) {
        final char newbuffer[] = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }


    @Override
    public String toString() {
        return new String(this.buffer, 0, this.len);
    }

}

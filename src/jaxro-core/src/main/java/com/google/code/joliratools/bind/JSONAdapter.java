package com.google.code.joliratools.bind;

/**
 * @author "Gabe Hopper"
 * 
 */
public interface JSONAdapter {

    /**
     * @param writer
     */
    public abstract void toJSON(java.io.PrintWriter writer);

}
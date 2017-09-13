/*******************************************************************************
 * Copyright (c) 2001, 2017 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] http://openjdk.java.net/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *******************************************************************************/
package com.ibm.jvmti.tests.util;

public class SyncThread extends Thread 
{
    protected Object lock = new Object();
    protected volatile boolean notified = false;

    public SyncThread() 
    {
        super();
    }
    
    public SyncThread(ThreadGroup group, String name) 
    {
        super(group, name);
    }
    
    
    public void syncStart() 
    {

    	synchronized(lock) 
    	{
            super.start();
            try {
            	while(!notified) {
            		lock.wait();
            	}
            } catch (java.lang.InterruptedException ie) {
            	interrupt();
            }
    	}
    }
    
    public void run() 
    {
        synchronized(lock) {
        	notified = true;
            lock.notify();
        }
        while(!isInterrupted() && work()) {
        	yield();
        }
    }
        
    protected boolean work() 
    {
        yield();
        return true;
    }
}
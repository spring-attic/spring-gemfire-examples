/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.gemfire.examples.util;

/**
 * @author Wayne Lund
 * @author David Turanski
 *
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class PersistenceDirectoryGenerator {

    private File parent;
    private volatile FileLock lock;
    private File selected;
    
    public void setParent(File f) {
        parent = f;
    }

    public File getParent() {
        return parent;
    }

    public void unlock() {
        FileLock lock = this.lock;
        this.lock = null;
        if (lock != null) {
            try {
                lock.release();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
            lock = null;
        }
        if (selected!=null) {
            selected.delete();
        }
        this.selected = null;
    }
    
    public File createDirectory(String folder) {
        if (this.selected==null) {
            findDirectory();
        }
        File f = new File(selected, folder);
        if (f.exists() && f.isDirectory()) {
            return f;
        }
        if (!f.mkdirs()) {
            throw new RuntimeException("Unable to create directory:"+f);
        }
        return f;
       
    }

    public synchronized File findDirectory() {
        if (lock!=null) {
            return selected;
        }
        for (int i = 0; i < 100; i++) {
            File dir = new File(parent, "node" + i);
            System.err.println("Dir Name: " + dir.getAbsolutePath());
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new RuntimeException("Unable to create directory for persistence:" + dir);
                }
            }
            if (checkDirectory(dir)) {
                System.err.println("Directory for persistence is:"+dir);
                selected = dir.getAbsoluteFile();
                selected.deleteOnExit();
                return selected;
            }
        }
        throw new RuntimeException("Unable to locate persistence directory.");
    }

    public boolean checkDirectory(File f) {
        return lock(f);
    }

    private boolean lock(File directory) {
        String name = "node.lock";
        File lockfile = new File(directory, name);
        lockfile.deleteOnExit();
        try {
            RandomAccessFile rf = new RandomAccessFile(lockfile, "rw");
            FileChannel fc = rf.getChannel();
            System.err.println("Attempting to lock:"+lockfile);
            lock = fc.tryLock();
            System.err.println("Locked:"+lock);
            if (lock!=null) {
                //gemfire doesn't clean up
                File f = new File(directory,"BACKUPdefault.if");
                boolean result = true;
                if (f.exists()) result = f.delete();
                f = new File(directory,"DRLK_IFdefault.lk");
                if (result && f.exists()) result = f.delete();
                return result;
            }
        } catch (IOException x) {

        } catch (OverlappingFileLockException e) {
            // File is already locked in this thread or virtual machine
        }
        return false;
    }

    public File getSelected() {
        return selected;
    }

    public void setSelected(File selected) {
        this.selected = selected;
    }

}
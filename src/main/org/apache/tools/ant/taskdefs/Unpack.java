/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.taskdefs;


import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Abstract Base class for unpack tasks.
 *
 * @author <a href="mailto:umagesh@apache.org">Magesh Umasankar</a>
 */

public abstract class Unpack extends Task {

    protected File source;
    protected File dest;

    /**
     * @deprecated setSrc(String) is deprecated and is replaced with
     *             setSrc(File) to make Ant's Introspection
     *             mechanism do the work and also to encapsulate operations on
     *             the type in its own class.
     */
    public void setSrc(String src) {
        log("DEPRECATED - The setSrc(String) method has been deprecated."
            + " Use setSrc(File) instead.");
        setSrc(project.resolveFile(src));
    }

    /**
     * @deprecated setDest(String) is deprecated and is replaced with
     *             setDest(File) to make Ant's Introspection
     *             mechanism do the work and also to encapsulate operations on
     *             the type in its own class.
     */
    public void setDest(String aDest) {
        log("DEPRECATED - The setDest(String) method has been deprecated."
            + " Use setDest(File) instead.");
        setDest(project.resolveFile(aDest));
    }

    public void setSrc(File src) {
        source = src;
    }

    public void setDest(File aDest) {
        this.dest = aDest;
    }

    private void validate() throws BuildException {
        if (source == null) {
            throw new BuildException("No Src specified", location);
        }

        if (!source.exists()) {
            throw new BuildException("Src doesn't exist", location);
        }

        if (source.isDirectory()) {
            throw new BuildException("Cannot expand a directory", location);
        }

        if (dest == null) {
            dest = new File(source.getParent());
        }

        if (dest.isDirectory()) {
            String defaultExtension = getDefaultExtension();
            createDestFile(defaultExtension);
        }
    }

    private void createDestFile(String defaultExtension) {
        String sourceName = source.getName();
        int len = sourceName.length();
        if (defaultExtension != null
            && len > defaultExtension.length()
            && defaultExtension.equalsIgnoreCase(sourceName.substring(len-defaultExtension.length()))) {
            dest = new File(dest, sourceName.substring(0,
                                                       len-defaultExtension.length()));
        } else {
            dest = new File(dest, sourceName);
        }
    }

    public void execute() throws BuildException {
        validate();
        extract();
    }

    protected abstract String getDefaultExtension();
    protected abstract void extract();
}

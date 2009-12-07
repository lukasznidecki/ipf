/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

/**
 * Exception thrown by processors to indicate that an authentication failure occurred
 * and has to be logged in ATNA.
 */
public class MllpAuthenticationFailure extends Exception {
    private static final long serialVersionUID = -7577712062697569080L;

    /**
     * Constructs the exception.
     */
    public MllpAuthenticationFailure() {
        super();
    }

    /**
     * Constructs the exception.
     * @param message
     *          see {@link Exception#Exception(String)}
     */
    public MllpAuthenticationFailure(String message) {
        super(message);
    }

    /**
     * Constructs the exception.
     * @param message
     *          see {@link Exception#Exception(String, Throwable)}
     * @param cause
     *          see {@link Exception#Exception(String, Throwable)}
     */
    public MllpAuthenticationFailure(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs the exception.
     * @param cause
     *          see {@link Exception#Exception(Throwable)}
     */
    public MllpAuthenticationFailure(Throwable cause) {
        super(cause);
    }
}
/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct;

/**
 * Strategy for handling injection. This is only used on annotated based component io.swagger.v3.oas.models such as CDI, Spring and
 * JSR330.
 *
 * @author Kevin Grüneberg
 */
public enum InjectionStrategy {

    /** Annotations are written on the field **/
    FIELD,

    /** Annotations are written on the constructor **/
    CONSTRUCTOR
}

/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct;

import java.lang.annotation.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Configures the mapping between two iterable like types, e.g. {@code List<String>} and {@code List<Date>}.
 *
 *
 * <p>Note: either  @IterableMapping#dateFormat, @IterableMapping#resultType or @IterableMapping#qualifiedBy
 * must be specified</p>
 *
 * Supported mappings are:
 * <ul>
 *     <li>{@code Iterable<A>} to/from {@code Iterable<B>}/{@code Iterable<A>}</li>
 *     <li>{@code Iterable<A>} to/from {@code B[]}/{@code A[]}</li>
 *     <li>{@code Iterable<A>} to/from {@code Stream<B>}/{@code Stream<A>}</li>
 *     <li>{@code A[]} to/from {@code Stream<B>}/{@code Stream<A>}</li>
 *     <li>{@code A[]} to/from {@code B[]}</li>
 *     <li>{@code Stream<A>} to/from {@code Stream<B>}</li>
 * </ul>
 *
 * @author Gunnar Morling
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface IterableMapping {

    /**
     * A format string as processable by {@link SimpleDateFormat} if the annotated method maps from an iterable of
     * {@code String} to an iterable {@link Date} or vice-versa. Will be ignored for all other element types.
     *
     * @return A date format string as processable by {@link SimpleDateFormat}.
     */
    String dateFormat() default "";

    /**
     * A format string as processable by {@link DecimalFormat} if the annotated method maps from a
     *  {@link Number} to a {@link String} or vice-versa. Will be ignored for all other element types.
     *
     * @return A decimal format string as processable by {@link DecimalFormat}.
     */
    String numberFormat() default "";

    /**
     * A qualifier can be specified to aid the selection process of a suitable mapper. This is useful in case multiple
     * mappers (hand written of internal) qualify and result in an 'Ambiguous mapping methods found' error.
     *
     * A qualifier is a custom annotation and can be placed on either a hand written mapper class or a method.
     *
     * @return the qualifiers
     */
    Class<? extends Annotation>[] qualifiedBy() default { };

    /**
     * String-based form of qualifiers; When looking for a suitable mapping method to map this iterable mapping method's
     * element type, MapStruct will only consider those methods carrying directly or indirectly (i.e. on the
     * class-level) a {@link Named} annotation for each of the specified qualifier names.
     * <p>
     * Note that annotation-based qualifiers are generally preferable as they allow more easily to find references and
     * are safe for refactorings, but name-based qualifiers can be a less verbose alternative when requiring a large
     * number of qualifiers as no custom annotation types are needed.
     *
     * @return One or more qualifier name(s)
     * @see #qualifiedBy()
     * @see Named
     */
    String[] qualifiedByName() default { };

    /**
     * Specifies the type of the element to be used in the result of the mapping method in case multiple mapping
     * methods qualify.
     *
     * @return the elementTargetType to select
     */
    Class<?> elementTargetType() default void.class;


    /**
     * The strategy to be applied when {@code null} is passed as source value to this iterable mapping. If no
     * strategy is configured, the strategy given via {@link MapperConfig#nullValueMappingStrategy()} or
     * {@link Mapper#nullValueMappingStrategy()} will be applied, using {@link NullValueMappingStrategy#RETURN_NULL}
     * by default.
     *
     * @return The strategy to be applied when {@code null} is passed as source value to the methods of this mapping.
     */
    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.RETURN_NULL;
}

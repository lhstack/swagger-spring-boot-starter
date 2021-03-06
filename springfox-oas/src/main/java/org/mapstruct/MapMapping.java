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
 * Configures the mapping between two map types, e.g. {@code Map<String, String>} and {@code Map<Long, Date>}.
 *
 * <p>Note: at least one element needs to be specified</p>
 *
 * @author Gunnar Morling
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MapMapping {

    /**
     * A format string as processable by {@link SimpleDateFormat} if the annotated method maps from a map with key type
     * {@code String} to an map with key type {@link Date} or vice-versa. Will be ignored for all other key types.
     *
     * @return A date format string as processable by {@link SimpleDateFormat}.
     */
    String keyDateFormat() default "";

    /**
     * A format string as processable by {@link SimpleDateFormat} if the annotated method maps from a map with value
     * type {@code String} to an map with value type {@link Date} or vice-versa. Will be ignored for all other value
     * types.
     *
     * @return A date format string as processable by {@link SimpleDateFormat}.
     */
    String valueDateFormat() default "";

    /**
     * A format string as processable by {@link DecimalFormat} if the annotated method maps from a
     *  {@link Number} to a {@link String} or vice-versa. Will be ignored for all other key types.
     *
     * @return A decimal format string as processable by {@link DecimalFormat}.
     */
    String keyNumberFormat() default "";

    /**
     * A format string as processable by {@link DecimalFormat} if the annotated method maps from a
     *  {@link Number} to a {@link String} or vice-versa. Will be ignored for all other value types.
     *
     * @return A decimal format string as processable by {@link DecimalFormat}.
     */
    String valueNumberFormat() default "";

    /**
     * A key value qualifier can be specified to aid the selection process of a suitable mapper. This is useful in
     * case multiple mappers (hand written of internal) qualify and result in an 'Ambiguous mapping methods found'
     * error.
     *
     * A qualifier is a custom annotation and can be placed on either a hand written mapper class or a method.
     *
     * @return the qualifiers
     */
    Class<? extends Annotation>[] keyQualifiedBy() default { };

    /**
     * String-based form of qualifiers; When looking for a suitable mapping method to map this map mapping method's key
     * type, MapStruct will only consider those methods carrying directly or indirectly (i.e. on the class-level) a
     * {@link Named} annotation for each of the specified qualifier names.
     * <p>
     * Note that annotation-based qualifiers are generally preferable as they allow more easily to find references and
     * are safe for refactorings, but name-based qualifiers can be a less verbose alternative when requiring a large
     * number of qualifiers as no custom annotation types are needed.
     *
     * @return One or more qualifier name(s)
     * @see #keyQualifiedBy()
     * @see Named
     */
    String[] keyQualifiedByName() default { };


    /**
     * A value qualifier can be specified to aid the selection process of a suitable mapper for the values in the map.
     * This is useful in case multiple mappers (hand written of internal) qualify and result in an 'Ambiguous mapping
     * methods found' error.
     * <p>
     * A qualifier is a custom annotation and can be placed on either a hand written mapper class or a method.
     *
     * @return the qualifiers
     */
    Class<? extends Annotation>[] valueQualifiedBy() default { };

    /**
     * String-based form of qualifiers; When looking for a suitable mapping method to map this map mapping method's
     * value type, MapStruct will only consider those methods carrying directly or indirectly (i.e. on the class-level)
     * a {@link Named} annotation for each of the specified qualifier names.
     * <p>
     * Note that annotation-based qualifiers are generally preferable as they allow more easily to find references and
     * are safe for refactorings, but name-based qualifiers can be a less verbose alternative when requiring a large
     * number of qualifiers as no custom annotation types are needed.
     *
     * @return One or more qualifier name(s)
     * @see #valueQualifiedBy()
     * @see Named
     */
    String[] valueQualifiedByName() default { };

    /**
     * Specifies the type of the key to be used in the result of the mapping method in case multiple mapping
     * methods qualify.
     *
     *
     * @return the resultType to select
     */
    Class<?> keyTargetType() default void.class;

    /**
     * Specifies the type of the value to be used in the result of the mapping method in case multiple mapping
     * methods qualify.
     *
     *
     * @return the resultType to select
     */
    Class<?> valueTargetType() default void.class;


    /**
     * The strategy to be applied when {@code null} is passed as source value to this map mapping. If no
     * strategy is configured, the strategy given via {@link MapperConfig#nullValueMappingStrategy()} or
     * {@link Mapper#nullValueMappingStrategy()} will be applied, using {@link NullValueMappingStrategy#RETURN_NULL}
     * by default.
     *
     * @return The strategy to be applied when {@code null} is passed as source value to the methods of this mapping.
     */
    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.RETURN_NULL;
}

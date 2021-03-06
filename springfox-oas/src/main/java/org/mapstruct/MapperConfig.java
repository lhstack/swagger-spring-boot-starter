/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct;

import org.mapstruct.factory.Mappers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.mapstruct.NullValueCheckStrategy.ON_IMPLICIT_CONVERSION;

/**
 * Marks a class or interface as configuration source for generated mappers. This allows to share common configurations
 * between several mapper classes.
 * <p>
 * Generally, any settings given via {@link Mapper} take precedence over the settings given via the referenced
 * {@code MapperConfig}. The lists of referenced mappers given via {@link Mapper#uses()} and
 * {@link MapperConfig#uses()} will be merged.
 * <p>
 * Additionally, mapper configuration classes may declare one more <em>prototype mapping methods</em>. These methods are
 * not meant to be invoked themselves (no implementation will generated for mapper config classes), but serve as
 * configuration template for mapping methods declared by actual mapper classes. Depending on the configured
 * {@link #mappingInheritanceStrategy()}, the configuration can be inherited either explicitly using
 * {@link InheritConfiguration} or {@link InheritInverseConfiguration}, or automatically in case all source and target
 * types are assignable.
 * </p>
 *
 * @author Sjaak Derksen
 * @see Mapper#config()
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MapperConfig {

    /**
     * The mapper types used by this mapper.
     *
     * @return The mapper types used by this mapper.
     */
    Class<?>[] uses() default { };

    /**
     * How unmapped properties of the source type of a mapping should be
     * reported.
     *
     * @return The reporting policy for unmapped source properties.
     *
     * @since 1.3
     */
    ReportingPolicy unmappedSourcePolicy() default ReportingPolicy.IGNORE;

    /**
     * How unmapped properties of the target type of a mapping should be
     * reported.
     *
     * @return The reporting policy for unmapped target properties.
     */
    ReportingPolicy unmappedTargetPolicy() default ReportingPolicy.WARN;

    /**
     * How lossy (narrowing) conversion, for instance: long to integer should be
     * reported.
     *
     * @since 1.3
     *
     * @return The reporting policy for type conversion.
     */
    ReportingPolicy typeConversionPolicy() default ReportingPolicy.IGNORE;

    /**
     * Specifies the component model to which the generated mapper should
     * adhere. Supported values are
     * <ul>
     * <li> {@code default}: the mapper uses no component model, instances are
     * typically retrieved via {@link Mappers#getMapper(Class)}</li>
     * <li>
     * {@code cdi}: the generated mapper is an application-scoped CDI bean and
     * can be retrieved via {@code @Inject}</li>
     * <li>
     * {@code spring}: the generated mapper is a Spring bean and
     * can be retrieved via {@code @Autowired}</li>
     * <li>
     * {@code jsr330}: the generated mapper is annotated with {@code @javax.inject.Named} and
     * {@code @Singleton}, and can be retrieved via {@code @Inject}</li>
     * </ul>
     *
     * @return The component model for the generated mapper.
     */
    String componentModel() default "default";

    /**
     * Specifies the name of the implementation class. The {@code <CLASS_NAME>} will be replaced by the
     * interface/abstract class name.
     * <p>
     * Defaults to postfixing the name with {@code Impl}: {@code <CLASS_NAME>Impl}
     *
     * @return The implementation name.
     * @see #implementationPackage()
     */
    String implementationName() default "<CLASS_NAME>Impl";

    /**
     * Specifies the target package for the generated implementation. The {@code <PACKAGE_NAME>} will be replaced by the
     * interface's or abstract class' package.
     * <p>
     * Defaults to using the same package as the mapper interface/abstract class
     *
     * @return the implementation package.
     * @see #implementationName()
     */
    String implementationPackage() default "<PACKAGE_NAME>";

    /**
     * The strategy to be applied when propagating the value of collection-typed properties. By default, only JavaBeans
     * accessor methods (setters or getters) will be used, but it is also possible to invoke a corresponding adder
     * method for each element of the source collection (e.g. {@code orderDto.addOrderLine()}).
     *
     * @return The strategy applied when propagating the value of collection-typed properties.
     */
    CollectionMappingStrategy collectionMappingStrategy() default CollectionMappingStrategy.ACCESSOR_ONLY;

    /**
     * The strategy to be applied when {@code null} is passed as source argument value to mapping methods. If no
     * strategy is configured, {@link NullValueMappingStrategy#RETURN_NULL} will be used by default.
     *
     * @return The strategy to be applied when {@code null} is passed as source value to mapping methods.
     */
    NullValueMappingStrategy nullValueMappingStrategy() default NullValueMappingStrategy.RETURN_NULL;

    /**
     * The strategy to be applied when a source bean property is {@code null} or not present. If no strategy is
     * configured, {@link NullValuePropertyMappingStrategy#SET_TO_NULL} will be used by default.
     *
     * @since 1.3
     *
     * @return The strategy to be applied when {@code null} is passed as source property value or the source property
     * is not present.
     */
    NullValuePropertyMappingStrategy nullValuePropertyMappingStrategy() default
        NullValuePropertyMappingStrategy.SET_TO_NULL;

    /**
     * The strategy to use for applying method-level configuration annotations of prototype methods in the interface
     * annotated with this annotation. Annotations that can be inherited are for example {@link Mapping},
     * {@link IterableMapping}, {@link MapMapping}, or {@link BeanMapping}.
     * <p>
     * If no strategy is configured, {@link MappingInheritanceStrategy#EXPLICIT} will be used as default.
     *
     * @return The strategy to use for applying {@code @Mapping} configurations of prototype methods in the interface
     * annotated with this annotation.
     */
    MappingInheritanceStrategy mappingInheritanceStrategy()
        default MappingInheritanceStrategy.EXPLICIT;

    /**
     * Determines when to include a null check on the source property value of a bean mapping.
     *
     * Can be overridden by the one on {@link Mapper}, {@link BeanMapping} or {@link Mapping}.
     *
     * @return strategy how to do null checking
     */
    NullValueCheckStrategy nullValueCheckStrategy() default ON_IMPLICIT_CONVERSION;

    /**
     * Determines whether to use field or constructor injection. This is only used on annotated based component io.swagger.v3.oas.models
     * such as CDI, Spring and JSR 330.
     *
     * Can be overridden by the one on {@link Mapper}.
     *
     * If no strategy is configured, {@link InjectionStrategy#FIELD} will be used as default.
     *
     * @return strategy how to inject
     */
    InjectionStrategy injectionStrategy() default InjectionStrategy.FIELD;

    /**
     * If MapStruct could not find another mapping method or apply an automatic conversion it will try to generate a
     * sub-mapping method between the two beans. If this property is set to {@code true} MapStruct will not try to
     * automatically generate sub-mapping methods.
     * <p>
     * Can be overridden by {@link Mapper#disableSubMappingMethodsGeneration()}
     * <p>
     * Note: If you need to use {@code disableSubMappingMethodsGeneration} please contact the MapStruct team at
     * <a href="http://mapstruct.org">mapstruct.org</a> or
     * <a href="https://github.com/mapstruct/mapstruct">github.com/mapstruct/mapstruct</a> to share what problem you
     * are facing with the automatic sub-mapping generation.
     *
     * @return whether the automatic generation of sub-mapping methods is disabled
     *
     * @since 1.2
     */
    boolean disableSubMappingMethodsGeneration() default false;

    /**
     * The information that should be used for the builder mappings. This can be used to define custom build methods
     * for the builder strategy that one uses.
     *
     * <p>
     * Can be overridden by {@link MapperConfig#builder()}.
     *
     * <p>
     * NOTE: In case no builder is defined here, in {@link BeanMapping} or {@link Mapper} and there is a single
     * build method, then that method would be used.
     * <p>
     * If the builder is defined and there is a single method that does not match the name of the build method then
     * a compile error will occur
     *
     * @return the builder information
     *
     * @since 1.3
     */
    Builder builder() default @Builder;
}

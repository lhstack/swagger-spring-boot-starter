package springfox.documentation.builders;

import springfox.documentation.service.ParameterSpecification;

/**
 * @author lhstack
 */
@FunctionalInterface
public interface ParameterSpecificationProvider {
  ParameterSpecification create(ParameterSpecificationContext context);
}

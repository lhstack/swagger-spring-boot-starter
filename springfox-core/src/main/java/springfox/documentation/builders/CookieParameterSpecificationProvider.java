package springfox.documentation.builders;

import springfox.documentation.service.*;

public class CookieParameterSpecificationProvider implements ParameterSpecificationProvider {
  @Override
  public ParameterSpecification create(ParameterSpecificationContext context) {
    SimpleParameterSpecification simpleParameter = context.getSimpleParameter();
    SimpleParameterSpecification validSimpleParameter = null;
    ContentSpecification validContentEncoding = null;
    if (simpleParameter.getModel().getScalar().isPresent()) {
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(simpleParameter.getExplode())
          .style(ParameterStyle.FORM)
          .build();
    } else if (simpleParameter.getModel().getCollection().isPresent()) {
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(simpleParameter.getExplode())
          .style(ParameterStyle.FORM)
          .collectionFormat(simpleParameter.nullSafeIsExplode()
              ? CollectionFormat.MULTI
              : CollectionFormat.CSV)
          .build();
    }  else {
      validSimpleParameter = context.getSimpleParameterSpecificationBuilder()
          .copyOf(simpleParameter)
          .explode(simpleParameter.getExplode())
          .style(ParameterStyle.FORM)
          .collectionFormat(null)
          .build();
    }
    if (context.getContentParameter() != null) {
      validContentEncoding = context.getContentSpecificationBuilder()
          .copyOf(context.getContentParameter())
          .build();
    }
    return new ParameterSpecification(
        validSimpleParameter,
        validContentEncoding);
  }
}

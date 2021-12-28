//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package springfox.documentation.oas.mappers;

import io.swagger.v3.oas.models.examples.Example;
import org.springframework.stereotype.Component;

@Component
public class ExamplesMapperImpl implements ExamplesMapper {
    public ExamplesMapperImpl() {
    }

    @Override
    public Example toOasExample(springfox.documentation.schema.Example from) {
        if (from == null) {
            return null;
        } else {
            Example example = new Example();
            example.summary(from.getSummary());
            example.description(from.getDescription());
            example.value(from.getValue());
            example.externalValue(from.getExternalValue());
            this.afterMappingParameter(from, example);
            return example;
        }
    }
}

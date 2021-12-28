//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package springfox.documentation.oas.mappers;

import io.swagger.v3.oas.models.headers.Header.StyleEnum;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ParameterStyle;

@Component
public class StyleEnumMapperImpl extends StyleEnumMapper {
    public StyleEnumMapperImpl() {
    }

    @Override
    public StyleEnum headerStyle(ParameterStyle from) {
        if (from == null) {
            return null;
        } else {
            StyleEnum styleEnum;
            switch(from) {
                case DEFAULT:
                    styleEnum = null;
                    break;
                case SIMPLE:
                    styleEnum = StyleEnum.SIMPLE;
                    break;
                case MATRIX:
                    styleEnum = null;
                    break;
                case LABEL:
                    styleEnum = null;
                    break;
                case FORM:
                    styleEnum = null;
                    break;
                case SPACEDELIMITED:
                    styleEnum = null;
                    break;
                case PIPEDELIMITED:
                    styleEnum = null;
                    break;
                case DEEPOBJECT:
                    styleEnum = null;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected enum constant: " + from);
            }

            return styleEnum;
        }
    }

    @Override
    public io.swagger.v3.oas.models.media.Encoding.StyleEnum encodingStyle(ParameterStyle from) {
        if (from == null) {
            return null;
        } else {
            io.swagger.v3.oas.models.media.Encoding.StyleEnum styleEnum;
            switch(from) {
                case DEFAULT:
                    styleEnum = null;
                    break;
                case SIMPLE:
                    styleEnum = null;
                    break;
                case MATRIX:
                    styleEnum = null;
                    break;
                case LABEL:
                    styleEnum = null;
                    break;
                case FORM:
                    styleEnum = null;
                    break;
                case SPACEDELIMITED:
                    styleEnum = io.swagger.v3.oas.models.media.Encoding.StyleEnum.SPACE_DELIMITED;
                    break;
                case PIPEDELIMITED:
                    styleEnum = io.swagger.v3.oas.models.media.Encoding.StyleEnum.PIPE_DELIMITED;
                    break;
                case DEEPOBJECT:
                    styleEnum = io.swagger.v3.oas.models.media.Encoding.StyleEnum.DEEP_OBJECT;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected enum constant: " + from);
            }

            return styleEnum;
        }
    }
}

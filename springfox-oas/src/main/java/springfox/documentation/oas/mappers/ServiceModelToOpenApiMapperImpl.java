//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package springfox.documentation.oas.mappers;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.Parameter.StyleEnum;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.service.*;

import java.util.*;

@Component
public class ServiceModelToOpenApiMapperImpl extends ServiceModelToOpenApiMapper {
    @Autowired
    private VendorExtensionsMapper vendorExtensionsMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private ExamplesMapper examplesMapper;
    @Autowired
    private SecurityMapper securityMapper;
    @Autowired
    private SchemaMapper schemaMapper;
    @Autowired
    private StyleEnumMapper styleEnumMapper;
    @Autowired
    private SecuritySchemeMapper securitySchemeMapper;

    public ServiceModelToOpenApiMapperImpl() {
    }

    @Override
    public OpenAPI mapDocumentation(Documentation from) {
        if (from == null) {
            return null;
        } else {
            OpenAPI openAPI = new OpenAPI();
            if (from.getResourceListing() != null) {
                if (openAPI.getComponents() == null) {
                    openAPI.components(new Components());
                }

                this.resourceListingToComponents(from.getResourceListing(), openAPI.getComponents());
            }

            if (openAPI.getComponents() == null) {
                openAPI.components(new Components());
            }

            this.documentationToComponents(from, openAPI.getComponents());
            openAPI.extensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            openAPI.paths(this.mapPaths(from.getApiListings()));
            openAPI.externalDocs(this.mapExternalDocs(from.getExternalDocumentation()));
            openAPI.info(this.mapApiInfo(this.fromResourceListingInfo(from)));
            openAPI.servers(this.serverListToServerList(from.getServers()));
            openAPI.tags(this.tagSetToTagList(from.getTags()));
            openAPI.openapi("3.0.3");
            return openAPI;
        }
    }

    @Override
    Operation mapOperation(springfox.documentation.service.Operation from, ModelNamesRegistry modelNamesRegistry) {
        if (from == null) {
            return null;
        } else {
            Operation operation = new Operation();
            operation.security(this.securityMapper.mapFrom(from.getSecurityReferences()));
            operation.extensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            operation.requestBody(this.map(from.getBody(), modelNamesRegistry));
            operation.operationId(from.getUniqueId());
            operation.description(from.getNotes());
            operation.parameters(this.requestParameterSortedSetToParameterList(from.getQueryParameters(), modelNamesRegistry));
            Set<String> set = from.getTags();
            if (set != null) {
                operation.tags(new ArrayList(set));
            }

            operation.summary(from.getSummary());
            operation.responses(this.map(from.getResponses(), modelNamesRegistry));
            if (from.getDeprecated() != null) {
                operation.deprecated(Boolean.parseBoolean(from.getDeprecated()));
            }

            return operation;
        }
    }

    @Override
    Parameter mapParameter(RequestParameter from, ModelNamesRegistry modelNamesRegistry) {
        if (from == null) {
            return null;
        } else {
            Parameter parameter = new Parameter();
            parameter.schema(this.fromSimpleParameter(this.fromParameterSpecificationQuery(from), modelNamesRegistry));
            parameter.in(this.fromInIn(from));
            parameter.name(from.getName());
            parameter.description(from.getDescription());
            parameter.required(from.getRequired());
            parameter.deprecated(from.getDeprecated());
            from.getParameterSpecification().getQuery()
                    .ifPresent(simpleParameterSpecification -> {
                        parameter.setDefaultValue(simpleParameterSpecification.getDefaultValue());
                    });
            parameter.examples(this.examplesMapper.mapExamples(from.getExamples()));
            parameter.extensions(this.vendorExtensionsMapper.mapExtensions(from.getExtensions()));
            parameter.allowReserved((Boolean) from.getParameterSpecification().getQuery().map((q) -> {
                return q.getAllowReserved();
            }).orElse((Boolean) null));
            parameter.explode((Boolean) from.getParameterSpecification().getQuery().map((q) -> {
                return q.getExplode();
            }).orElse((Boolean) null));
            parameter.allowEmptyValue((Boolean) from.getParameterSpecification().getQuery().map((q) -> {
                return q.getAllowEmptyValue();
            }).orElse((Boolean) null));
            parameter.style((StyleEnum) from.getParameterSpecification().getQuery().map((q) -> {
                return this.parameterStyle(q.getStyle());
            }).orElse((StyleEnum) null));
            this.afterMappingParameter(from, parameter);
            return parameter;
        }
    }

    @Override
    HttpMethod mapHttpMethod(org.springframework.http.HttpMethod method) {
        if (method == null) {
            return null;
        } else {
            HttpMethod httpMethod;
            switch (method) {
                case GET:
                    httpMethod = HttpMethod.GET;
                    break;
                case HEAD:
                    httpMethod = HttpMethod.HEAD;
                    break;
                case POST:
                    httpMethod = HttpMethod.POST;
                    break;
                case PUT:
                    httpMethod = HttpMethod.PUT;
                    break;
                case PATCH:
                    httpMethod = HttpMethod.PATCH;
                    break;
                case DELETE:
                    httpMethod = HttpMethod.DELETE;
                    break;
                case OPTIONS:
                    httpMethod = HttpMethod.OPTIONS;
                    break;
                case TRACE:
                    httpMethod = HttpMethod.TRACE;
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected enum constant: " + method);
            }

            return httpMethod;
        }
    }

    @Override
    protected MediaType fromRepresentation(Representation each, ModelNamesRegistry modelNamesRegistry) {
        if (each == null) {
            return null;
        } else {
            MediaType mediaType = new MediaType();
            mediaType.schema(this.schemaMapper.mapModel(each.getModel(), modelNamesRegistry));
            List<VendorExtension> facetExtensions = this.eachModelFacetExtensions(each);
            mediaType.extensions(this.vendorExtensionsMapper.mapExtensions(facetExtensions));
            mediaType.encoding(this.fromEncodings(each.getEncodings(), modelNamesRegistry));
            return mediaType;
        }
    }

    @Override
    protected Encoding mapEncoding(springfox.documentation.service.Encoding from, ModelNamesRegistry modelNamesRegistry) {
        if (from == null) {
            return null;
        } else {
            Encoding encoding = new Encoding();
            encoding.setStyle(this.styleEnumMapper.encodingStyle(from.getStyle()));
            encoding.setContentType(from.getContentType());
            encoding.setHeaders(this.fromHeaders(from.getHeaders(), modelNamesRegistry));
            encoding.setExplode(from.getExplode());
            encoding.setAllowReserved(from.getAllowReserved());
            encoding.extensions(this.vendorExtensionsMapper.mapExtensions(from.getExtensions()));
            return encoding;
        }
    }

    @Override
    protected Header mapHeader(springfox.documentation.service.Header from, ModelNamesRegistry modelNamesRegistry) {
        if (from == null) {
            return null;
        } else {
            Header header = new Header();
            header.schema(this.schemaMapper.mapFrom(from.getModelSpecification(), modelNamesRegistry));
            header.required(from.getRequired());
            header.description(from.getDescription());
            return header;
        }
    }

    @Override
    protected Info mapApiInfo(ApiInfo from) {
        if (from == null) {
            return null;
        } else {
            Info info = new Info();
            info.license(this.licenseMapper.apiInfoToLicense(from));
            info.extensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            info.termsOfService(from.getTermsOfServiceUrl());
            info.contact(this.map(from.getContact()));
            info.title(from.getTitle());
            info.description(from.getDescription());
            info.version(from.getVersion());
            return info;
        }
    }

    @Override
    protected Contact map(springfox.documentation.service.Contact from) {
        if (from == null) {
            return null;
        } else {
            Contact contact = new Contact();
            contact.name(from.getName());
            contact.url(from.getUrl());
            contact.email(from.getEmail());
            return contact;
        }
    }

    @Override
    protected Tag mapTag(springfox.documentation.service.Tag from) {
        if (from == null) {
            return null;
        } else {
            Tag tag = new Tag();
            tag.extensions(this.vendorExtensionsMapper.mapExtensions(from.getVendorExtensions()));
            tag.name(from.getName());
            tag.description(from.getDescription());
            return tag;
        }
    }

    @Override
    protected Server mapServer(springfox.documentation.service.Server from) {
        if (from == null) {
            return null;
        } else {
            Server server = new Server();
            server.extensions(this.vendorExtensionsMapper.mapExtensions(from.getExtensions()));
            server.url(from.getUrl());
            server.description(from.getDescription());
            server.variables(this.serverVariableMap(from.getVariables()));
            return server;
        }
    }

    @Override
    protected ServerVariable mapServerVariable(springfox.documentation.service.ServerVariable from) {
        if (from == null) {
            return null;
        } else {
            ServerVariable serverVariable = new ServerVariable();
            serverVariable.setDefault(from.getDefaultValue());
            List<String> list = from.getAllowedValues();
            if (list != null) {
                serverVariable.setEnum(new ArrayList(list));
            }

            serverVariable.description(from.getDescription());
            serverVariable.extensions(this.vendorExtensionsMapper.mapExtensions(from.getExtensions()));
            return serverVariable;
        }
    }

    @Override
    protected ExternalDocumentation mapExternalDocs(springfox.documentation.common.ExternalDocumentation from) {
        if (from == null) {
            return null;
        } else {
            ExternalDocumentation externalDocumentation = new ExternalDocumentation();
            externalDocumentation.description(from.getDescription());
            externalDocumentation.url(from.getUrl());
            externalDocumentation.extensions(this.vendorExtensionsMapper.mapExtensions(from.getExtensions()));
            return externalDocumentation;
        }
    }

    protected void resourceListingToComponents(ResourceListing resourceListing, Components mappingTarget) {
        if (resourceListing != null) {
            Map map;
            if (mappingTarget.getSecuritySchemes() != null) {
                map = this.securitySchemeMapper.map(resourceListing.getSecuritySchemes());
                if (map != null) {
                    mappingTarget.getSecuritySchemes().clear();
                    mappingTarget.getSecuritySchemes().putAll(map);
                } else {
                    mappingTarget.securitySchemes((Map) null);
                }
            } else {
                map = this.securitySchemeMapper.map(resourceListing.getSecuritySchemes());
                if (map != null) {
                    mappingTarget.securitySchemes(map);
                }
            }

        }
    }

    protected void documentationToComponents(Documentation documentation, Components mappingTarget) {
        if (documentation != null) {
            Map map;
            if (mappingTarget.getSchemas() != null) {
                map = this.schemaMapper.modelsFromApiListings(documentation.getApiListings());
                if (map != null) {
                    mappingTarget.getSchemas().clear();
                    mappingTarget.getSchemas().putAll(map);
                } else {
                    mappingTarget.schemas((Map) null);
                }
            } else {
                map = this.schemaMapper.modelsFromApiListings(documentation.getApiListings());
                if (map != null) {
                    mappingTarget.schemas(map);
                }
            }

        }
    }

    private ApiInfo fromResourceListingInfo(Documentation documentation) {
        if (documentation == null) {
            return null;
        } else {
            ResourceListing resourceListing = documentation.getResourceListing();
            if (resourceListing == null) {
                return null;
            } else {
                ApiInfo info = resourceListing.getInfo();
                return info == null ? null : info;
            }
        }
    }

    protected List<Server> serverListToServerList(List<springfox.documentation.service.Server> list) {
        if (list == null) {
            return null;
        } else {
            List<Server> list1 = new ArrayList(list.size());
            Iterator var3 = list.iterator();

            while (var3.hasNext()) {
                springfox.documentation.service.Server server = (springfox.documentation.service.Server) var3.next();
                list1.add(this.mapServer(server));
            }

            return list1;
        }
    }

    protected List<Tag> tagSetToTagList(Set<springfox.documentation.service.Tag> set) {
        if (set == null) {
            return null;
        } else {
            List<Tag> list = new ArrayList(set.size());
            Iterator var3 = set.iterator();

            while (var3.hasNext()) {
                springfox.documentation.service.Tag tag = (springfox.documentation.service.Tag) var3.next();
                list.add(this.mapTag(tag));
            }

            return list;
        }
    }

    protected List<Parameter> requestParameterSortedSetToParameterList(SortedSet<RequestParameter> sortedSet, ModelNamesRegistry modelNamesRegistry) {
        if (sortedSet == null) {
            return null;
        } else {
            List<Parameter> list = new ArrayList(sortedSet.size());
            Iterator var4 = sortedSet.iterator();

            while (var4.hasNext()) {
                RequestParameter requestParameter = (RequestParameter) var4.next();
                list.add(this.mapParameter(requestParameter, modelNamesRegistry));
            }

            return list;
        }
    }

    private Optional<SimpleParameterSpecification> fromParameterSpecificationQuery(RequestParameter requestParameter) {
        if (requestParameter == null) {
            return null;
        } else {
            ParameterSpecification parameterSpecification = requestParameter.getParameterSpecification();
            if (parameterSpecification == null) {
                return null;
            } else {
                Optional<SimpleParameterSpecification> query = parameterSpecification.getQuery();
                return query == null ? null : query;
            }
        }
    }

    private String fromInIn(RequestParameter requestParameter) {
        if (requestParameter == null) {
            return null;
        } else {
            ParameterType in = requestParameter.getIn();
            if (in == null) {
                return null;
            } else {
                String in1 = in.getIn();
                return in1 == null ? null : in1;
            }
        }
    }

    private List<VendorExtension> eachModelFacetExtensions(Representation representation) {
        if (representation == null) {
            return null;
        } else {
            ModelSpecification model = representation.getModel();
            if (model == null) {
                return null;
            } else {
                List<VendorExtension> facetExtensions = model.getFacetExtensions();
                return facetExtensions == null ? null : facetExtensions;
            }
        }
    }
}

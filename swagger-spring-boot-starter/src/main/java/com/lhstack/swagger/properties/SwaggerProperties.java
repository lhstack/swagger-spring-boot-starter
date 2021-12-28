package com.lhstack.swagger.properties;

import io.swagger.models.Tag;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;

import java.util.*;

/**
 * @author lhstack
 * @date 2021/9/28
 * @class SwaggerProperties
 * @since 1.8
 */
@ConfigurationProperties(prefix = "knife4j.swagger")
public class SwaggerProperties {

    /**
     * 默认配置
     */
    private Properties defaultProperties;

    /**
     * 自定义类似带分组的
     */
    @NestedConfigurationProperty
    private Set<Properties> properties;

    /**
     * 全局参数配置
     */
    @NestedConfigurationProperty
    private List<Parameter> parameters = Collections.emptyList();


    public Properties getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Properties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public Set<Properties> getProperties() {
        return properties;
    }

    public void setProperties(Set<Properties> properties) {
        this.properties = properties;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public static enum DocumentType {
        /**
         *
         */
        OAS_30(DocumentationType.OAS_30),
        SWAGGER_2(DocumentationType.SWAGGER_2),
        SWAGGER_12(DocumentationType.SWAGGER_12);
        private final DocumentationType documentationType;

        DocumentType(DocumentationType documentationType) {
            this.documentationType = documentationType;
        }

        public DocumentationType getDocumentationType() {
            return documentationType;
        }
    }

    public static enum ScalarType {
        /**
         *
         */
        INTEGER(springfox.documentation.schema.ScalarType.INTEGER),
        LONG(springfox.documentation.schema.ScalarType.LONG),
        DATE(springfox.documentation.schema.ScalarType.DATE),
        DATE_TIME(springfox.documentation.schema.ScalarType.DATE_TIME),
        STRING(springfox.documentation.schema.ScalarType.STRING),
        BYTE(springfox.documentation.schema.ScalarType.BYTE),
        BINARY(springfox.documentation.schema.ScalarType.BINARY),
        PASSWORD(springfox.documentation.schema.ScalarType.PASSWORD),
        BOOLEAN(springfox.documentation.schema.ScalarType.BOOLEAN),
        DOUBLE(springfox.documentation.schema.ScalarType.DOUBLE),
        FLOAT(springfox.documentation.schema.ScalarType.FLOAT),
        BIGINTEGER(springfox.documentation.schema.ScalarType.BIGINTEGER),
        BIGDECIMAL(springfox.documentation.schema.ScalarType.BIGDECIMAL),
        UUID(springfox.documentation.schema.ScalarType.UUID),
        EMAIL(springfox.documentation.schema.ScalarType.EMAIL),
        CURRENCY(springfox.documentation.schema.ScalarType.CURRENCY),
        URI(springfox.documentation.schema.ScalarType.URI),
        URL(springfox.documentation.schema.ScalarType.URL),
        OBJECT(springfox.documentation.schema.ScalarType.OBJECT);

        private final springfox.documentation.schema.ScalarType scalarType;

        ScalarType(springfox.documentation.schema.ScalarType scalarType) {
            this.scalarType = scalarType;
        }

        public springfox.documentation.schema.ScalarType getScalarType() {
            return scalarType;
        }
    }

    public static class Parameter {
        /**
         * 参数名称
         */
        private String name;

        /**
         * 参数类型
         */
        private ParameterType parameterType;

        /**
         * 是否必须
         */
        private Boolean required = true;

        /**
         * 是否隐藏
         */
        private Boolean hidden = false;

        /**
         * 是否删除
         */
        private Boolean deprecated = false;

        /**
         * 说明
         */
        private String description;

        /**
         * 默认值
         */
        private String defaultValue;

        private Boolean allowEmptyValue = true;

        private boolean allowMultiple = true;

        private Boolean allowReserved = false;

        private ScalarType scalarType = ScalarType.STRING;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ParameterType getParameterType() {
            return parameterType;
        }

        public void setParameterType(ParameterType parameterType) {
            this.parameterType = parameterType;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        public Boolean getHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        public Boolean getDeprecated() {
            return deprecated;
        }

        public void setDeprecated(Boolean deprecated) {
            this.deprecated = deprecated;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Boolean getAllowEmptyValue() {
            return allowEmptyValue;
        }

        public void setAllowEmptyValue(Boolean allowEmptyValue) {
            this.allowEmptyValue = allowEmptyValue;
        }

        public boolean isAllowMultiple() {
            return allowMultiple;
        }

        public void setAllowMultiple(boolean allowMultiple) {
            this.allowMultiple = allowMultiple;
        }

        public Boolean getAllowReserved() {
            return allowReserved;
        }

        public void setAllowReserved(Boolean allowReserved) {
            this.allowReserved = allowReserved;
        }

        public void setScalarType(ScalarType scalarType) {
            this.scalarType = scalarType;
        }

        public ScalarType getScalarType() {
            return scalarType;
        }

        public RequestParameter toRequestParameter() {
            return new RequestParameterBuilder()
                    .description(this.description)
                    .hidden(this.hidden)
                    .in(this.parameterType)
                    .query(builder -> {
                        builder.allowEmptyValue(this.allowEmptyValue)
                                .allowReserved(this.allowReserved)
                                .defaultValue(this.defaultValue)
                                .model(modelSpecificationBuilder -> {
                                    modelSpecificationBuilder.scalarModel(this.scalarType.scalarType)
                                            .build();
                                });
                    })
                    .name(this.name)
                    .required(this.required)
                    .build();
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Parameter parameter = (Parameter) o;
            return allowMultiple == parameter.allowMultiple && Objects.equals(name, parameter.name) && parameterType == parameter.parameterType && Objects.equals(required, parameter.required) && Objects.equals(hidden, parameter.hidden) && Objects.equals(deprecated, parameter.deprecated) && Objects.equals(description, parameter.description) && Objects.equals(defaultValue, parameter.defaultValue) && Objects.equals(allowEmptyValue, parameter.allowEmptyValue) && Objects.equals(allowReserved, parameter.allowReserved) && Objects.equals(scalarType, parameter.scalarType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, parameterType, required, hidden, deprecated, description, defaultValue, allowEmptyValue, allowMultiple, allowReserved, scalarType);
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "name='" + name + '\'' +
                    ", parameterType=" + parameterType +
                    ", required=" + required +
                    ", hidden=" + hidden +
                    ", deprecated=" + deprecated +
                    ", description='" + description + '\'' +
                    ", defaultValue='" + defaultValue + '\'' +
                    ", allowEmptyValue=" + allowEmptyValue +
                    ", allowMultiple=" + allowMultiple +
                    ", allowReserved=" + allowReserved +
                    ", scalarType=" + scalarType +
                    '}';
        }
    }

    public static class Properties {

        /**
         * 文档版本
         */
        private DocumentType documentType = DocumentType.OAS_30;

        /**
         * 忽略的class类型
         */
        private Set<Class<?>> ignoredParameterTypes = new HashSet<>();

        /**
         * 标题
         */
        private String title;

        /**
         * 项目版本
         */
        private String version;

        /**
         * 联系人名称
         */
        private String contactName;

        /**
         * 联系人网站
         */
        private String contactUrl;

        /**
         * 许可证
         */
        private String license;

        /**
         * 许可证地址
         */
        private String licenseUrl;

        private String termsOfServiceUrl;

        /**
         * 说明
         */
        private String description;

        /**
         * 联系人邮箱
         */
        private String contactEmail;

        /**
         * 文档分组
         */
        private String group = "default";

        /**
         * 标签
         */
        private List<Tag> tags;

        /**
         * 是否开启
         */
        private Boolean enable = true;

        /**
         * api接口包地址
         */
        private String basePackage;

        /**
         * 全局参数配置
         */
        private List<Parameter> parameters = Collections.emptyList();

        public DocumentType getDocumentType() {
            return documentType;
        }

        public void setDocumentType(DocumentType documentType) {
            this.documentType = documentType;
        }

        public Set<Class<?>> getIgnoredParameterTypes() {
            return ignoredParameterTypes;
        }

        public void setIgnoredParameterTypes(Set<Class<?>> ignoredParameterTypes) {
            this.ignoredParameterTypes = ignoredParameterTypes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Properties that = (Properties) o;
            return documentType == that.documentType && Objects.equals(ignoredParameterTypes, that.ignoredParameterTypes) && Objects.equals(title, that.title) && Objects.equals(version, that.version) && Objects.equals(contactName, that.contactName) && Objects.equals(contactUrl, that.contactUrl) && Objects.equals(license, that.license) && Objects.equals(licenseUrl, that.licenseUrl) && Objects.equals(termsOfServiceUrl, that.termsOfServiceUrl) && Objects.equals(description, that.description) && Objects.equals(contactEmail, that.contactEmail) && Objects.equals(group, that.group) && Objects.equals(tags, that.tags) && Objects.equals(enable, that.enable) && Objects.equals(basePackage, that.basePackage) && Objects.equals(parameters, that.parameters);
        }

        @Override
        public int hashCode() {
            return Objects.hash(documentType, ignoredParameterTypes, title, version, contactName, contactUrl, license, licenseUrl, termsOfServiceUrl, description, contactEmail, group, tags, enable, basePackage, parameters);
        }

        @Override
        public String toString() {
            return "Properties{" +
                    "documentType=" + documentType +
                    ", ignoredParameterTypes=" + ignoredParameterTypes +
                    ", title='" + title + '\'' +
                    ", version='" + version + '\'' +
                    ", contactName='" + contactName + '\'' +
                    ", contactUrl='" + contactUrl + '\'' +
                    ", license='" + license + '\'' +
                    ", licenseUrl='" + licenseUrl + '\'' +
                    ", termsOfServiceUrl='" + termsOfServiceUrl + '\'' +
                    ", description='" + description + '\'' +
                    ", contactEmail='" + contactEmail + '\'' +
                    ", group='" + group + '\'' +
                    ", tags=" + tags +
                    ", enable=" + enable +
                    ", basePackage='" + basePackage + '\'' +
                    ", parameters=" + parameters +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SwaggerProperties{" +
                "defaultProperties=" + defaultProperties +
                ", properties=" + properties +
                ", parameters=" + parameters +
                '}';
    }
}

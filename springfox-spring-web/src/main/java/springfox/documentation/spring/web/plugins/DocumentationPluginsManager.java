/*
 *
 *  Copyright 2015-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package springfox.documentation.spring.web.plugins;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import springfox.documentation.common.Compatibility;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.*;
import springfox.documentation.spi.service.contexts.*;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.DefaultModelNamesRegistryFactory;

import java.util.*;
import java.util.function.Function;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static springfox.documentation.spring.web.plugins.DuplicateGroupsDetector.ensureNoDuplicateGroups;

@Component
public class DocumentationPluginsManager {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Qualifier("apiListingBuilderPluginRegistry")
    private PluginRegistry<ApiListingBuilderPlugin, DocumentationType> apiListingPlugins;
    @Autowired
    @Qualifier("parameterBuilderPluginRegistry")
    private PluginRegistry<ParameterBuilderPlugin, DocumentationType> parameterPlugins;
    @Autowired
    @Qualifier("expandedParameterBuilderPluginRegistry")
    private PluginRegistry<ExpandedParameterBuilderPlugin, DocumentationType> parameterExpanderPlugins;
    @Autowired
    @Qualifier("operationBuilderPluginRegistry")
    private PluginRegistry<OperationBuilderPlugin, DocumentationType> operationBuilderPlugins;
    @Autowired
    @Qualifier("operationModelsProviderPluginRegistry")
    private PluginRegistry<OperationModelsProviderPlugin, DocumentationType> operationModelsProviders;
    @Autowired
    @Qualifier("defaultsProviderPluginRegistry")
    private PluginRegistry<DefaultsProviderPlugin, DocumentationType> defaultsProviders;
    @Autowired
    @Qualifier("pathDecoratorRegistry")
    private PluginRegistry<PathDecorator, DocumentationContext> pathDecorators;
    @Autowired
    @Qualifier("apiListingScannerPluginRegistry")
    private PluginRegistry<ApiListingScannerPlugin, DocumentationType> apiListingScanners;
    @Autowired
    @Qualifier("responseBuilderPluginRegistry")
    private PluginRegistry<ResponseBuilderPlugin, DocumentationType> responsePlugins;
    @Autowired
    @Qualifier("modelNamesRegistryFactoryPluginRegistry")
    private PluginRegistry<ModelNamesRegistryFactoryPlugin, DocumentationType> modelNameRegistryFactoryPlugins;

    public Collection<DocumentationPlugin> documentationPlugins() throws IllegalStateException {
        //List<DocumentationPlugin> plugins = documentationPlugins.getPlugins();
        Map<String, DocumentationPlugin> beansOfType = applicationContext.getBeansOfType(DocumentationPlugin.class);
        List<DocumentationPlugin> plugins = new ArrayList<>(beansOfType.values());
        ensureNoDuplicateGroups(plugins);
        if (plugins.isEmpty()) {
            return singleton(defaultDocumentationPlugin());
        }
        return plugins;
    }

    @SuppressWarnings("deprecation")
    public Compatibility<springfox.documentation.service.Parameter, RequestParameter>
    parameter(ParameterContext parameterContext) {
        for (ParameterBuilderPlugin each : parameterPlugins.getPluginsFor(parameterContext.getDocumentationType())) {
            each.apply(parameterContext);
        }
        return new Compatibility<>(
                parameterContext.parameterBuilder().build(),
                parameterContext.requestParameterBuilder().build());
    }

    public Response response(ResponseContext responseContext) {
        for (ResponseBuilderPlugin each : responsePlugins.getPluginsFor(responseContext.getDocumentationType())) {
            each.apply(responseContext);
        }
        return responseContext.responseBuilder().build();
    }

    @SuppressWarnings("deprecation")
    public Compatibility<springfox.documentation.service.Parameter, RequestParameter> expandParameter(
            ParameterExpansionContext context) {
        for (ExpandedParameterBuilderPlugin each : parameterExpanderPlugins.getPluginsFor(context.getDocumentationType())) {
            each.apply(context);
        }
        return new Compatibility<>(
                context.getParameterBuilder().build(),
                context.getRequestParameterBuilder().build());
    }

    public Operation operation(OperationContext operationContext) {
        for (OperationBuilderPlugin each : operationBuilderPlugins.getPluginsFor(operationContext.getDocumentationType())) {
            each.apply(operationContext);
        }
        return operationContext.operationBuilder().build();
    }


    public ApiListing apiListing(ApiListingContext context) {
        for (ApiListingBuilderPlugin each : apiListingPlugins.getPluginsFor(context.getDocumentationType())) {
            each.apply(context);
        }
        return context.apiListingBuilder().build();
    }

    public Set<ModelContext> modelContexts(RequestMappingContext context) {
        DocumentationType documentationType = context.getDocumentationContext().getDocumentationType();
        for (OperationModelsProviderPlugin each : operationModelsProviders.getPluginsFor(documentationType)) {
            each.apply(context);
        }
        return context.operationModelsBuilder().build();
    }

    public ModelNamesRegistryFactoryPlugin modelNamesGeneratorFactory(DocumentationType documentationType) {
        return modelNameRegistryFactoryPlugins.getPluginOrDefaultFor(
                documentationType,
                new DefaultModelNamesRegistryFactory());
    }

    private DocumentationPlugin defaultDocumentationPlugin() {
        return new Docket(DocumentationType.OAS_30);
    }

    public DocumentationContextBuilder applyDefaults(
            DocumentationType documentationType,
            DocumentationContextBuilder builder) {
        defaultsProviders.getPluginsFor(documentationType)
                .forEach(each -> each.apply(builder));
        return builder;
    }

    public Function<String, String> decorator(final PathContext context) {
        return input -> {
            Iterable<Function<String, String>> decorators
                    = pathDecorators.getPluginsFor(context.documentationContext()).stream()
                    .map(each -> each.decorator(context)).collect(toList());
            String decorated = input;
            for (Function<String, String> decorator : decorators) {
                decorated = decorator.apply(decorated);
            }
            return decorated;
        };
    }

    public Collection<ApiDescription> additionalListings(final ApiListingScanningContext context) {
        final DocumentationType documentationType = context.getDocumentationContext().getDocumentationType();
        List<ApiDescription> additional = new ArrayList<>();
        for (ApiListingScannerPlugin each : apiListingScanners.getPluginsFor(documentationType)) {
            additional.addAll(each.apply(context.getDocumentationContext()));
        }
        return additional;
    }
}

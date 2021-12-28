package com.lhstack.swagger.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lhstack
 * @date 2021/9/30
 * @class BaseFilter
 * @since 1.8
 */
public class BasicFilter {
    private final Logger logger = LoggerFactory.getLogger(BasicFilter.class);
    protected List<Pattern> urlFilters = null;

    public BasicFilter() {
        this.urlFilters = new ArrayList();
        this.urlFilters.add(Pattern.compile(".*?/doc\\.html.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/v2/api-docs.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/v3/api-docs.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/v2/api-docs-ext.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/swagger-resources.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/swagger-ui\\.html.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/swagger-ui.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/ui.*", 2));
        this.urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/security.*", 2));
    }

    protected boolean match(String uri) {
        boolean match = false;
        if (uri != null) {
            Iterator var3 = this.getUrlFilters().iterator();

            while(var3.hasNext()) {
                Pattern pattern = (Pattern)var3.next();
                if (pattern.matcher(uri).matches()) {
                    match = true;
                    break;
                }
            }
        }

        return match;
    }

    protected String decodeBase64(String source) {
        String decodeStr = null;
        if (source != null) {
            try {
                byte[] bytes = Base64.getDecoder().decode(source);
                decodeStr = new String(bytes);
            } catch (Exception var4) {
                this.logger.error(var4.getMessage(), var4);
            }
        }

        return decodeStr;
    }

    public List<Pattern> getUrlFilters() {
        return this.urlFilters;
    }
}
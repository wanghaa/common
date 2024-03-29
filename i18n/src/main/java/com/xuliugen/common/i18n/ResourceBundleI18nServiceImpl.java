package com.xuliugen.common.i18n;

import com.xuliugen.common.i18n.impl.ResourceBundleI18nService;
import com.xuliugen.common.i18n.support.I18nServiceAccessor;

/**
 * I18nService实现类
 * @author Ken
 * @since 2012-11-5
 */
public class ResourceBundleI18nServiceImpl extends ResourceBundleI18nService {

    public I18nServiceAccessor getAccessor() {
        return new I18nServiceAccessor(this);
    }
}

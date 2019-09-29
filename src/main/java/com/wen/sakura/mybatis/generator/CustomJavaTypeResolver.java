package com.wen.sakura.mybatis.generator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

import java.sql.Types;
import java.util.Properties;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    protected boolean forceTinyIntInteger;
    protected boolean forceSmallIntInteger;

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.forceTinyIntInteger = StringUtility.isTrue(properties.getProperty("forceTinyIntInteger"));
        this.forceSmallIntInteger = StringUtility.isTrue(properties.getProperty("forceSmallIntInteger"));
        if (this.forceTinyIntInteger) {
            super.typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                    new FullyQualifiedJavaType(Integer.class.getName())));
        }
        if (this.forceSmallIntInteger) {
            super.typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT",
                    new FullyQualifiedJavaType(Integer.class.getName())));
        }
    }
}

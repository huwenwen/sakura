package com.wen.sakura.mybatis.generator;

import com.wen.sakura.util.Helper;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class XmlFileOverwritePlugin extends PluginAdapter {
    private boolean overwrite = true;

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMapFile, IntrospectedTable introspectedTable) {
        if (overwrite) {
            try {
                Field mergedField = GeneratedXmlFile.class.getDeclaredField("isMergeable");
                if (!mergedField.isAccessible()) {
                    mergedField.setAccessible(true);
                }
                mergedField.setBoolean(sqlMapFile, false);
                System.out.println(sqlMapFile.getFileName() + " isMergeable " + sqlMapFile.isMergeable());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.sqlMapGenerated(sqlMapFile, introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Helper.isNotEmpty(properties.getProperty("overwrite"))) {
            this.overwrite = StringUtility.isTrue(properties.getProperty("overwrite"));
        }
    }
}

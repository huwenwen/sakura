package com.wen.sakura.mybatis.generator;

import com.wen.sakura.util.Helper;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.Properties;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class NamingPlugin extends PluginAdapter {
    private String daoSuffix = "Mapper";
    private String xmlSuffix = "Mapper";
    private String exampleSuffix = "Example";
    private String tablePrefix = "t";
    private String tableSuffix = "";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String dx = Helper.pick(() -> context.getProperty("daoSuffix"), () -> daoSuffix);
        String sx = Helper.pick(() -> context.getProperty("xmlSuffix"), () -> xmlSuffix);
        String ex = Helper.pick(() -> context.getProperty("exampleSuffix"), () -> exampleSuffix);
        String ts = Helper.pick(() -> context.getProperty("tableSuffix"), () -> tableSuffix);
        String tableName = getModelName(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        String daopack = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
        String modelpack = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();
        introspectedTable.setBaseRecordType(modelpack.concat(".").concat(tableName).concat(ts));
        introspectedTable.setMyBatis3JavaMapperType(daopack.concat(".").concat(tableName).concat(dx));
        introspectedTable.setMyBatis3XmlMapperFileName(tableName.concat(sx).concat(".xml"));
        introspectedTable.setExampleType(modelpack.concat(".").concat(tableName).concat(ex));
        super.initialized(introspectedTable);
    }

    public String getModelName(String tableName) {
        String tx = Helper.pick(() -> context.getProperty("tablePrefix"), () -> tablePrefix);
        StringBuilder name = new StringBuilder();
        String[] namearray = tableName.replaceFirst(tx, "").split("_");
        for (String n : namearray) {
            if (Helper.isEmpty(n)) {
                continue;
            }
            name.append(n.substring(0, 1).toUpperCase());
            if (n.length() > 1) {
                name.append(n.substring(1));
            }
        }
        return name.toString();
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        if (Helper.isNotEmpty(properties.getProperty("daoSuffix"))) {
            this.daoSuffix = properties.getProperty("daoSuffix");
        }
        if (Helper.isNotEmpty(properties.getProperty("xmlSuffix"))) {
            this.xmlSuffix = properties.getProperty("xmlSuffix");
        }
        if (Helper.isNotEmpty(properties.getProperty("exampleSuffix"))) {
            this.exampleSuffix = properties.getProperty("exampleSuffix");
        }
        if (Helper.isNotEmpty(properties.getProperty("tablePrefix"))) {
            this.tablePrefix = properties.getProperty("tablePrefix");
        }
        if (Helper.isNotEmpty(properties.getProperty("tableSuffix"))) {
            this.tableSuffix = properties.getProperty("tableSuffix");
        }
    }
}

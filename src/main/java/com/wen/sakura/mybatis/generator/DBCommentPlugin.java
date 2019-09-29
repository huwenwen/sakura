package com.wen.sakura.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class DBCommentPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
        topLevelClass.addJavaDocLine(" **/");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        generateFieldExplain(field, introspectedColumn);
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        generateMethodExplain(method, introspectedColumn);
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        generateMethodExplain(method, introspectedColumn);
        return true;
    }

    private void generateFieldExplain(Field field, IntrospectedColumn introspectedColumn) {
        String comment = (introspectedColumn.getRemarks() == null ? "" : introspectedColumn.getRemarks());
        field.addJavaDocLine("/** " + comment + " */");
    }

    private void generateMethodExplain(Method method, IntrospectedColumn introspectedColumn) {
        String comment = (introspectedColumn.getRemarks() == null ? "" : introspectedColumn.getRemarks());
        method.addJavaDocLine("/** " + comment + " */");
    }
}

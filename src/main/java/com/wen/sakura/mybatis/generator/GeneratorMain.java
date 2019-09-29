package com.wen.sakura.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huwenwen
 * @date 2019/9/29
 */
public class GeneratorMain {

    private static final String GENERATOR_CONFIG_PATH = "generatorConfig.xml";

    /**
     * 替代generator插件
     *
     * @param args
     */
    public static void main(String[] args) {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        try {
            boolean overwrite = true;
            URL resource = GeneratorMain.class.getClassLoader().getResource(GENERATOR_CONFIG_PATH);
            if (resource == null) {
                System.err.println("not found generatorConfig.xml, give path is " + GENERATOR_CONFIG_PATH);
                return;
            }
            Configuration config = cp.parseConfiguration(new File(resource.toURI()));
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

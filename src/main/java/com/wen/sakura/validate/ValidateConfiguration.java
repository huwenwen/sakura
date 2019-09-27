package com.wen.sakura.validate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ValidatorFactory;
import java.util.Properties;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public class ValidateConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(ValidatorFactory validatorFactory) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidatorFactory(validatorFactory);
        processor.setProxyTargetClass(true);
        return processor;
    }

    @Bean
    public ValidatorFactory validatorFactory() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:META-INF/validate");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFileEncodings(new Properties());
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
//        factory.setProviderClass(HibernateValidator.class);
        return factory;
    }


}

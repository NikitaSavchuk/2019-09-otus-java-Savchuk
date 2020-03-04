package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.frontend.FrontendService;
import ru.otus.frontend.FrontendServiceImpl;
import ru.otus.frontend.handlers.GetUserDataResponseHandler;
import ru.otus.hibernate.handlers.GetUserDataRequestHandler;
import ru.otus.messagesystem.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@ComponentScan("ru.otus")
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DBServiceCachedUser dbServiceCachedUser;

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    ApplicationContext applicationContext;

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }


    @Bean
    public MsClient frontendMsClient() {
        return new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem());
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(frontendMsClient(), DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean
    public MsClient databaseMsClient() {
        return new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem());
    }

    @PostConstruct
    private void postConstruct() {
        frontendMsClient().addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(frontendService()));
        databaseMsClient().addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbServiceCachedUser));

        messageSystem().addClient(frontendMsClient());
        messageSystem().addClient(databaseMsClient());
    }

    @PreDestroy
    private void preDestroy() {
        try {
            messageSystem().dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/WEB-INF/static/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
                .resourceChain(false).addResolver(new WebJarsResourceResolver());
    }
}

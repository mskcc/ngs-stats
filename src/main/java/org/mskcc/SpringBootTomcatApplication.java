package org.mskcc;

import org.mskcc.picardstats.PicardStatsController;
import org.mskcc.sequencer.SequencerDoneController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
from: https://www.baeldung.com/spring-boot-war-tomcat-deploy w/Swagger2 added
 */
@SpringBootApplication
@EnableSwagger2
public class SpringBootTomcatApplication extends SpringBootServletInitializer implements CommandLineRunner {
    @Autowired
    private PicardStatsController c;

    @Autowired
    private SequencerDoneController x;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTomcatApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("NGS Stats")
                .description("Picard Stats and more.")
                .contact(new Contact("The IGO Data Team", "https://igo.mskcc.org", "zzPDL_SKI_IGO_DATA@mskcc.org"))
                .build();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        MyFilter myFilter = new MyFilter();
        filterRegistrationBean.setFilter(myFilter);
        return filterRegistrationBean;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && args[0].equals("-buildDatabase"))
            c.buildDatabaseFromPicardFiles();
    }
}

class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getRequestURI().equals("") || request.getRequestURI().equals("/")){
            response.sendRedirect("/swagger-ui.html");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {
    }
}
package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Import(value={MybatisConfig.class})
@Configuration
@EnableWebMvc
@ComponentScan(basePackages= "com.bloomtoget")
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/",".jsp");

    }
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resource/**").addResourceLocations("/resource/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resource/js/");
        registry.addResourceHandler("/import/**").addResourceLocations("/resource/import/");
        registry.addResourceHandler("/img/**").addResourceLocations("/resource/img/");
        registry.addResourceHandler("/imgUpload/**").addResourceLocations("/resource/imgUpload/");
        registry.addResourceHandler("/css/**").addResourceLocations("/resource/css/");
    }


}

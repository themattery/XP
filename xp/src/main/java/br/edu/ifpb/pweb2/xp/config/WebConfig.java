package br.edu.ifpb.pweb2.xp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagens-upload/**")
                .addResourceLocations(UploadUtil.getDiretorioUploadsUri());

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

}

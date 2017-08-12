package com.bluescale.web.configuration;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.bluescale.web","com.bluescale.business"})
public class MappingConfiguration extends WebMvcConfigurerAdapter{
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/templates/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
	
	 @Bean
	    public MultipartConfigElement multipartConfigElement() {
	        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
	        multipartConfigFactory.setMaxFileSize("5120MB");
	        multipartConfigFactory.setMaxRequestSize("5120MB");
	        return multipartConfigFactory.createMultipartConfig();
	    }
	    @Bean  
	    public UrlBasedViewResolver setupViewResolver() {  
	        UrlBasedViewResolver resolver = new UrlBasedViewResolver();  
	        resolver.setPrefix("/templates/");  
	        resolver.setSuffix(".jsp");  
	        resolver.setViewClass(JstlView.class);
	        return resolver;  
	    }
	    
	    @Bean
	    public StandardServletMultipartResolver multipartResolver(){
	        return new StandardServletMultipartResolver();
	    }
	    
	    @Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	        // Serving static files using the Servlet container's default Servlet.
	        configurer.enable();
	    }
	 
	    @Override
	    public void addFormatters(FormatterRegistry formatterRegistry) {
	        // add your custom formatters
	    }
	    
	    @Override
	        public void addViewControllers(ViewControllerRegistry registry) {
	            registry.addViewController("/login").setViewName("login");
	        }

}
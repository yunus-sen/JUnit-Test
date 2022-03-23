package com.yunussen.mobilappws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



//global olarak cors enable ettim
@Configuration
public class WebConfig implements  WebMvcConfigurer  {
	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry
		.addMapping("/**")//tüm controllerlarıma izin verdim
		.allowedMethods("*")//tüm methodlara
		.allowedOrigins("*");//tüm domainlere

	}
}

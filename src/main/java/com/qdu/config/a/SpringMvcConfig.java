//package com.qdu.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
////这是一个Spring MVC配置类，用于配置web相关的配置
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer{
//
//	//文件上传有很多方式，可以使用配置虚拟路径，将一个虚拟路径映射到一个实际路径
//	//从而可以通过虚拟路径来访问实际路径下的文件
//	//addResourceHandlers()方法中用于配置一些静态资源（但是因为Spring Boot默认帮你配置了静态资源的访问）
//	//通常不需要再添加静态资源的配置了。还可以用于配置虚拟路径。
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/up/**").addResourceLocations("file:C:/upload/");
//	}
//
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/index").setViewName("index");
//		//如果希望跳转到templates目录下的某个动态页面，需要经控制器跳转
//		//通常在控制器中处理完请求，再指定要跳转的页面
//		//但是如果对一个请求不做其他处理，只是跳转页面，可以直接在这里配置
//		//这里的配置说明如果发送的请求的url是/to_upload，会跳转到upload页面
//
//		registry.addViewController("/index3").setViewName("index3");
//		registry.addViewController("/index4").setViewName("index4");
//		registry.addViewController("/userself").setViewName("userself");
//		registry.addViewController("/profile").setViewName("profile");
//		registry.addViewController("/showprofile").setViewName("showprofile");
//		registry.addViewController("/changeHead").setViewName("changeHead");
//		registry.addViewController("/publishhouse").setViewName("publishhouse");
//		registry.addViewController("/myhouse").setViewName("myhouse");
//		registry.addViewController("/updatemyhouse").setViewName("updatemyhouse");
//		registry.addViewController("/password").setViewName("password");
//		registry.addViewController("/updateusername").setViewName("updateusername");
//		registry.addViewController("/adminlogin").setViewName("adminlogin");
//		registry.addViewController("/admin").setViewName("admin");
//		registry.addViewController("/adminhouse").setViewName("adminhouse");
//		registry.addViewController("/adminadd").setViewName("adminadd");
//		registry.addViewController("/adminpwd").setViewName("adminpwd");
//		registry.addViewController("/adminpwd_fail").setViewName("adminpwd_fail");
//		registry.addViewController("/login").setViewName("login");
//		registry.addViewController("/me").setViewName("me");
//		registry.addViewController("/mess").setViewName("mess");
//		registry.addViewController("/register").setViewName("register");
//		registry.addViewController("/register_fail").setViewName("register_fail");
//		registry.addViewController("/password_fail").setViewName("password_fail");
//		registry.addViewController("/update_fail").setViewName("update_fail");
//		registry.addViewController("/register_succeed").setViewName("register_succeed");
//
//		//如果请求的url是/login，跳转到login_page.html页面
//		registry.addViewController("/toLogin").setViewName("login");
//		//如果请求的url是/login_failed，跳转到login_faile.html页面
//		registry.addViewController("/login_failed").setViewName("login_fail");
//		registry.addViewController("/toRegister").setViewName("register");
//		registry.addViewController("/housedetail").setViewName("housedetail");
//		registry.addViewController("/houseinfo").setViewName("houseinfo");
//		registry.addViewController("/header").setViewName("header");
//		registry.addViewController("/houseSearchPage").setViewName("houseSearchPage");
//
//
//	}
//
//	//虽然Spring Boot提供默认的MultipartResolver配置，
//	//但是如果希望使用其他第三方库（commons-fileupload）实现文件上传
//	//可以自行添加MultipartResolver配置，来覆盖默认配置
////	@Bean
////	public CommonsMultipartResolver multipartResolver() {
////		CommonsMultipartResolver resolver=new CommonsMultipartResolver();
////		resolver.setMaxUploadSize(10*1024*1024); //这里限制上传大小不能超过10M
////		resolver.setDefaultEncoding("UTF-8"); //解决请求参数中可能出现的中文乱码
////		return resolver;
////	}
//
//}

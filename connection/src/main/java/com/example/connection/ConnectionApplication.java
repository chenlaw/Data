package com.example.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConnectionApplication {
	@Autowired
	private RestTemplate restTemplate;
	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory factory){
		return new RestTemplate(factory);
	}

	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(180000);//单位为ms
		factory.setConnectTimeout(5000);//单位为ms
		return factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConnectionApplication.class, args);

	}

}

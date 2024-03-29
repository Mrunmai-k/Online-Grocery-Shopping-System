package com.example.grocerystore.configuration;

import org.modelmapper.ModelMapper;
import com.example.grocerystore.util.mappings.MappingsInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        MappingsInitializer.initMappings(modelMapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

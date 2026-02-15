package com.alex.myexpenses.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alex.myexpenses.dto.user.UserDTO;
import com.alex.myexpenses.entity.user.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ModelMapperConfig {
	
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration()
		.setFieldMatchingEnabled(true)
		.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
		.setSkipNullEnabled(true)
		.setAmbiguityIgnored(true)
		.setMatchingStrategy(MatchingStrategies.STRICT);
		// IIgnor the password during the mapping between UserEntity and UserDTO
		modelMapper.addMappings(new PropertyMap<UserEntity, UserDTO>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());  // Ignor the password
            }
        });
		return modelMapper;
	}

}

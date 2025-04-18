package com.alex.myexpenses.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ModelMapperConfig {
	
	
	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper() {
			 @Override
			    public <D> D map(Object source, Class<D> destinationType) {
			        Object tmpSource = source;
			        if(source == null){
			            tmpSource = new Object();
			        }

			        return super.map(tmpSource, destinationType);
			    }
		};
		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
		.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
//		mapperConfiguration(modelMapper);
		return modelMapper;
	}

}

package br.ufpb.dcx.dsc.finance_management;

import org.modelmapper.record.RecordModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;


@SpringBootApplication
public class FinanceManagementApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper().registerModule(new RecordModule());
	}

	public static void main(String[] args) {
		SpringApplication.run(FinanceManagementApplication.class, args);
	}

}

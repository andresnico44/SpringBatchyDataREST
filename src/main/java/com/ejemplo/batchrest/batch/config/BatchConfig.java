package com.ejemplo.batchrest.batch.config;

import com.ejemplo.batchrest.domain.Persona;
import com.ejemplo.batchrest.domain.PersonaCsv;
import com.ejemplo.batchrest.batch.processor.PersonaProcessor;
import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job importarPersonasDesdeCsvJob(JobRepository jobRepository, Step importarDesdeCsvStep) {
        return new JobBuilder("importarPersonasDesdeCsvJob", jobRepository)
                .flow(importarDesdeCsvStep)
                .end()
                .build();
    }

    @Bean
    public Step importarDesdeCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                     FlatFileItemReader<PersonaCsv> csvReader,
                                     PersonaProcessor processor,
                                     JpaItemWriter<Persona> jpaWriter) {
        return new StepBuilder("importarDesdeCsvStep", jobRepository)
                .<PersonaCsv, Persona>chunk(5, transactionManager)
                .reader(csvReader)
                .processor(processor)
                .writer(jpaWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<PersonaCsv> csvReader() {
        return new FlatFileItemReaderBuilder<PersonaCsv>()
                .name("personaCsvReader")
                .resource(new ClassPathResource("personas.csv")) // Archivo CSV en la carpeta resources
                .lineMapper(new DefaultLineMapper<PersonaCsv>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("nombre", "edad");
                        setDelimiter(",");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<PersonaCsv>() {{
                        setTargetType(PersonaCsv.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Persona> jpaWriter(EntityManagerFactory emf) {
        return new JpaItemWriterBuilder<Persona>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public PersonaProcessor processor() {
        return new PersonaProcessor();
    }

    @Bean
    public DataSourceInitializer batchDataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-postgresql.sql"));
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(true);
        return initializer;
    }
}
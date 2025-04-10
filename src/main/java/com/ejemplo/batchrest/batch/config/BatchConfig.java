package com.ejemplo.batchrest.batch.config;


import com.ejemplo.batchrest.domain.Persona;
import com.ejemplo.batchrest.batch.processor.PersonaProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job importarPersonasJob(JobRepository jobRepository, Step importarStep) {
        return new JobBuilder("importarPersonasJob", jobRepository)
                .flow(importarStep)
                .end()
                .build();
    }

    @Bean
    public Step importarStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              JpaPagingItemReader<Persona> reader,
                              PersonaProcessor processor,
                              JpaItemWriter<Persona> writer) {
        return new StepBuilder("importarStep", jobRepository)
                .<Persona, Persona>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Persona> reader(EntityManagerFactory emf) {
        return new JpaPagingItemReaderBuilder<Persona>()
                .name("personaReader")
                .entityManagerFactory(emf)
                .queryString("SELECT p FROM Persona p")
                .pageSize(5)
                .build();
    }

    @Bean
    public JpaItemWriter<Persona> writer(EntityManagerFactory emf) {
        return new JpaItemWriterBuilder<Persona>()
                .entityManagerFactory(emf)
                .build();
    }
}


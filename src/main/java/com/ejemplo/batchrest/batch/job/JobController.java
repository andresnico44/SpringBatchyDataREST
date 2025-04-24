package com.ejemplo.batchrest.batch.job;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importarPersonasDesdeCsvJob;

    @PostMapping("/importar-csv")
    public String ejecutarJobImportarCsv() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importarPersonasDesdeCsvJob, params);
        return "Job de importación desde CSV ejecutado con estado: " + execution.getStatus();
    }
}
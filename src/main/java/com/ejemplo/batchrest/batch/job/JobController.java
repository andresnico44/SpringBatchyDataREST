package com.ejemplo.batchrest.batch.job;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importarPersonasJob;

    @PostMapping("/importar")
    public String ejecutarJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(importarPersonasJob, params);
        return "Job ejecutado con estado: " + execution.getStatus();
    }
}

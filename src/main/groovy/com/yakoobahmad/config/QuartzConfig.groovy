package com.yakoobahmad.config

import com.yakoobahmad.TestJob
import org.quartz.SimpleTrigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.JobDetailFactoryBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean

import javax.sql.DataSource

@Configuration
class QuartzConfig {

    @Autowired
    private DataSource dataSource

    @Bean
    public org.quartz.spi.JobFactory jobFactory(ApplicationContext applicationContext) {
        QuartzJobFactory jf = new QuartzJobFactory()
        jf.setApplicationContext(applicationContext)
        return jf
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean()
        scheduler.setOverwriteExistingJobs(true)
        scheduler.setApplicationContextSchedulerContextKey("applicationContext")
        scheduler.setConfigLocation(new ClassPathResource("quartz.properties"))
        scheduler.setAutoStartup(true)
        scheduler.setDataSource(dataSource)
        scheduler.setWaitForJobsToCompleteOnShutdown(true)


        scheduler.setTriggers(testJobTrigger().getObject())

        return scheduler
    }

    @Bean(name = "testJobTrigger")
    public SimpleTriggerFactoryBean testJobTrigger() {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean()
        factoryBean.setJobDetail(testJobDetails().getObject())
        factoryBean.setStartDelay(5)
        factoryBean.setRepeatInterval(2000)
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT)
        return factoryBean
    }

    @Bean(name = "testJobDetails")
    public JobDetailFactoryBean testJobDetails() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean()
        jobDetailFactoryBean.setJobClass(TestJob.class)
        jobDetailFactoryBean.setDescription("test Job")
        jobDetailFactoryBean.setDurability(true)
        jobDetailFactoryBean.setName("TestJob")
        return jobDetailFactoryBean
    }

}

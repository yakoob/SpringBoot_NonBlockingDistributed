package com.yakoobahmad

import akka.actor.ActorRef
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        AkkaService akkaService = Application.context?.getBean(AkkaService.class)
        ServerService serverService = Application.context?.getBean(ServerService.class)
        akkaService?.homeManager?.tell(serverService?.listActive()?.toListString(), ActorRef.noSender())
        println " "

    }

}
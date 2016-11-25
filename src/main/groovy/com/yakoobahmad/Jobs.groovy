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

        // do call to db on separate thread and subscribe to the result so that we can push into the actor system
        rx.Observable.just(serverService?.listActive()?.toListString())
            .subscribe({serverServiceResults ->
                // once serverService has finished its work
                akkaService?.homeManager?.tell(serverServiceResults, ActorRef.noSender())
            })
        println " "

    }

}
package com.yakoobahmad

import com.google.gson.Gson
import grails.gorm.annotation.Entity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Entity
class Server {

    enum Type {
        //external server types
        DNS,
        GOOGLE_HTTP_PROXY,
        ALEXA_HTTP_PROXY,
        WEBOFTRUST_HTTP_PROXY,

        // our servers
                OPENSIPS,
        WEBAPP,
        CCAPP, // rename CALLCONTROL
        WEBAPP_CCAPP, // CALLCONTROL_AND_WEBPP
        FREESWITCH,
        FREESWITCH_GROUP,
        FILESTORE
    }

    enum Location { BRU, HKG, LAX9841, SYD, TYO, ALC, ATL, DTLA }
    enum Status { ACTIVE, DRAINSTOP, FAILED }
    enum Environment { PRODUCTION, QA, STAGING, INTEGRATION, DEVELOPMENT }
    String name
    Type type
    Location location
    Status status

    // Fields to be updated on app start in ServerService.thisServer()
    String ipAddress    // Just the address is fine, we don't need all the detail from the IpAddress object.
    String hostname     // ex: web123 or bobs-macbook, not necessarily the same as name.
    String fullDns      // ex: web123.yakoobahmad.com
    Environment environment

    // Akka port for our servers, proxy port for remote proxies
    Integer port

    // For third-party proxy servers.
    // Also uses ipAddress & port above.
    String userName
    String password
    Integer priority
    Integer weight

    Boolean enablePdfProcessing // used to enable server to participate in converting files into pdf.

    static constraints = {
        name nullable: true
        status nullable: true
        created nullable: true
    }

    static mapping = {

        datasources(['replicate'])
        comment 'replicate'
    }

    String toString(){
        return "Server[id:$id | name:$name | status:$status | ipaddress:$ipAddress]"
    }

}

@Service
class ServerService {

    public Server getThisServer(){
        return listActive()?.find{Server s -> s.name == "DEV-FULLSTACK"}
    }

    def listActive(){
        Server.withNewSession {
            return Server.replicate.findAll()
        }
    }

}

@RestController
@RequestMapping(value="/server")
class ServerController {

    @Autowired
    ServerService serverService

    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    def listActiveAccounts() {
        return new Gson().toJson(serverService.listActive()).toString()
    }

    /*
    @RequestMapping(value = "/goToPaid/{accountId}", method = RequestMethod.PUT)
    def goToPaid(@PathVariable String accountId) {
        return accountService.goToPaid(accountId?.toLong())
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    def update(@PathVariable String id, @RequestBody Account account) {

        if (!Account.read(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND)

        return new ResponseEntity<Account>(account.attach().save(failOnError:true), HttpStatus.OK)
    }
    */
}
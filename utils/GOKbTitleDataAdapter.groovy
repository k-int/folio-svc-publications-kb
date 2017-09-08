#!/usr/bin/groovy

@GrabResolver(name='mvnRepository', root='http://central.maven.org/maven2/')
@GrabResolver(name='kint', root='http://nexus.k-int.com/content/repositories/releases')
@Grapes([
  @Grab(group='net.sf.opencsv', module='opencsv', version='2.3'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='com.k-int', module='goai', version='1.0.2'),
  @Grab(group='org.slf4j', module='slf4j-api', version='1.7.6'),
  @Grab(group='org.slf4j', module='jcl-over-slf4j', version='1.7.6'),
  @Grab(group='net.sourceforge.nekohtml', module='nekohtml', version='1.9.14'),
  @Grab(group='xerces', module='xercesImpl', version='2.9.1')
])

import groovyx.net.http.*
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import org.apache.http.*
import org.apache.http.protocol.*
import java.nio.charset.Charset
import static groovy.json.JsonOutput.*
import groovy.util.slurpersupport.GPathResult
import org.apache.log4j.*
import com.k_int.goai.*;
import java.text.SimpleDateFormat

def config_file = new File('GOKbTitleDataAdapter-config.groovy')

def config = new ConfigSlurper().parse(config_file.toURL())
if ( ! config.maxtimestamp ) {
  println("Intialise timestamp");
  config.maxtimestamp = 0
}

println("Starting...");


try {
  //Titles
  println("Connect to ${config.oai_server}gokb/oai/titles");

  OaiClient oaiclient_titles = new OaiClient(host:config.oai_server+'gokb/oai/titles');
  oaiclient_titles.getChangesSince(null, 'gokb') { record ->
    try{

      // Example GOKb Record
      // <title id="567">
      //   <name>Unknown Title 0000-0027</name>
      //   <status>Expected</status>
      //   <editStatus>In Progress</editStatus>
      //   <shortcode>Unknown_Title_0000-0027</shortcode>
      //   <identifiers>
      //     <identifier namespace="issnl" value="0000-0027" />
      //     <identifier namespace="issn" value="0000-0027" />
      //     <identifier namespace="originEditUrl" value="https://gokb.openlibraryfoundaton.org/gokb/resource/show/org.gokb.cred.JournalInstance:567" />
      //   </identifiers>
      //   <imprint />
      //   <medium>Journal</medium>
      //   <OAStatus>Unknown</OAStatus>
      //   <continuingSeries />
      //   <publishedFrom />
      //   <publishedTo />
      //   <issuer />
      //   <history />
      //   <TIPPs count="0" />
      // </title>

      println("Process title with id:: ${record.metadata.gokb.title.@id}");
      println("Title Record:\n ${record.metadata.class.name} ${record.metadata}");

      // Extract an issn-l if one is present
      def issnl = record.metadata.gokb.title.identifiers.identifier.findAll {it.@namespace == 'issnl'}?.@value?.toString();
      println("Extracted issnl: ${issnl}");

      // GOKB Creates aggregate title records which represent all instances. This isn't ideal. We want to submit 1 instance record
      // For each ISSN / ISBN in a GOKb record, and to link those records by work.
      record.metadata.gokb.title.identifiers.identifier.each { identifier ->
         switch( identifier.@namespace ) {
           case 'issn':
             println("Resolve instance record for ISSN ${identifier.@value} and title ${record.metadata.gokb.title.name}");
             def pubskb_record = [
               title: record.metadata.gokb.title.name.toString(),
               identifiers:[
                 [namespace:'isxn', value:identifier.@value?.toString()]
               ]
             ]
             
             // If there is a issn-l, lets add a work
             if ( issnl ) {
               pubskb_record.work = [
                 title: record.metadata.gokb.title.name.toString(),
                 identifiers:[
                   [namespace:'isxn', value:issnl]
                 ]
               ]
             }

             println("Submit to resolver: ${pubskb_record}");

             break;
           case 'isbn':
             println("Resolve instance record for ISBN ${identifier.@value} and title ${record.metadata.gokb.title.name}");
             break;
           default:
             println("Ignoring ID from namespace ${identifier.@namespace}");
        }
      }
    }catch(Exception e){
      println "EXCEPTION WHILE PROCESSING TITLES"
      e.printStackTrace();
    }
  }
}
catch ( Exception e ) {
  e.printStackTrace();
}
finally {
}

println("Done.");

config_file.withWriter { writer ->
  config.writeTo(writer)
}


package pubskb

import groovy.transform.CompileStatic
import grails.rest.RestfulController

@CompileStatic
class InstanceController extends RestfulController {

  static responseFormats = ['json', 'xml']

  InstanceController() {
    super(Instance)
  }

  def search(String q, Integer max) { 
    if (q) {
        def query = Instance.where { 
          title ==~ "%${q}%"
        }
        respond query.list(max: Math.min( max ?: 10, 100)) 
    }
    else {
        respond([]) 
    }
  }

}

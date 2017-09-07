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

  /**
   * Resolve a json document describing an instance. If the document matches an instance already
   * in the KB, return the item, otherwise create a new item and return that.
   */
  def resolve() {
    log.debug("resolve");
    respond([title:'Decision and Control'], status: 200)
  }
}

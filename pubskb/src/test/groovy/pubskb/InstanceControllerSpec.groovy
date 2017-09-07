package pubskb

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import grails.test.hibernate.HibernateSpec

class InstanceControllerSpec extends HibernateSpec implements ControllerUnitTest<InstanceController> {

  static decision_and_control = [ title: 'Decision and Control', author: 'Beer, Stafford', identifiers: [ isbn:'1234-5678' ] ]

  static doWithSpring = {
        jsonSmartViewResolver(JsonViewResolver)
  }

  def setup() {
    Instance.saveAll(
      new Instance(title: 'Brain of the Firm'),
      new Instance(title: 'Platform for Change'),
      new Instance(title: 'Heart of Enterprise')
    )
  }

  def cleanup() {
  }

  void 'test the search action finds results'() {
    when: 'A query is executed that finds results'
      controller.search('rai', 10)

    then: 'The response is correct'
      response.json.size() == 1
      response.json[0].title == 'Brain of the Firm'
  }

  void 'test the resolve function'() {
    when: 'An item not already known in the KB is resolved'
      request.method='POST'
      request.json=decision_and_control
      controller.resolve()

    then:
      response.json.title=='Decision and Control'
  }
}

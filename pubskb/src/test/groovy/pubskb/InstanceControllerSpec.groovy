package pubskb

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import grails.test.hibernate.HibernateSpec

class InstanceControllerSpec extends HibernateSpec implements ControllerUnitTest<InstanceController> {

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
}

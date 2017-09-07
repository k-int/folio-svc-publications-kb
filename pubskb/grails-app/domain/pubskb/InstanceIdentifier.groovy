package pubskb

class InstanceIdentifier {

  Instance instance
  IdentifierNamespace namespace
  String value

  static constraints = {
    instance (nullable:false, blank:false)
    namespace (nullable:false, blank:false)
    value (nullable:false, blank:false)
  }
}

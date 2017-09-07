package pubskb

class Instance {

  Work work
  String title

  static constraints = {
     work (nullable:false, blank:false)
     title (nullable:false, blank:false)
  }

  static hasMany = [
    identifiers:InstanceIdentifier,
    identifierErrors:InstanceIdentifierErratum
  ]

  static mappedBy = [
    identifiers:'instance',
    identifierErrors:'instance'
  ]
}

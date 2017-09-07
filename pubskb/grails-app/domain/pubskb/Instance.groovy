package pubskb

class Instance {

  Work work
  String title
  RefdataValue itemType // BKM-Books and monographs / JOU-Journals / etc

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

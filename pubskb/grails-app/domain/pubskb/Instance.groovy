package pubskb

class Instance {

  Work work
  String title
  RefdataValue itemType // BKM-Books and monographs / SER - Series / etc

  static constraints = {
     work (nullable:true);
     title (nullable:false, blank:false)
     itemType (nullable:true);
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

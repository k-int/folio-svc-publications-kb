package pubskb

class Work {

  String title

  static constraints = {
    title(nullable:true, blank:false);
  }
}


# About

## Front end resolve function

pubskb is a microservice for growing a knowledgebase about publications. The idea is that the instance/resolve function will accept a JSON document (Described below, and likely will grow). The resolve function will then use a rulebase
to attempt to resolve that item to one already known about. This part of the knowledgebase will involve recording common errors, checking actions and general QA rules.

There can be three outcomes to calling the resolve endpoint

1) The item matched an existing item with confidence
1.1) An extra use case -- the item matched with confidence AND had some properties we have not seen before, so add the extra information to the KB
2) The item is considered new, and was added to the knowledgebase
3) The item could no be consistently resolved - EG It's identifiers and it's title were a complete mismatch.

## Back end sharing function

The fundamental issue this microservice tries to address is that any peer microservice needing title/publication data is likely to need large subsets of the title data. This leads to a microservice
anti-pattern which involves tight coupling of dependent services (EG ERM or packaging knowledgebase) with the data model of the reference service. The Domain Driven Development community are establishing
patterns to address these concerns, particularly, tools such as apache kafka are used to make data available to dependent services in ways that the dependent service needs. In essence, the important
contract between a service dependency and a service itself is that the dependent service will post in the event stream all the information needed for the service to create and store a copy of the data. 
This makes the dependent service entirely self contained, and allows local testing of the service and service resillience when the dependency is not available.

The fundamental back-end purpose of this microservice then is to publish an event stream of publications data which any dependent microservice can consume.

# The publications JSON document (In it's full form)
    
    {
      title: 'the authoritative title'
      variantTitles: [
        { title:'Authoritative Title' },
        { title:'Authoritative Title (the)' }
      ],
      publisher: {
        name:'A Publisher Name',
        identifiers:[
          {namespace:'isni', '897897324'}
        ]
      }
      identifiers:[
        { namespace:'isxn', value:'1345-5674' },
        { namespace:'isxn', value:'1345-5474' }
      ],
      work:{
        title:'the authoritiative title',                                  // Optional
        identifiers:[
          { namespace:'isxn', value:'1345-5674' }                          // This would be an ISSN-L (For example)
          { namespace:'GUID', value:'1345-5674-44323-35343-4353' }         // Allow local GUIDS for explicit linking
        ]
      }
    
    }
    
## Important notes

Instances with different ISSNs are different instances in the service. Instances linked by virtue of an ISSN-L are linked via work.

### Minimal Records

We're likely to see minimal records - perhaps even identifier only records. Assuming GOKb as a record source initially.

## About language variants

This microservice concerns itself with INSTANCE data (In the BIBFRAME sense). It is anticipated that language variants will become different instance records, rather than having multiple language variants of title data.

## About mis-cited title data

Errors/variability in recording the title, such as variations in omitting leading articles, are recorded by the knowledgebase for purposes of matching and error correction.

## Administrative actions

### Merge records

It may be possible that a record is duplicated. For example, we know that an item with ISSN 1234-5678 exists and is part of a group with ISSN-L 3456-4567 and sibling ISSN 3456-4567. We create a stub record 
which uses a work to link these 2 instance records together, but we don't know the title at that time. Later we see a record for a title with a DOI but no identifier, so create a new record. Later still,
we discover that these 2 items are the same, and need to merge them.




See also:
http://guides.grails.org/rest-hibernate/guide/index.html

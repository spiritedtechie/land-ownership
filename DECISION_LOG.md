
# Decision Log

## Feature Breakdown
Wanted to slice features out of overall requirement, and get the minimal thing working end-to-end, before layering
additional feature in. I broke down as follows:

1. Company tree, displaying only company IDs, small set of hard coded data (COMPLETE)
2. Company tree, displaying company names
3. Company tree, using real company data from CSV file
4. Company tree, displaying individual company land counts, hard coded land counts
5. Company tree, using real land parcels data from CSV file
6. Company tree, displaying aggregated land counts of company and all its sub-tree
7. ...

Only completed 1. in the time frame allocated - with assumption that using hard coded 
data could be a minimal viable (testable) product for at least some initial product feedback.

## Design

Broke out system into a few key parts:

1. __Build the full company graph__
2. __Scoping the graph to a particular requested company__ 
3. __Displaying the scoped graph__

These core areas are encapsulated to support change.

#### Data structure
Opted for a bi-directionally transversable graph.

This is backed by two hash maps:

1. Parent company for each company (known as the forward graph)
2. Child companies (set) for each company (known as the reverse graph)

This allows the graph to be efficiently scoped per request by:
1. Finding the node to scope - O(1)
2. Transversing the reverse graph whilst - O(log n)
3. Filtering (reducing) the forward graph along the way - O(log n)

It also kept the code simple vs. say implementing a bread-first search.

Much of the transversal is performed using recursion as it seems natural to 
recursively work up/down the tree in steps, and also it remove the boilerplate of 
looping logic (while loops).

#### Complexity analysis

n = number of companies; 
m = number of relationships

__On application load__

(Assuming the application was ultimately served as an API)

1. Time to load the reverse graph = O(n) 
2. Time to build the forward graph (adjacency list) = O(n x m)
3. Space of reverse graph = O(n)
4. Space of forward graph = O(n + m)

__Per request__

1. Time for creating scoped graph = O(log n)
2. Space of scoped graph = O(log n)

#### Conclusions
This trades off application load complexity vs. per-request complexity, supporting decent
per-request scoping of the graph by using more time and space on application load. It is essentially
an inverted index like on a database query.

It could fail if the company tree structure is deeply nested such that the O(log n) transversal
increases accordingly. i.e. if there were majority deeply nested company ownership relationships, 
transversal effectively could become closer to O(n) as the overall tree would be quite flat.

## Development

1. Utilised Java Streaming API - this is a more functional (declarative) approach, though appreciate 
for those unfamiliar to it, it could look confusing. Happy to explain anything or provide the
imperative alternative.

2. Used Guava library - however usage is quite low at present
so would consider removing it for now to keep the application runtime slim.

## Productionisation

Here are some thoughts for making the application operational, though what is viable depends on knowing more about
the context:

* __Service + API__ - allows for load-once of graph, and per-request scoping. In Java, I'd use something like Spring Boot, 
but I'm sure there is a Javascript alternative e.g. Express

* __Database__ - having a repository for company details which can be queried per request for companies on
the scoped graph. These could be cached as well if response times need optimising. 

Also feels like there may be a graph database (Neo4J?) which could store the graph itself
and support efficient operations on it. But a relational database could work too to hold the reverse graph. 
Potentially the forward graph adjacency list (company -> set of children) can be precomputed and stored in the DB.

* __Metrics__ - some options like response times, scoped graph sizes, top searched companies etc. to understand
how the system operates and is being used by users.

* __UI__ - beyond the CLI, a graph visualisation library for the company tree is possible. This would
also support displaying more complex company trees e.g. complex owning relationships.

## Thoughts For Future Work

__Adding land parcel aggregate counts__ - possible to periodically precompute these values using the full graph. 
This might mean eventual consistency, depending on if the counts are updated
in real-time (e.g. as companies are added / changed in the tree) or as a periodic batch process over the graph. Either way,
using company-only land parcel counts, and computing aggregate counts per request does not seem sensible 
as there are a lot more of them vs companies.

__Supporting complex relationships__ - e.g. multiple parents companies - The data structure 
can support this if the reverse graph HashMap was changed to have a set of parents.  
Perhaps the CLI display would need to change as complex relationship via text-only form is limiting.

__Per region / area searches__ - not thought about this too much, but possibility to partition the graph per region? 
Or attach a region to each company node in the graph and reduce the graph accordingly.

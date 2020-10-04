
# Decision Log

## Scope
Focus was to have something working end-to-end, so descoped some things for the purposes of the 
time-box. It's debatable how useful the application is in its current state, 
but provides a base to build upon further and iterate on.

Here is what was descoped from the basic requirements:

1. Using a full set of data - felt that focusing on the company tree listing would be more useful 
than initially loading an entire set of data. Assumed that hard coding some data in the software
would be sufficient for at least a minimal testable product.

2. Land parcel count - since each company is to display land parcel count of its entire
sub-tree, it felt like this was one large feature in its own right as the entire tree 
would need to be processed to aggregate the counts (more about this below in future work).

3. Displaying full company details -  I don't think this would be difficult to add in and seems 
like a sensible immediate next step, but wanted to scope minimally to ensure I could 
finish the tree listing within the time-box.


## Design

Opted for a bi-directionally searchable graph, utilising hash maps for
efficient search and retrieval of parts of the graph.

Broke out system into a few parts

1. Loading the full company graph as a bidirectionally navigable graph (per application load)
2. Scoping the graph to a particular requested company (per application request e.g. API call)
3. Displaying the graph

These core areas are encapsulated to support change.

### Complexity analysis

n = number of companies; 
m = number of relationships

#### On application load:

(Assuming the application was ultimately served as an API)

1. Time for loading the reverse graph = O(n) 
2. Time for building the forward graph (adjacency list) = O(n x m)
3. Space of reverse graph = O(n)
4. Space of forward graph = O(n + m)

#### Per request:

1. Time for creating scoped graph = O(log n)
2. Space complexity of scoped graph = O(log n)

#### Conclusions
Evidently, we are trading off increased space requirements and higher application start time
to allow for a decent performance in per-request search. In effect, we are creating various
in-memory search indexes to favour per-request operations, and avoid O(n x m) search time of the graph.

A big assumption here that underlies this design is that the number of deep sub-company relationships are minimal 
-- as this affects time of travelling up the graph to build the scoped graph. 
It would still be a lot less than O(n), unless there was an entirely flat company tree!

Overall, I think utilising a bi-directionally searchable graph has not added significant complexity to the
code and feel like the code is maintainable and the data structure encapsulated. 
Yet I acknowledge the space complexity tradeoffs of this choice.

### Development

1. Utilised Java Streaming API - this is a more functional (declarative) approach, though appreciate 
for those unfamiliar to it, it could look confusing. Happy to explain anything.

2. Used Guava library - however usage is quite low at present
so would consider removing it for now to keep the application runtime slim.

# Next Steps

1. Adding in company information e.g. name - perhaps a repository call to find full company details
for companies in the per-request scoped graph. Depending on number of requests, the data could be cached 
too.

2. Adding land parcel aggregate counts - potentially periodically precompute these values using the full graph. 
This might mean eventual consistency, depending on if the counts are updated
in real-time (e.g. as companies are added / changed in the tree) or as a batch process over the graph. Either way,
holding all land parcels individually, and computing aggregate counts per request does not seem sensible 
as there are a lot more of them vs companies.

3. Drilling into a company - this looks straightforward since finding a company is already efficient,
but perhaps the depth of expanding a company may need to be limited for companies with deep 
child relationships.

4. Supporting complex relationships e.g. multiple parents companies - The data structure 
can support this, but perhaps the display would need to change as complex relationship
via text-only form is limiting.

5. Per region / area searches - not thought about this too much, but possibility to partition the tree per region?

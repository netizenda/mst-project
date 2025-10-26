# mst project (Prim & Kruskal)

# overview
this repository contains my implementation of two classic MST algorithms Prim and Kruskal
the objective of the project was to implement both algorithms from scratch, test their correctness on multiple datasets, measure performance, and provide visuals for each input graph

# layout
mst-project/
data/ ass_3_input.json
graph_images/
src/ main/ java/
test/ java/
ass_3_output.json
pom.xml
.github/workflows/maven.yml
README.md

# tests
I included unit tests (src/test/java/MSTTest.java) which check:
- equal MST total weight (prim kruskal)
- MST contains exactly V1 edges
- MST is acyclic and connected
- operation counts and times are non-negative
  CI runs tests automatically on push/PR

# sum of results
full results are in ass_3_output.json

# analysis and conclusions
- both algorithms produce identical MST cost for all datasets verifying correctness
- prim typically performs better on dense graphs because it expands from one component and leverages a priority queue
- kruskal can be faster on sparse graphs because sorting edges and Union-Find operations become cheaper than repeated heap operations
- operation counters provide a practical (implementation-specific) perspective on where time is spent; they are useful for comparisons but are not formal complexity proofs
  use prim for dense graphs or when fast decrease key available
  use kruskal for sparse graphs and when you need a simple implementation

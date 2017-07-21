# kotlin-scientist-meetup
The code for my Chicago Kotlin presentation on creating a wrapper library for scientist4j. Plus how to do typesafe builders!

Follow along with how I evolved the code by looking at the following tags:
1-simple-wrapper - the initial wrapper that protects us from platform types
2-clean-builder - introduces the typesafe builder (more as an illustrative example)*


*caveat to introducing the builder here- I probably would not do that in real production code for this use case. Typesafe builders are better suited to structures that are either deeply nested, have many optional fields, or many repeated items.



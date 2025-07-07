# Release Train Metro Plan

We will analyze relations between OpenRewrite artifacts to understand the release train and see which releases a change implies.
We will use OpenRewrite to query the data and build a visualization on top of it to understand the whole picture.

## Abstract

Teams of Software developers have an allergy relationship to duplicate code for good reasons. Introducing shared libraries is a common medicine in these situations, but like each medicine, shared code has an unintended consequence — it leads to coupling between components and makes release planning mandatory.

By analyzing complex multilayer dependencies between your components, you can extract the necessary information and materialize the Metro plan of your release train. OpenRewrite's DataTable feature makes it easy to extract this information and visualize it. And what's more, with OpenRewrite, these analyses are efficiently scalable for hundreds of connections, making it perfect for complex codebases with many interdependent components.

In this session, I will show you how to utilize DataTable and OpenRewrite recipes to query your code base and gain valuable insights into your release planning process. On top of that, I will reveal the underlying Metro plan of your release train so you can plan your releases more easily in even the most complex scenarios.
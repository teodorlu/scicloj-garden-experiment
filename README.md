**⚠️ THIS PROJECT IS HIGHLY EXPERIMENTAL, PROCEED AT YOUR OWN RISK ⚠️**

# Scicloj knowledge garden: a memex?

This repo attempts to build a knowledge garden for scicloj out of hypertext, clojure and markdown.
We don't know (yet) whether that will succeed.

## Teodor believes in memexes built out of hypertext, and that the clojure data science community could use a memex to help with workflow discovery

Teodor believes that babashka and files is a good foundation for building a memex.
He has argued that point on Babashka-conf 2023:

https://play.teod.eu/build-your-own-little-memex-with-babasha/

After babashka-conf, he ran off to build a library for building memexes.
But he quickly got stuck, how can one build a library for building memexes without also building a memex?

That's where this repository comes in:
Teodor believes that a library for building memexes should be built alongside a memex.

On April 1st, 2024, Teodor and Daniel discussed whether a curriculum like https://currmap.clojure.camp/ could be made for Clojure data science:

https://clojurians.slack.com/archives/C0BQDEJ8M/p1711971630025459

Teodor did some quick prototyping, and concluded that:

- This seems doable
- but it's a bit of work
- and it would be nice if we could link into places where one can learn these things
- and it requires a "home".

This repository is an attempt to be that home.

## Design draft 2024

Mental model:

1. The source is text files (markdown) and metadata files (EDN)
2. An intermediate in-memory representation works as a narrow waist
   - Document content is Pandoc JSON
   - Site metadata is a datascript DB
3. The generated documents are pure HTML, preferrably fast
   - Extension is done with custom elements that can (optionally) read the metadata files
   - We want to avoid a big, complex build system for _this repo_
   - Instead of a big, complex build system, we can try linking generated javascript / clojurescript from other places

Goals:

- We can work on the entire system from a REPL
- We can _very quickly and easily_ regenerate any files when we have modified content (markdown) or metadata (EDN)
- We provide a porcelain for links

Notes:

- This repo itself contains the HTML files we push to the web, there is no build step
  - So that we can steal ideas & code from https://github.com/teodorlu/play.teod.eu
- The input format is Markdown
  - Teodor's previous prototyping has all been in Org-mode (an Emacs package).
    Teodor doesn't believe Org-mode is the right fit for a scicloj memex; Org-mode excludes non-Emacs users, Teodor believes that's bad.
- The repo contains its own code for working with the hypertext documents
  - The code is written in Clojure
  - And the code runs on JVM Clojure + Babashka where possible.
- Markdown parser: Pandoc, nextjournal/markdown or something else?
  - Per 2024-04-07, Teodor isn't sure.
  - Does nextjournal/markdown support babashka?
    - nextjournal/markdown is about 3x faster than shelling out to pandoc, according to some rough benchmarking by Teodor (when run from a JVM).
      - The advantage for nextjournal/markdown is largest for smaller documents, where process spawning and pandoc initialization has a bigger impact.
    - Teodor does not know
  - Teodor has the most experience with Pandoc
  - What about [Quarto]?
    - Teodor isn't sure
    - Does it bring any benefits?
  - For now, Pandoc is probably a good place to start.

[nextjournal/markdown]: https://github.com/nextjournal/markdown/
[Quarto]: https://quarto.org/
[Pandoc]: https://pandoc.org/
[Babashka]: https://babashka.org/

## Proposed first steps

Figuring out where to start can be hard!
Let's make something useful as quickly as possible, to avoid getting stuck in theoretical memex design.

Here's a proposal:

1. Actually make a curriculum map for Clojure data science
2. Build the curriculum map out of hypertext: each thing to learn refers to where one can learn that thing
3. Gradually extend the scope to serve as an index of Clojure data science content
4. When there's too much content for _one list of things_, start making tables / focused views that view specific aspects.
   - Either automatically as programs that read metadata and generate tables
   - Or manually as people who solve a problem by referring to where one needs to read, and what one needs to learn.

Teodor believes "make a curriculum" is a good start because it's a _complete_ goal.

## What about Kira's book and other attempts?

Teodor believes that there's no reason not to do _multiple_ initiatives for this.
Also, a book is way more cohesive than a memex.
A book has a _reading order_, which is amazing for the reader.
A memex is more loosely coupled, in a permanent state of work-in-progress.

Where a book can be written, and then be completed, a memex is built to grow continuously.
Where a book has a release date, and perhaps a second release, a memex is built to accrede knowledge in time.

In other words Teodor believes, Kira's book and other structured attemps at making the Clojure data science ecosystem more discoverable are purely synergetic with a scicloj knowledge garden.

## Places where we can steal ideas

- Wikipedia
  - Information structure
  - Mix between data (wikidata) and hypertext (wikipedia)
  - Contribution model
- Knowledge work on Twitter
  - Start with visa's ideas: https://twitter.com/FANswitchboard (optionally also https://twitter.com/visakanv)
- Tana as a tool for personal knowledge management (PKM) and collective knowledge management (CKM)
  - https://tana.inc/

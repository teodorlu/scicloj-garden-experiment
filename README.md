# Scicloj knowledge garden: a memex?

This repo attempts to build a knowledge garden for scicloj out of hypertext, clojure and markdown.
We don't know (yet) whether that will succeed.

## Context: Teodor believes in memexes built out of hypertext

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

- This repo itself contains the HTML files we push to the web, there is no build step
  - So that we can steal ideas & code from https://github.com/teodorlu/play.teod.eu
- The input format is Markdown
  - Teodor's previous prototyping has all been in Org-mode (an Emacs package).
    Teodor doesn't believe Org-mode is the right fit for a scicloj memex; Org-mode excludes non-Emacs users, Teodor believes that's bad.
- The repo contains its own code for working with the hypertext documents
  - The code is written in Clojure
  - And the code runs on JVM Clojure + Babashka where possible.

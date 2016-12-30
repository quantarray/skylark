Release:
========

* At shell prompt, type 'sbt release'. Follow the prompts.
* Go to https://oss.sonatype.org and log in.
* Under Staging Repositories, find the published artifacts and choose Close and then Release.
* Verify presence of artifacts in https://oss.sonatype.org/content/groups/public/com/quantarray/skylark-measure_2.11/.

Update docs:
============
* Update the release version in src/site/*.md.
* At shell prompt, type 'sbt ornate'.
* Copy target/site/* to docs/.
* Commit and push changes.

Publication Steps:
==================

* Update version in build.sbt
* Update version in README.md
* At sbt prompt, type 'publishSigned'
* Go to https://oss.sonatype.org and log in
* Under Staging Repositories, find the published artifacts and choose Close and then Promote
* Verify presence of artifacts in https://oss.sonatype.org/content/groups/public/com/quantarray/skylark-measure_2.11/


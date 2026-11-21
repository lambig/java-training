The Training regression workflow compiles and assembles the project on Corretto 17,
then compares JUnit outcomes with the verified baseline commit recorded in
`baseline.json` (11 tests, two intentional exercise failures).

New failures, changed failure types, missing tests, skipped tests, missing reports,
and build errors fail CI. Previously failing exercises may pass; new passing tests
are allowed. The baseline records test identities and exception types only, without
assertion messages, expected values, or solutions. Baseline changes require review.

Raw build output and JUnit reports are not uploaded or printed by the training
workflow because assertions can disclose exercise answers. CI uploads only counts,
commit identifiers, and a generic result. To diagnose a failure locally, run
`./gradlew clean build` on JDK 17. To run the same CI gate, use
`python3 .github/ci/check_training.py`.

This gate detects regressions relative to the baseline. It does not certify that
the exercises are complete or that all exercise tests pass.

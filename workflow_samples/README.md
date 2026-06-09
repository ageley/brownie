# Workflow samples

These are **not active** — GitHub App permissions forbid me from touching
`.github/workflows/`. Compare them against your real workflows and merge by hand.

| Sample        | Trigger                         | Image / tag                                                                 |
|---------------|---------------------------------|-----------------------------------------------------------------------------|
| `ci.yml`      | every PR commit                 | `ghcr.io/<owner>/brownie-snapshots:<version>-<run_number>` (e.g. `0.0.1-7`)  |
| `release.yml` | push to `main` (merged PR)      | `ghcr.io/<owner>/brownie-releases:<version>` + `:latest`, plus a `v<version>` git tag |
| `cleanup.yml` | weekly cron / manual            | prunes old `brownie-snapshots`, keeping the 10 most recent                  |

## Versioning

The artifact version now lives in `gradle.properties` (`version=0.0.1`, no
`-SNAPSHOT`). Snapshots append the GitHub Actions build counter
(`github.run_number`) so every PR commit gets a unique, increasing tag
(`0.0.1-1`, `0.0.1-2`, …). The release image and git tag use the bare version
(`0.0.1`). Bump the base version by editing the single `version=` line in
`gradle.properties`.

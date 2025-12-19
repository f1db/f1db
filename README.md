[![GitHub Release](https://img.shields.io/github/v/release/f1db/f1db?style=for-the-badge&labelColor=%23333&color=%23d40000&)](https://github.com/f1db/f1db/releases)
[![GitHub License](https://img.shields.io/github/license/f1db/f1db?style=for-the-badge&labelColor=%23333&color=%23d40000)](https://creativecommons.org/licenses/by/4.0/)
[![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/f1db/f1db/total?style=for-the-badge&labelColor=%23333&color=%23d40000)](https://github.com/f1db/f1db/releases)
[![GitHub Repo stars](https://img.shields.io/github/stars/f1db/f1db?style=for-the-badge&labelColor=%23333&color=%23d40000)](https://github.com/f1db/f1db/stargazers)


## F1DB

[F1DB] is the most comprehensive free open source database with all-time Formula 1 racing data and statistics.

Whether you are building a custom website, mobile application or just using F1DB to query data, here is what we provide:

- All drivers
- All constructors (including chassis)
- All engine manufacturers (including engines)
- All tyre manufacturers
- All circuits and location data
- All seasons from 1950 to present; including:
  - entrants
  - standings
- All races; including:
  - free practice and warming-up results
  - qualifying and pre-qualifying results
  - sprint race results
  - starting grid positions
  - race results
  - fastest laps
  - pit stops
  - driver of the day results
  - standings

New releases will be available as soon as possible after every race. See also [Versioning](#versioning).

Release artifacts are available in the following formats:

- CSV
- JSON
- JSON splitted (multiple data files)
- Smile (binary variant of the JSON format)
- SQL (MySQL, PostgreSQL or SQLite .sql dump files to create and populate a custom database)
- SQL with single inserts (much smaller .sql dump files with single insert per table; better performance but not supported by all platforms)
- SQLite database


## F1DB JSON Schema

The main [`f1db.schema.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/current/single/f1db.schema.json)
schema file contains all F1DB object definitions and serves as the main documentation.  

For the splitted distributions the [splitted](https://github.com/f1db/f1db/tree/main/src/schema/current/splitted)
schema files must be used.

Both the JSON and Smile artifacts validate against the F1DB Json Schema.

See the [Changelog](./CHANGELOG.md) for details about schema changes and version compatibility.


## F1DB SQL / SQLite Database

The SQLite database artifact contains all data in a relational database format
and can be used to directly query the data instead of processing the JSON format.

The database file could also serve students who want to learn SQL.

We suggest to use [SQLiteStudio] for querying the database file.
Of course any SQLite compliant SQL client can be used as well.

Since `v2024.3.0` SQL artifacts are available for MySQL, PostgreSQL and SQLite
that provide .sql dump files to import the schema and data directly in these database systems.

If you want to populate another database system (e.g. H2) then most likely the
PostgreSQL .sql dump file is your best option. 
If that doesn't work, and you want a .sql dump file for another database system to be added
to the release artifacts, then please create a [feature request](https://github.com/f1db/f1db/issues).


## Versioning

Releases are versioned using a "customized" [CalVer] versioning scheme:

    YYYY.RR.MICRO(.MODIFIER)

which uses the following conventions:

- **YYYY** – Full year of the season – `2026`, ..
- **RR** – Round number within the season – `0`, `1`, `2`, .., `10`, `11`, ..
- **MICRO** – Patch version – `0`, `1`, `2`, ..
- **MODIFIER** – An optional text tag like `dev`, `alpha`, `beta`, `rc`, ..

Note this is basically the `YYYY.MINOR.MICRO(.MODIFIER)` pattern where `MINOR` indicates the round number (`RR`).

Examples:

- **2026.0.0** – Pre-season release before the 1<sup>st</sup> race round of the 2026 season.
- **2026.0.1** – Pre-season patch release before the 1<sup>st</sup> race round of the 2026 season.
- **2026.0.1** – First pre-season patch release before the 1<sup>st</sup> race round of the 2026 season.
- **2026.1.0** – Final release after the 1<sup>st</sup> race round of the 2026 season.
- **2026.2.0** – Final release after the 2<sup>nd</sup> race round of the 2026 season.
- **2026.2.1** – First patch release after the 2<sup>nd</sup> race round of the 2026 season.
- **2026.2.2** – Second patch release after the 2<sup>nd</sup> race round of the 2026 season.

As you can see we use a special **RR** `0` for pre-season releases.


## Community

The F1DB community can be found on [GitHub Discussions](https://github.com/f1db/f1db/discussions), where you can ask and answer questions, voice ideas, and share your projects.

Keep in touch with us by following us on [X](https://twitter.com/f1db_com) or [Bluesky](https://bsky.app/profile/f1db.com).


## Reporting Data Issues

We use [GitHub Issues](https://github.com/f1db/f1db/issues) to track data issues or related problems.

If you found a data issue which is not reported yet, please [create a new issue](https://github.com/f1db/f1db/issues/new).

## Building from Source

To build the distribution zips simply run:

    ./gradlew clean build

or without running the tests (way faster):

    ./gradlew clean build -x test

The build requires Java 21, running the tests requires an environment with Docker.


## Releasing

[GitHub Actions](https://github.com/f1db/f1db/actions) is used to build and upload new releases by creating a new git tag.

Follow these steps:

1. Update the version number in `gradle.properties`
2. `git commit -am "Release v<version>"`
3. `git push`
4. `git tag -a v<version> -m "Release v<version>"`
3. `git push origin v<version>`

and the release action is automatically triggered by pushing the new tag.


## License

[![Creative Commons License][CC BY Icon Normal]][CC BY]

F1DB is licensed under a [Creative Commons Attribution 4.0 International License][CC BY].


[F1DB]: https://www.f1db.com
[CalVer]: https://calver.org/
[CC BY]: http://creativecommons.org/licenses/by/4.0/  
[CC BY Icon Compact]: https://i.creativecommons.org/l/by/4.0/80x15.png
[CC BY Icon Normal]: https://i.creativecommons.org/l/by/4.0/88x31.png
[CC BY Plaintext]: https://creativecommons.org/licenses/by-sa/4.0/legalcode.txt
[SQLiteStudio]: https://sqlitestudio.pl/

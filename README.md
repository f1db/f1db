## What is F1DB?

[F1DB] is the most comprehensive free open source database with all-time Formula 1 racing data and statistics.

Whether you are building a custom website, mobile application or just using F1DB to query data, here is what we provide:

- All drivers
- All constructors
- All engine manufacturers
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
- SQLite


## Notable Changes in `v2024.0.0.beta2`

Added date and time fields for all sessions in the 2024 season:

- `time` (of the race)
- `preQualifyingDate`, `preQualifyingTime`
- `freePractice1Date`, `freePractice1Time`, `freePractice2Date`, `freePractice2Time`, `freePractice3Date`, `freePractice3Time`, `freePractice4Date`, `freePractice4Time`
- `qualifying1Date`, `qualifying1Time`, `qualifying2Date`, `qualifying2Time`, `qualifyingDate`, `qualifyingTime`
- `sprintQualifyingDate`, `sprintQualifyingTime`
- `sprintRaceDate`, `sprintRaceTime`
- `warmingUpDate`, `warmingUpTime`

To accommodate this change the JSON Schema is bumped to version `v4.1.0`.
Note that currently the data is only provided for the 2024 season,
but we plan to provide the data for earlier seasons in the future.


## Notable Changes in `v2024.0.0.beta1`

After acquiring the [F1DB.com](https://www.f1db.com) domain the project has been rebranded back to F1DB.
Because this name change is reflected in the JSON Schema files the schema is bumped to version `v4.0.0`


## F1DB JSON Schema

The main [`f1db.schema.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/current/single/f1db.schema.json)
schema file contains all F1DB object definitions and serves as the main documentation.  

For the splitted distributions the [splitted](https://github.com/f1db/f1db/tree/main/src/schema/current/splitted)
schema files must be used.

Both the JSON and Smile artifacts validate against the F1DB Json Schema.

#### Schema version compatibility / history

| Version                 | Schema Version                                                                                                                                                                                                                                    |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `>=` `v2024.0.0.beta2`  | [`f1db.schema.json v4.1.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v4.1.0/single/f1db.schema.json), [`f1db-*.schema.json v4.1.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v4.1.0/splitted)                |
| `>=` `v2024.0.0.beta1`  | [`f1db.schema.json v4.0.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v4.0.0/single/f1db.schema.json), [`f1db-*.schema.json v4.0.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v4.0.0/splitted)                |
| `>=` `v2023.22.0`       | [`onlyf1-db.schema.json v3.2.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v3.2.0/single/onlyf1-db.schema.json), [`onlyf1-db-*.schema.json v3.2.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v3.2.0/splitted) |
| `>=` `v2023.8.0`        | [`onlyf1-db.schema.json v3.1.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v3.1.0/single/onlyf1-db.schema.json), [`onlyf1-db-*.schema.json v3.1.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v3.1.0/splitted) |
| `>=` `v2023.4.0`        | [`onlyf1-db.schema.json v3.0.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v3.0.0/single/onlyf1-db.schema.json), [`onlyf1-db-*.schema.json v3.0.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v3.0.0/splitted) |
| `>=` `v2023.0.0.beta1`  | [`onlyf1-db.schema.json v2.0.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v2.0.0/single/onlyf1-db.schema.json), [`onlyf1-db-*.schema.json v2.0.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v2.0.0/splitted) |
| `>=` `v2022.1.1`        | [`f1db-json-schema-v1.4.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.4.0.json)                                                                                                                   |
| `>=` `v2022.0.0`        | [`f1db-json-schema-v1.3.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.3.0.json)                                                                                                                   |
| `>=` `v2022.0.0.beta4`  | [`f1db-json-schema-v1.3.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.3.0.json)                                                                                                                   |
| `>=` `v2022.0.0.beta3`  | [`f1db-json-schema-v1.2.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.2.0.json)                                                                                                                   |
| `>=` `v2022.0.0.beta2`  | [`f1db-json-schema-v1.1.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.1.0.json)                                                                                                                   |
| `>=` `v2022.0.0.alpha1` | [`f1db-json-schema-v1.0.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v1.x/f1db-json-schema-v1.0.0.json)                                                                                                                   |


## F1DB SQLite Database

The SQLite database artifact contains all data in a relational database format
and can be used to directly query the data instead of processing the JSON format.

The database file could also serve students who want to learn SQL.

We suggest to use [SQLiteStudio] for querying the database file.
Of course any SQLite compliant SQL client can be used as well.  


## Versioning

Releases are versioned using a "customized" [CalVer] versioning scheme:

    YYYY.RR.MICRO(.MODIFIER)

which uses the following conventions:

- **YYYY** – Full year of the season – `2024`, ..
- **RR** – Round number within the season – `0`, `1`, `2`, .., `10`, `11`, ..
- **MICRO** – Patch version – `0`, `1`, `2`, ..
- **MODIFIER** – An optional text tag like `dev`, `alpha`, `beta`, `rc`, ..

Note this is basically the `YYYY.MINOR.MICRO(.MODIFIER)` pattern where `MINOR` indicates the round number (`RR`).

Examples:

- **2024.0.0.alpha** – Pre-season alpha release before the 1<sup>st</sup> race round of the 2024 season.
- **2024.0.0.beta** – Pre-season beta release before the 1<sup>st</sup> race round of the 2024 season.
- **2024.0.0** – Final pre-season release before the 1<sup>st</sup> race round of the 2024 season.
- **2024.0.1** – First pre-season patch release before the 1<sup>st</sup> race round of the 2024 season.
- **2024.1.0** – Final release after the 1<sup>st</sup> race round of the 2024 season.
- **2024.2.0** – Final release after the 2<sup>nd</sup> race round of the 2024 season.
- **2024.2.1** – First patch release after the 2<sup>nd</sup> race round of the 2024 season.
- **2024.2.2** – Second patch release after the 2<sup>nd</sup> race round of the 2024 season.

As you can see we use a special **RR** `0` for pre-season releases.


## Community

The F1DB community can be found on [GitHub Discussions](https://github.com/f1db/f1db/discussions), where you can ask and answer questions, voice ideas, and share your projects.

Keep in touch with us by following us on X — [@F1DBcom](https://twitter.com/f1dbcom).


## Reporting Data Issues

We use [GitHub Issues](https://github.com/f1db/f1db/issues) to track data issues or related problems.

If you found a data issue which is not reported yet, please [create a new issue](https://github.com/f1db/f1db/issues/new).

## Building from Source

To build the distribution zips simply run:

    ./gradlew clean dist assemble

Requires Java 17.


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

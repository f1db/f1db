<img src="./logo.png">

## What is F1DB?

F1DB is the most comprehensive free open source database with all-time Formula 1 racing data and statistics.

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

- JSON
- Smile (binary variant of the JSON format)
- SQLite


## F1DB JSON Schema

The [`f1db-json-schema-v1.3.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/f1db-json-schema-v1.3.0.json)
schema file contains all F1DB object definitions and serves as the main documentation.  

Both the JSON and Smile artifacts validate against the F1DB Json Schema.

#### Schema version compatibility / history

| Version                 | Schema Version                                                                                                             |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------|
| `>=` `v2022.0.0.beta4`  | [`f1db-json-schema-v1.3.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/f1db-json-schema-v1.3.0.json) |
| `>=` `v2022.0.0.beta3`  | [`f1db-json-schema-v1.2.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/f1db-json-schema-v1.2.0.json) |
| `>=` `v2022.0.0.beta2`  | [`f1db-json-schema-v1.1.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/f1db-json-schema-v1.1.0.json) |
| `>=` `v2022.0.0.alpha1` | [`f1db-json-schema-v1.0.0.json`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/f1db-json-schema-v1.0.0.json) |


## F1DB SQLite Database

The SQLite database artifact contains all data in a relational database format
and is very convenient for people with SQL knowledge that just want to query
the data instead of processing the JSON format. 
The database file could also serve students who want to learn SQL.

We suggest to use [SQLiteStudio] for querying the database file.
Of course any SQLite compliant SQL client can be used as well.  


## Versioning

Releases are versioned using a "customized" [CalVer] versioning scheme:

    YYYY.RR.MICRO(.MODIFIER)

which uses the following conventions:

- **YYYY** – Full year of the season – `2022`, `2023`, ..
- **RR** – Round number within the season – `0`, `1`, `2`, .., `10`, `11`, ..
- **MICRO** – Patch version – `0`, `1`, `2`, ..
- **MODIFIER** – An optional text tag like `dev`, `alpha`, `beta`, `rc`, ..

Note this is basically the `YYYY.MINOR.MICRO(.MODIFIER)` pattern where `MINOR` indicates the round number (`RR`).

Examples:

- **2022.0.0.alpha** – Pre-season alpha release before the 1<sup>st</sup> race round of the 2022 season.
- **2022.0.0.beta** – Pre-season beta release before the 1<sup>st</sup> race round of the 2022 season.
- **2022.0.0** – Final pre-season release before the 1<sup>st</sup> race round of the 2022 season.
- **2022.0.1** – First pre-season patch release before the 1<sup>st</sup> race round of the 2022 season.
- **2022.1.0** – Final release after the 1<sup>st</sup> race round of the 2022 season.
- **2022.2.0** – Final release after the 2<sup>nd</sup> race round of the 2022 season.
- **2022.2.1** – First patch release after the 2<sup>nd</sup> race round of the 2022 season.
- **2022.2.2** – Second patch release after the 2<sup>nd</sup> race round of the 2022 season.

As you can see we use a special **RR** `0` for pre-season releases.


## Community

The F1DB community can be found on [GitHub Discussions](https://github.com/f1db/f1db/discussions), where you can ask and answer questions, voice ideas, and share your projects.

Keep in touch with us by following us on Twitter — [@F1DBnet](https://twitter.com/f1dbnet).


## Reporting Data Issues

We use [GitHub Issues](https://github.com/f1db/f1db/issues) to track data issues or related problems.

If you found a data issue which is not reported yet, please [create a new issue](https://github.com/f1db/f1db/issues/new).

## Building from Source

To build the distribution zips simply run:

    ./gradlew clean dist assemble


## Releasing

[GitHub Actions](https://github.com/f1db/f1db/actions) is used to build and upload new releases by creating a new git tag.

Follow these steps:

1. Update the version number in `gradle.properties`
2. `git commit -am "Preparing release v<version>"`
3. `git push`
4. `git tag -a v<version> -m "Release v<version>"`
3. `git push origin v<version>`

and the release action is automatically triggered by pushing the new tag.


## License

[![Creative Commons License][CC BY Icon Normal]][CC BY]

F1DB is licensed under a [Creative Commons Attribution 4.0 International License][CC BY].


[CalVer]: https://calver.org/
[CC BY]: http://creativecommons.org/licenses/by/4.0/  
[CC BY Icon Compact]: https://i.creativecommons.org/l/by/4.0/80x15.png
[CC BY Icon Normal]: https://i.creativecommons.org/l/by/4.0/88x31.png
[CC BY Plaintext]: https://creativecommons.org/licenses/by-sa/4.0/legalcode.txt
[SQLiteStudio]: https://sqlitestudio.pl/

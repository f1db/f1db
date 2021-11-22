# F1DB

## What is F1DB?

F1DB – Formula 1 Database – is the most comprehensive free open source database with all-time Formula 1 Grand Prix racing data and statistics.

Whether you are building a custom website, mobile application or just using F1DB to query data, here is what we provide:

- All drivers
- All constructors
- All engine manufacturers
- All tyre manufacturers
- All circuits and location data
- All seasons from 1950 to present
- All standings
- All races; including:
  - free practice and warming-up results
  - qualifying and pre-qualifying results
  - sprint race results
  - starting grid positions
  - race results
  - fastest laps
  - pit stops
  - driver of the day results


New releases will be availble as soon as possible after every race. See also [Versioning](#versioning).


## F1DB JSON Schema

TODO.


## F1DB SQLite Database

TODO.


## Versioning

Releases are versioned using a customized [CalVer] versioning scheme:

    YYYY.RR.MICRO(.MODIFIER)

which uses the following conventions:

- **YYYY** – Full year of the season – `2022`, `2023`, ..
- **RR** – Round number within the season – `0`, `1`, `2`, .., `10`, `11`, ..
- **MICRO** – Patch version – `0`, `1`, `2`, ..
- **MODIFIER** – An optional text tag like `dev`, `alpha`, `beta`, `rc`, ..

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

TODO.


## License

[![Creative Commons License][CC BY Icon Normal]][CC BY]

F1DB is licensed under a [Creative Commons Attribution 4.0 International License][CC BY].


[CalVer]: https://calver.org/
[CC BY]: http://creativecommons.org/licenses/by/4.0/  
[CC BY Icon Compact]: https://i.creativecommons.org/l/by/4.0/80x15.png
[CC BY Icon Normal]: https://i.creativecommons.org/l/by/4.0/88x31.png
[CC BY Plaintext]: https://creativecommons.org/licenses/by-sa/4.0/legalcode.txt
[SQLiteStudio]: https://sqlitestudio.pl/

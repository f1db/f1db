## F1DB JSON Schema Changelog

### Version compatibility

| Version                 | Schema Version                                                                                                                                                                                                                                    |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `>=` `v2025.13.0`       | [`f1db.schema.json v6.2.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v6.2.0/single/f1db.schema.json), [`f1db-*.schema.json v6.2.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v6.2.0/splitted)                |
| `>=` `v2025.6.0`        | [`f1db.schema.json v6.1.1`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v6.1.1/single/f1db.schema.json), [`f1db-*.schema.json v6.1.1 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v6.1.1/splitted)                |
| `>=` `v2025.0.3`        | [`f1db.schema.json v6.1.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v6.1.0/single/f1db.schema.json), [`f1db-*.schema.json v6.1.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v6.1.0/splitted)                |
| `>=` `v2025.0.2`        | [`f1db.schema.json v6.0.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v6.0.0/single/f1db.schema.json), [`f1db-*.schema.json v6.0.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v6.0.0/splitted)                |
| `>=` `v2024.19.0`       | [`f1db.schema.json v5.0.0`](https://raw.githubusercontent.com/f1db/f1db/main/src/schema/v5.0.0/single/f1db.schema.json), [`f1db-*.schema.json v5.0.0 splitted`](https://github.com/f1db/f1db/tree/main/src/schema/v5.0.0/splitted)                |
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


### Changelog

### `v6.2.0`

- Added `iocCode` (International Olympic Committee code) property to `Country` object.
- Changed Czech Republic to Czechia (`czechia`).
- Changed Macedonia to North Macedonia (`north-macedonia`).
- Changed Swaziland to Eswatini (`eswatini`).


### `v6.1.1`

- Fixed type of `turns` property from `number` to `integer` in `Circuit` and `Race` objects.


### `v6.1.0`

- Added `polePosition` property to `RaceResult` object.


### `v6.0.0`

- Added `direction`, `length` and `turns` properties to `Circuit` object.
- Added `direction` and `turns` properties to `Race` object (`courseLength` already exists).
- Added `driversChampionshipDecider` and `constructorsChampionshipDecider` properties to `Race` object.


### `v5.0.0`

- Added chassis and engine data.
- Added qualification position details (`qualificationPositionNumber` and `qualificationPositionText`) to race results and starting grid positions.
- Added total points statistic (`totalPoints`) to constructors and engine manufacturers.
- Added extensive season statistics for drivers, constructors, engine manufacturers and tyre manufacturers.
- Renamed `ConstructorPreviousNextConstructor` to `ConstructorChronology`
- Added explicit `positionDisplayOrder` to `DriverFamilyRelationship`, `ConstructorChronology`, `PracticeResult`, `QualifyingResult`, `StartingGridPosition`, `RaceResult`, `FastestLap`, `PitStop`, `DriverOfTheDayResult`, `RaceDriverStanding`, `RaceConstructorStanding`, `SeasonDriverStanding` and `SeasonConstructorStanding`.

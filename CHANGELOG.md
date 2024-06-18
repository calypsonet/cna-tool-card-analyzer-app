# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.0.2] - 2024-06-18
### Fixed
- Accept cards returning a failing status word when getting non-mandatory Traceability Information tag.
### Upgraded
- Keyple Service Library `3.2.1` -> `3.2.2`

## [2.0.1] - 2024-05-14
### Fixed
- Hex/dec conversion issue in card check procedure.
### Upgraded
- Keypop Reader API `2.0.0` -> `2.0.1`
- Keypop Calypso Card API `2.0.0` -> `2.1.0`
- Keyple Common API `2.0.0` -> `2.0.1`
- Keyple Util Library `2.3.1` -> `2.4.0`
- Keyple Service Library `3.2.0` -> `3.2.1`
- Keyple Calypso Card Library `3.0.1` -> `3.1.1`
- Keyple Plugin PC/SC Library `2.2.0` -> `2.2.1`
- Gradle `6.8.3` -> `7.6.4`

## [2.0.0] - 2024-04-02
### Changed
- Clean up: Global code cleanup following Sonarlint recommendations to improve code quality and maintainability.
### Upgraded
- Calypsonet Terminal Reader API `1.3.+` -> Keypop Reader API `2.0.0`
- Calypsonet Terminal Calypso API `1.8.+` -> Keypop Calypso Card API `2.0.0`
- Keyple Service Resource Library `2.3.+` -> `3.2.0`
- Keyple Calypso Card Library `2.2.0` -> `3.0.1`
- Keyple Plugin PC/SC Library `2.1.+` -> `2.2.0`
- Keyple Util Library `2.+` -> `2.3.1` (source code not impacted)
### Added
- CI/CD: Added a GitHub action that builds and tests the code on every push to the repository.

[unreleased]: https://github.com/calypsonet/calypso-card-analyzer/compare/2.0.2...HEAD
[2.0.2]: https://github.com/calypsonet/calypso-card-analyzer/compare/2.0.1...2.0.2
[2.0.1]: https://github.com/calypsonet/calypso-card-analyzer/compare/2.0.1...2.0.0
[2.0.0]: https://github.com/calypsonet/calypso-card-analyzer/releases/tag/2.0.0
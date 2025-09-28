## Task 08: Refactor Garage Configuration to Adequately Handle New Data Structure

This task involves refactoring the application to correctly process and utilize the expanded data structure returned by the `LoadInitialGarageConfigurationUseCase`. The primary goals are to incorporate new fields for `Sector` and `ParkingSpot`, ensure all monetary values are handled with `BigDecimal` for precision, and adapt use cases to the updated data.

### Subtask 08.1: Update Data Models for Sector and ParkingSpot

This subtask focuses on modifying the domain models, DTOs, entities, and mappers to accommodate the new fields for `Sector` and `ParkingSpot`.

*   **08.1.1: Modify `Sector` domain model**
    *   Add `basePrice` (BigDecimal) to represent the base parking price for the sector.
    *   Add `openHour` (LocalTime) to define the opening time of the sector.
    *   Add `closeHour` (LocalTime) to define the closing time of the sector.
    *   Add `durationLimitMinutes` (Int) to specify the maximum parking duration.

*   **08.1.2: Modify `ParkingSpot` domain model**
    *   Add `latitude` (BigDecimal) to store the geographical latitude of the spot.
    *   Add `longitude` (BigDecimal) to store the geographical longitude of the spot.

*   **08.1.3: Update `SectorDTO`**
    *   Add `base_price` (Double, will be converted to BigDecimal in domain/entity).
    *   Add `open_hour` (String, will be converted to LocalTime).
    *   Add `close_hour` (String, will be converted to LocalTime).
    *   Add `duration_limit_minutes` (Int).

*   **08.1.4: Update `SpotDTO`**
    *   Add `lat` (Double, will be converted to BigDecimal).
    *   Add `lng` (Double, will be converted to BigDecimal).
    *   Add `occupied` (Boolean).

*   **08.1.5: Update `SectorEntity`**
    *   Add `basePrice` (BigDecimal).
    *   Add `openHour` (LocalTime).
    *   Add `closeHour` (LocalTime).
    *   Add `durationLimitMinutes` (Int).

*   **08.1.6: Update `ParkingSpotEntity`**
    *   Add `latitude` (BigDecimal).
    *   Add `longitude` (BigDecimal).

*   **08.1.7: Update `SectorMapper`**
    *   Modify `toEntity` and `toDomain` methods to map the new `basePrice`, `openHour`, `closeHour`, and `durationLimitMinutes` fields.
    *   Ensure correct type conversions (e.g., String to LocalTime, Double to BigDecimal).

*   **08.1.8: Update `ParkingSpotMapper`**
    *   Modify `toEntity` and `toDomain` methods to map the new `latitude` and `longitude` fields.
    *   Ensure correct type conversions (e.g., Double to BigDecimal).

### Subtask 08.2: Refactor `LoadInitialGarageConfigurationUseCase`

This subtask involves adjusting the logic within `LoadInitialGarageConfigurationUseCase` to correctly parse and persist the new data from the initial configuration call.

*   **08.2.1: Adjust parsing logic for sectors**
    *   Update the mapping from `SectorDTO` to `Sector` to include `basePrice`, `openHour`, `closeHour`, and `durationLimitMinutes`.
    *   Implement necessary type conversions (e.g., parsing `open_hour` and `close_hour` strings to `LocalTime`).

*   **08.2.2: Adjust parsing logic for spots**
    *   Update the mapping from `SpotDTO` to `ParkingSpot` to include `latitude` and `longitude`.
    *   Ensure `occupied` status from `SpotDTO` is correctly set in `ParkingSpot`.

*   **08.2.3: Ensure `BigDecimal` usage**
    *   Verify that `basePrice`, `latitude`, and `longitude` are handled as `BigDecimal` throughout the parsing and persistence process.

### Subtask 08.3: Refactor `HandleVehicleEntryUseCase`

This subtask focuses on adapting `HandleVehicleEntryUseCase` to the new data structure, particularly how parking spots are identified and how pricing is determined.

*   **08.3.1: Modify `execute` method signature**
    *   Change `execute(licensePlate: String, entryTime: LocalDateTime)` to `execute(licensePlate: String, entryTime: LocalDateTime, latitude: BigDecimal, longitude: BigDecimal)` to receive coordinates.

*   **08.3.2: Implement spot finding by coordinates**
    *   Remove the existing `garageRepository.findAvailableSpot()` call.
    *   Add a new method to `GarageRepositoryPort` (e.g., `findSpotByCoordinates(latitude: BigDecimal, longitude: BigDecimal)`) that returns a `ParkingSpot?`.
    *   Implement this new method in `GarageRepositoryGateway` to query `ParkingSpotEntity` based on `latitude` and `longitude`.
    *   Call this new method in `HandleVehicleEntryUseCase` to find the `availableSpot`.
    *   Handle cases where no spot is found at the given coordinates (e.g., throw `NoAvailableSpotException`).

*   **08.3.3: Update `ParkingSession` creation with `basePrice`**
    *   When creating a `ParkingSession`, use the `basePrice` from the `Sector` associated with the found `ParkingSpot` as the initial price component.

*   **08.3.4: Ensure `BigDecimal` for price calculations**
    *   Review all price-related calculations within this use case and ensure they use `BigDecimal` arithmetic for precision.

### Subtask 08.4: Ensure Monetary Precision with `BigDecimal`

This subtask is a cross-cutting concern to ensure all monetary values are represented and calculated using `BigDecimal` to avoid floating-point inaccuracies.

*   **08.4.1: Identify and change `Double` to `BigDecimal`**
    *   Review all domain models (`ParkingSession`, `Sector`), DTOs (`RevenueResponseDTO`, `SectorDTO`), and entities (`ParkingSessionEntity`, `SectorEntity`) for fields representing monetary values (e.g., `basePrice`, `finalCost`, `amount`, `dynamicPricePercentage` if it's a monetary adjustment) and change their type from `Double` to `BigDecimal`.

*   **08.4.2: Update all calculations**
    *   Modify any arithmetic operations involving these fields to use `BigDecimal` methods (e.g., `plus`, `minus`, `multiply`, `divide`).
    *   Pay attention to scaling and rounding modes for division operations.

### Subtask 08.5: Update Tests

This subtask involves updating existing unit and integration tests to reflect the changes in data models and use case logic.

*   **08.5.1: Update `LoadInitialGarageConfigurationUseCaseTest`**
    *   Adjust mock responses for `simulatorClient.getGarageConfiguration()` to return `GarageConfigDTO` with new fields.
    *   Update assertions to verify correct parsing and persistence of new sector and spot data.

*   **08.5.2: Update `HandleVehicleEntryUseCaseTest`**
    *   Modify test method signatures to pass `latitude` and `longitude`.
    *   Adjust mock responses for `garageRepository.findSpotByCoordinates()`.
    *   Update assertions related to `ParkingSession` creation and price calculations.

*   **08.5.3: Review and update other affected tests**
    *   Identify any other tests that might be impacted by changes to `Sector`, `ParkingSpot`, `ParkingSession` models, or `BigDecimal` conversions, and update them accordingly.


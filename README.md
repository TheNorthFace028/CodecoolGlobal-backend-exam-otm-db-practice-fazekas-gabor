Készítsünk egy programot amivel nyilván lehet tartani, hogy egy repülőtérre milyen repülők száltak le.

# Adatbázis

Az adatbázis egy táblából áll amelynek neve `airplanes_landed`. A tábla következő oszlopokal rendelkezik:

- captain VARCHAR(255)
- co_pilot VARCHAR(255)
- aircraft_type VARCHAR(255)
- date_of_landing DATE

Például:

| captain           | co_pilot            | aircraft_type         | date_of_landing |
|:------------------|:--------------------|:----------------------|:----------------|
| Orville Wright    | Kirby Chambliss     | Cessna 182            | 2021-07-10      |
| Wilbur Wright     | Paul Bonhomme       | Airbus A380           | 2020-11-11      |
| Peter Besenyei    | Martin Sonka        | Boeing 737-800        | 2021-01-26      |
| Martin Sonka      | Peter Besenyei      | Mooney M20V           | 2020-09-15      |
| Paul Bonhomme     | Wilbur Wright       | Lancair Barracuda     | 2021-02-19      |
| Kirby Chambliss   | Orville Wright      | Beechcraft Baron g58  | 2021-03-22      |

# Java alkalmazás

Az `AirportAdministrator` osztály konstruktora a következő paraméterekkel rendelkezik: 
- `String dbUrl` az url amin az adatbázis elérhető.
- `String dbUser` felhasználónév amivel csatlakozhatunk az adatbázishoz.
- `String dbPassword`  A `dbUser`-hez tartozó jelszó.

Készítsd el a `AirportAdministrator` osztály `getUniquePlanes` metódusát! Abban az esetben ha az adatbázis üres a metódus térjen vissza egy üres `String` listával. Egyéb esetben a a metódus térjen vissza egy `String` listában az összes a reptérre leszált repülő tipusának nevével ABC szerinti csökennő sorrendben. Egy repülőtípus csak egyszer szerepeljen függetlenül attol hányszor szált le az adott típusú repülőgép a reptérre.

A megoldáshoz használj `PreparedStatement`-et!

# Test-ek
```java
private static final List<String> EXPECTED_PLANES = List.of( "Mooney M20V", "Cessna 182", "Boeing 737-800", "Airbus A380");

    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private AirportAdministrator airportAdministrator;

    @BeforeEach
    void init() throws SQLException {
        airportAdministrator = new AirportAdministrator(DB_URL, DB_USER, DB_PASSWORD);
        createTable();
    }

    @AfterEach
    void destruct() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            String dropTable = "DROP TABLE IF EXISTS airplanes_landed";
            Statement statement = connection.createStatement();
            statement.execute(dropTable);
        }
    }


    private void createTable() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String createTable = "CREATE TABLE IF NOT EXISTS airplanes_landed (" +
                    "captain VARCHAR(255), " +
                    "co_pilot VARCHAR(255), " +
                    "aircraft_type VARCHAR(255), " +
                    "date_of_landing DATE" +
                    ");";
            Statement statement = connection.createStatement();
            statement.execute(createTable);
        }
    }

    @Test
    void test_getUniqueAirplanes_allAirplanesOnce_anyOrder() throws SQLException {
        insertMultipleLanding();
        List<String> actualPlanes = airportAdministrator.getUniquePlanes();

        assertEquals(EXPECTED_PLANES.size(), actualPlanes.size());
        for (String expectedPlane : EXPECTED_PLANES) {
            assertTrue(actualPlanes.contains(expectedPlane));
        }
    }

    @Test
    void test_getUniquePlanes_allPlanesOnce_alphabeticOrder() throws SQLException {
        insertMultipleLanding();
        List<String> actualPlanes = airportAdministrator.getUniquePlanes();
        assertEquals(EXPECTED_PLANES,actualPlanes);
    }

    @Test
    void test_geUniqueBirds_oneDuplicate_anyOrder() throws SQLException {
        insertMultipleLanding();
        insertDuplicateAirplane();
        List<String> actualPlanes = airportAdministrator.getUniquePlanes();
        assertEquals(EXPECTED_PLANES.size(),actualPlanes.size());
        for (String plane : EXPECTED_PLANES) {
            assertTrue(actualPlanes.contains(plane));
        }
    }

    @Test
    void test_getUniquePlanes_emptyDatabase() {
        assertEquals(List.of(),airportAdministrator.getUniquePlanes());
    }

    private void insertMultipleLanding() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertAirplanes = "INSERT INTO airplanes_landed (captain, co_pilot, aircraft_type, date_of_landing) VALUES " +
                    "('Orville Wright', 'Kirby Chambliss', 'Cessna 182', '2021-07-10'), " +
                    "('Wilbur Wright', 'Paul Bonhomme', 'Airbus A380', '2020-11-11'), " +
                    "('Peter Besenyei', 'Martin Sonka', 'Boeing 737-800', '2021-01-26'), " +
                    "('Martin Sonka', 'Peter Besenyei', 'Mooney M20V', '2020-09-15');";
            Statement statement = connection.createStatement();
            statement.execute(insertAirplanes);
        }
    }

    private void insertDuplicateAirplane() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insertDuplicate = "INSERT INTO airplanes_landed (captain, co_pilot, aircraft_type, date_of_landing) VALUES " +
                    "('Martin Sonka', 'Peter Besenyei', 'Mooney M20V', '2020-09-15');";
            Statement statement = connection.createStatement();
            statement.execute(insertDuplicate);
        }
    }
```


DROP TABBLE IF EXISTS airplanes_landed CASCADE;
CREATE TABLE airplanes_landed
(
    captain varchar(255),
    co_pilot varchar(255),
    aircraft_type varchar(255),
    date_of_landing DATE
)
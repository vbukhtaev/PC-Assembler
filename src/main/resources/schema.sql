CREATE TABLE IF NOT EXISTS manufacturer
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS motherboard_form_factor
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS fan_power_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS cpu_power_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS graphics_card_power_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS main_power_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS storage_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS storage_power_connector
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS pci_express_connector_version
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS vendor
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS socket
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS ram_type
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS video_memory_type
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS psu_form_factor
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS psu_certificate
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE NOT NULL,
    created_at  TIMESTAMP          NOT NULL,
    modified_at TIMESTAMP          NOT NULL
);

CREATE TABLE IF NOT EXISTS fan_size
(
    id          VARCHAR(36) PRIMARY KEY,
    length      INTEGER   NOT NULL CHECK ( length > 0 ),
    width       INTEGER   NOT NULL CHECK ( width > 0 ),
    height      INTEGER   NOT NULL CHECK ( height > 0 ),
    created_at  TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    UNIQUE (length, width, height)
);

CREATE TABLE IF NOT EXISTS expansion_bay
(
    id          VARCHAR(36) PRIMARY KEY,
    size        NUMERIC   NOT NULL CHECK ( size >= 1 ),
    created_at  TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    UNIQUE (size)
);

CREATE TABLE IF NOT EXISTS ssd
(
    id                 VARCHAR(36) PRIMARY KEY,
    name               VARCHAR(64) UNIQUE                            NOT NULL,
    capacity           INTEGER                                       NOT NULL CHECK ( capacity >= 120 ),
    reading_speed      INTEGER                                       NOT NULL CHECK ( reading_speed > 0 ),
    writing_speed      INTEGER                                       NOT NULL CHECK ( writing_speed > 0 ),
    created_at         TIMESTAMP                                     NOT NULL,
    modified_at        TIMESTAMP                                     NOT NULL,
    vendor_id          VARCHAR(36) REFERENCES vendor (id)            NOT NULL,
    connector_id       VARCHAR(36) REFERENCES storage_connector (id) NOT NULL,
    power_connector_id VARCHAR(36) REFERENCES storage_power_connector (id),
    UNIQUE (name, capacity)
);

CREATE TABLE IF NOT EXISTS hdd
(
    id                 VARCHAR(36) PRIMARY KEY,
    name               VARCHAR(64) UNIQUE                            NOT NULL,
    capacity           INTEGER                                       NOT NULL CHECK ( capacity >= 120 ),
    reading_speed      INTEGER                                       NOT NULL CHECK ( reading_speed > 0 ),
    writing_speed      INTEGER                                       NOT NULL CHECK ( writing_speed > 0 ),
    spindle_speed      INTEGER                                       NOT NULL CHECK ( spindle_speed >= 5400 ),
    cache_size         INTEGER                                       NOT NULL CHECK ( cache_size >= 32 ),
    created_at         TIMESTAMP                                     NOT NULL,
    modified_at        TIMESTAMP                                     NOT NULL,
    vendor_id          VARCHAR(36) REFERENCES vendor (id)            NOT NULL,
    connector_id       VARCHAR(36) REFERENCES storage_connector (id) NOT NULL,
    power_connector_id VARCHAR(36) REFERENCES storage_power_connector (id),
    UNIQUE (name, capacity)
);

CREATE TABLE IF NOT EXISTS cooler
(
    id                 VARCHAR(36) PRIMARY KEY,
    name               VARCHAR(64) UNIQUE                              NOT NULL,
    power_dissipation  INTEGER                                         NOT NULL CHECK ( power_dissipation > 0 ),
    height             INTEGER                                         NOT NULL CHECK ( height > 0 ),
    created_at         TIMESTAMP                                       NOT NULL,
    modified_at        TIMESTAMP                                       NOT NULL,
    vendor_id          VARCHAR(36) REFERENCES vendor (id)              NOT NULL,
    fan_size_id        VARCHAR(36) REFERENCES fan_size (id)            NOT NULL,
    power_connector_id VARCHAR(36) REFERENCES fan_power_connector (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS fan
(
    id                 VARCHAR(36) PRIMARY KEY,
    name               VARCHAR(64) UNIQUE                              NOT NULL,
    created_at         TIMESTAMP                                       NOT NULL,
    modified_at        TIMESTAMP                                       NOT NULL,
    vendor_id          VARCHAR(36) REFERENCES vendor (id)              NOT NULL,
    size_id            VARCHAR(36) REFERENCES fan_size (id)            NOT NULL,
    power_connector_id VARCHAR(36) REFERENCES fan_power_connector (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS gpu
(
    id                VARCHAR(36) PRIMARY KEY,
    name              VARCHAR(64)                                   NOT NULL,
    memory_size       INTEGER                                       NOT NULL CHECK ( memory_size >= 64 ),
    power_consumption INTEGER                                       NOT NULL CHECK ( power_consumption >= 0 ),
    created_at        TIMESTAMP                                     NOT NULL,
    modified_at       TIMESTAMP                                     NOT NULL,
    memory_type_id    VARCHAR(36) REFERENCES video_memory_type (id) NOT NULL,
    manufacturer_id   VARCHAR(36) REFERENCES manufacturer (id)      NOT NULL,
    UNIQUE (name, memory_size)
);

CREATE TABLE IF NOT EXISTS cpu
(
    id              VARCHAR(36) PRIMARY KEY,
    name            VARCHAR(64) UNIQUE                       NOT NULL,
    core_count      INTEGER                                  NOT NULL CHECK ( core_count > 0 ),
    thread_count    INTEGER                                  NOT NULL CHECK ( thread_count > 0 ),
    base_clock      INTEGER                                  NOT NULL CHECK ( base_clock >= 100 ),
    max_clock       INTEGER                                  NOT NULL CHECK ( max_clock >= 100 ),
    l3cache_size    INTEGER                                  NOT NULL CHECK ( l3cache_size >= 0 ),
    max_tdp         INTEGER                                  NOT NULL CHECK ( max_tdp > 0 ),
    created_at      TIMESTAMP                                NOT NULL,
    modified_at     TIMESTAMP                                NOT NULL,
    socket_id       VARCHAR(36) REFERENCES socket (id)       NOT NULL,
    manufacturer_id VARCHAR(36) REFERENCES manufacturer (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS psu
(
    id                      VARCHAR(36) PRIMARY KEY,
    name                    VARCHAR(64) UNIQUE                               NOT NULL,
    power                   INTEGER                                          NOT NULL CHECK ( power > 0 ),
    power_12v               INTEGER                                          NOT NULL CHECK ( power_12v > 0 AND power_12v <= psu.power),
    length                  INTEGER                                          NOT NULL CHECK ( length > 0 ),
    created_at              TIMESTAMP                                        NOT NULL,
    modified_at             TIMESTAMP                                        NOT NULL,
    vendor_id               VARCHAR(36) REFERENCES vendor (id)               NOT NULL,
    form_factor_id          VARCHAR(36) REFERENCES psu_form_factor (id)      NOT NULL,
    certificate_id          VARCHAR(36) REFERENCES psu_certificate (id)      NOT NULL,
    main_power_connector_id VARCHAR(36) REFERENCES main_power_connector (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS design
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE                 NOT NULL,
    created_at  TIMESTAMP                          NOT NULL,
    modified_at TIMESTAMP                          NOT NULL,
    vendor_id   VARCHAR(36) REFERENCES vendor (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS chipset
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(64) UNIQUE                 NOT NULL,
    created_at  TIMESTAMP                          NOT NULL,
    modified_at TIMESTAMP                          NOT NULL,
    socket_id   VARCHAR(36) REFERENCES socket (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS ram_module
(
    id          VARCHAR(36) PRIMARY KEY,
    clock       INTEGER                              NOT NULL CHECK ( clock >= 1333 ),
    capacity    INTEGER                              NOT NULL CHECK ( capacity >= 512 ),
    created_at  TIMESTAMP                            NOT NULL,
    modified_at TIMESTAMP                            NOT NULL,
    type_id     VARCHAR(36) REFERENCES ram_type (id) NOT NULL,
    vendor_id   VARCHAR(36) REFERENCES vendor (id)   NOT NULL,
    design_id   VARCHAR(36) REFERENCES design (id)   NOT NULL
);


CREATE TABLE IF NOT EXISTS graphics_card
(
    id                               VARCHAR(36) PRIMARY KEY,
    length                           INTEGER                                                   NOT NULL CHECK ( length >= 40 ),
    created_at                       TIMESTAMP                                                 NOT NULL,
    modified_at                      TIMESTAMP                                                 NOT NULL,
    gpu_id                           VARCHAR(36) REFERENCES gpu (id)                           NOT NULL,
    design_id                        VARCHAR(36) REFERENCES design (id)                        NOT NULL,
    power_connector_id               VARCHAR(36) REFERENCES graphics_card_power_connector (id) NOT NULL,
    pci_express_connector_version_id VARCHAR(36) REFERENCES pci_express_connector_version (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS motherboard
(
    id                               VARCHAR(36) PRIMARY KEY,
    max_memory_clock                 INTEGER                                                   NOT NULL CHECK ( max_memory_clock >= 1333 ),
    max_memory_over_clock            INTEGER                                                   NOT NULL CHECK ( max_memory_over_clock >= 1333 ),
    memory_slots_count               INTEGER                                                   NOT NULL CHECK ( memory_slots_count > 0 ),
    created_at                       TIMESTAMP                                                 NOT NULL,
    modified_at                      TIMESTAMP                                                 NOT NULL,
    design_id                        VARCHAR(36) REFERENCES design (id)                        NOT NULL,
    chipset_id                       VARCHAR(36) REFERENCES chipset (id)                       NOT NULL,
    ram_type_id                      VARCHAR(36) REFERENCES ram_type (id)                      NOT NULL,
    form_factor_id                   VARCHAR(36) REFERENCES motherboard_form_factor (id)       NOT NULL,
    cpu_power_connector_id           VARCHAR(36) REFERENCES cpu_power_connector (id)           NOT NULL,
    fan_power_connector_id           VARCHAR(36) REFERENCES fan_power_connector (id)           NOT NULL,
    main_power_connector_id          VARCHAR(36) REFERENCES main_power_connector (id)          NOT NULL,
    pci_express_connector_version_id VARCHAR(36) REFERENCES pci_express_connector_version (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS computer_case
(
    id                       VARCHAR(36) PRIMARY KEY,
    name                     VARCHAR(64) UNIQUE                 NOT NULL,
    psu_max_length           INTEGER                            NOT NULL CHECK ( psu_max_length > 0 ),
    graphics_card_max_length INTEGER                            NOT NULL CHECK ( graphics_card_max_length > 0),
    cooler_max_height        INTEGER                            NOT NULL CHECK ( cooler_max_height > 0 ),
    created_at               TIMESTAMP                          NOT NULL,
    modified_at              TIMESTAMP                          NOT NULL,
    vendor_id                VARCHAR(36) REFERENCES vendor (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS computer_build
(
    id               VARCHAR(36) PRIMARY KEY,
    created_at       TIMESTAMP                                 NOT NULL,
    modified_at      TIMESTAMP                                 NOT NULL,
    name             VARCHAR(64) UNIQUE                        NOT NULL,
    cpu_id           VARCHAR(36) REFERENCES cpu (id)           NOT NULL,
    psu_id           VARCHAR(36) REFERENCES psu (id)           NOT NULL,
    cooler_id        VARCHAR(36) REFERENCES cooler (id)        NOT NULL,
    motherboard_id   VARCHAR(36) REFERENCES motherboard (id)   NOT NULL,
    graphics_card_id VARCHAR(36) REFERENCES graphics_card (id) NOT NULL,
    computer_case_id VARCHAR(36) REFERENCES computer_case (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS cooler_to_socket
(
    cooler_id VARCHAR(36) REFERENCES cooler (id) NOT NULL,
    socket_id VARCHAR(36) REFERENCES socket (id) NOT NULL,
    PRIMARY KEY (cooler_id, socket_id)
);

CREATE TABLE IF NOT EXISTS cpu_to_ram_type
(
    id               VARCHAR(36) PRIMARY KEY,
    cpu_id           VARCHAR(36) REFERENCES cpu (id)      NOT NULL,
    ram_type_id      VARCHAR(36) REFERENCES ram_type (id) NOT NULL,
    max_memory_clock INTEGER                              NOT NULL CHECK ( max_memory_clock >= 1333 ),
    UNIQUE (cpu_id, ram_type_id)
);

CREATE TABLE IF NOT EXISTS motherboard_to_storage_connector
(
    id                   VARCHAR(36) PRIMARY KEY,
    motherboard_id       VARCHAR(36) REFERENCES motherboard (id)       NOT NULL,
    storage_connector_id VARCHAR(36) REFERENCES storage_connector (id) NOT NULL,
    amount               INTEGER                                       NOT NULL CHECK ( amount > 0 ),
    UNIQUE (motherboard_id, storage_connector_id)
);

CREATE TABLE IF NOT EXISTS psu_to_cpu_power_connector
(
    id                     VARCHAR(36) PRIMARY KEY,
    psu_id                 VARCHAR(36) REFERENCES psu (id)                 NOT NULL,
    cpu_power_connector_id VARCHAR(36) REFERENCES cpu_power_connector (id) NOT NULL,
    amount                 INTEGER                                         NOT NULL CHECK ( amount > 0 ),
    UNIQUE (psu_id, cpu_power_connector_id)
);

CREATE TABLE IF NOT EXISTS psu_to_graphics_card_power_connector
(
    id                               VARCHAR(36) PRIMARY KEY,
    psu_id                           VARCHAR(36) REFERENCES psu (id)                           NOT NULL,
    graphics_card_power_connector_id VARCHAR(36) REFERENCES graphics_card_power_connector (id) NOT NULL,
    amount                           INTEGER                                                   NOT NULL CHECK ( amount > 0 ),
    UNIQUE (psu_id, graphics_card_power_connector_id)
);

CREATE TABLE IF NOT EXISTS psu_to_storage_power_connector
(
    id                         VARCHAR(36) PRIMARY KEY,
    psu_id                     VARCHAR(36) REFERENCES psu (id)                     NOT NULL,
    storage_power_connector_id VARCHAR(36) REFERENCES storage_power_connector (id) NOT NULL,
    amount                     INTEGER                                             NOT NULL CHECK ( amount > 0 ),
    UNIQUE (psu_id, storage_power_connector_id)
);

CREATE TABLE IF NOT EXISTS computer_case_to_fan_size
(
    id               VARCHAR(36) PRIMARY KEY,
    computer_case_id VARCHAR(36) REFERENCES computer_case (id) NOT NULL,
    fan_size_id      VARCHAR(36) REFERENCES fan_size (id)      NOT NULL,
    amount           INTEGER                                   NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_case_id, fan_size_id)
);

CREATE TABLE IF NOT EXISTS computer_case_to_expansion_bay
(
    id               VARCHAR(36) PRIMARY KEY,
    computer_case_id VARCHAR(36) REFERENCES computer_case (id) NOT NULL,
    expansion_bay_id VARCHAR(36) REFERENCES expansion_bay (id) NOT NULL,
    amount           INTEGER                                   NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_case_id, expansion_bay_id)
);

CREATE TABLE IF NOT EXISTS computer_case_to_motherboard_form_factor
(
    computer_case_id           VARCHAR(36) REFERENCES computer_case (id)           NOT NULL,
    motherboard_form_factor_id VARCHAR(36) REFERENCES motherboard_form_factor (id) NOT NULL,
    PRIMARY KEY (computer_case_id, motherboard_form_factor_id)
);

CREATE TABLE IF NOT EXISTS computer_case_to_psu_form_factor
(
    computer_case_id   VARCHAR(36) REFERENCES computer_case (id)   NOT NULL,
    psu_form_factor_id VARCHAR(36) REFERENCES psu_form_factor (id) NOT NULL,
    PRIMARY KEY (computer_case_id, psu_form_factor_id)
);

CREATE TABLE IF NOT EXISTS computer_to_ram_module
(
    id            VARCHAR(36) PRIMARY KEY,
    computer_id   VARCHAR(36) REFERENCES computer_build (id) NOT NULL,
    ram_module_id VARCHAR(36) REFERENCES ram_module (id)     NOT NULL,
    amount        INTEGER                                    NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_id, ram_module_id)
);

CREATE TABLE IF NOT EXISTS computer_to_fan
(
    id          VARCHAR(36) PRIMARY KEY,
    computer_id VARCHAR(36) REFERENCES computer_build (id) NOT NULL,
    fan_id      VARCHAR(36) REFERENCES fan (id)            NOT NULL,
    amount      INTEGER                                    NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_id, fan_id)
);

CREATE TABLE IF NOT EXISTS computer_to_hdd
(
    id          VARCHAR(36) PRIMARY KEY,
    computer_id VARCHAR(36) REFERENCES computer_build (id) NOT NULL,
    hdd_id      VARCHAR(36) REFERENCES hdd (id)            NOT NULL,
    amount      INTEGER                                    NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_id, hdd_id)
);

CREATE TABLE IF NOT EXISTS computer_to_ssd
(
    id          VARCHAR(36) PRIMARY KEY,
    computer_id VARCHAR(36) REFERENCES computer_build (id) NOT NULL,
    ssd_id      VARCHAR(36) REFERENCES ssd (id)            NOT NULL,
    amount      INTEGER                                    NOT NULL CHECK ( amount > 0 ),
    UNIQUE (computer_id, ssd_id)
);
CREATE TYPE unit AS ENUM('dag','g','glass','kg','l','ml','spoon','teaspoon','pieces');

CREATE TABLE IF NOT EXISTS recipe
(
    id	UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    ingredients	TEXT NOT NULL,
    instructions TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    number_of_servings	INT
);
CREATE TABLE IF NOT EXISTS ingredients
(
    id	UUID 	PRIMARY KEY,
    recipe_id UUID NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    value		DOUBLE PRECISION NOT NULL,
    unit		unit NOT NULL,
    name		VARCHAR(255)	NOT NULL
);



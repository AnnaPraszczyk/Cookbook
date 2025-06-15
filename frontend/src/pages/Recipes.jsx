import React from "react";
import { Link } from "react-router-dom";


const Recipes = () => {
        const containerStyle = {
            display: "flex",
            flexDirection: "row",
            alignItems: "center",
            padding: "20px",
            gap: "20px",
            fontSize: "1.5rem",
            borderRadius: "8px",
        };

    return (
        <>
        <h1>Recipes Management</h1>
        <div style={containerStyle}>

            <Link to="/recipes/create">
                Create Recipe
            </Link>
            <Link to="/recipes/update">
                Update Recipe
            </Link>
            <Link to="/recipes/delete">
                Delete Recipe
            </Link>
        </div>
    </>
    );
};

export default Recipes;
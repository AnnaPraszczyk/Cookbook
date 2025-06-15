// UpdateRecipePage.jsx
import React from "react";
import RecipeUpdateForm from "../components/RecipeUpdateForm";

const UpdateRecipePage = () => {
    return (
        <div style={{ padding: "20px", marginTop: "100px" }}>
            <h1>Update Recipe</h1>
            <RecipeUpdateForm />
        </div>
    );
};

export default UpdateRecipePage;
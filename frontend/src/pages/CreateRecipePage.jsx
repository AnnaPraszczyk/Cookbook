// CreateRecipePage.jsx
import React from "react";
import RecipeCreateForm from "../components/RecipeCreateForm.jsx";

const CreateRecipePage = () => {
    return (
        <div style={{ padding: "20px", marginTop: "100px" }}>
            <h1>Create New Recipe</h1>
            <RecipeCreateForm />
        </div>
    );
};

export default CreateRecipePage;
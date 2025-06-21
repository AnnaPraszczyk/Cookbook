// CreateRecipePage.jsx
import React from "react";
import RecipeCreateForm from "../components/RecipeCreateForm.jsx";

const CreateRecipePage = () => {
    return (
        <div className="p-6 max-w-3xl mx-auto">
            <h1 className="text-4xl font-bold mb-6">Create New Recipe</h1>
            <RecipeCreateForm />
        </div>
    );
};

export default CreateRecipePage;
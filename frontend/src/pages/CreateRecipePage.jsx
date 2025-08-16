// CreateRecipePage.jsx
import React from "react";
import RecipeCreateForm from "../components/RecipeCreateForm.jsx";

const CreateRecipePage = () => {
    return (
        <div className="p-6 max-w-3xl mx-auto sm:px-6 md:px-8 lg:px-10 xl:px-12 mt-6">
            <h1 className="text-4xl sm:text-3xl font-bold mb-6">Create New Recipe</h1>
            <RecipeCreateForm />
        </div>
    );
};
export default CreateRecipePage;
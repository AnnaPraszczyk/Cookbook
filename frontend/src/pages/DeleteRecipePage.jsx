// DeleteRecipePage.jsx
import React from "react";
import RecipeDeleteForm from "../components/RecipeDeleteForm";

const DeleteRecipePage = () => {
    return (
        <div className="px-4 mt-6 sm:px-6 lg:px-8 py-6 max-w-4xl mx-auto">
            <h1>Delete Recipe</h1>
            <RecipeDeleteForm />
        </div>
    );
};
export default DeleteRecipePage;
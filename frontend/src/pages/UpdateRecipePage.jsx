// UpdateRecipePage.jsx
import React from "react";
import RecipeUpdateForm from "../components/RecipeUpdateForm";

const UpdateRecipePage = () => {
    return (
        <div className={"mt-6 p-6"}>
            <h1>Update Recipe</h1>
            <RecipeUpdateForm />
        </div>
    );
};
export default UpdateRecipePage;
// DeleteRecipePage.jsx
import React from "react";
import RecipeDeleteForm from "../components/RecipeDeleteForm";

const DeleteRecipePage = () => {
    return (
        <div style={{ padding: "20px"}}>
            <h1>Delete Recipe</h1>
            <RecipeDeleteForm />
        </div>
    );
};

export default DeleteRecipePage;
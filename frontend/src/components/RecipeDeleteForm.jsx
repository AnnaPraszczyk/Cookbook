// RecipeDeleteForm.jsx
import React, { useState } from "react";

const RecipeDeleteForm = () => {
    const [recipeId, setRecipeId] = useState("");
    const [confirmation, setConfirmation] = useState("");
    const [message, setMessage] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const requestData = {
            confirmation: confirmation,
        };

        try {
            await fetch(`/recipes/${recipeId}`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestData),
            });
            setMessage("Recipe deleted successfully.");
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <form
            onSubmit={handleSubmit}
            style={{
                display: "flex",
                flexDirection: "column",
                gap: "10px",
                maxWidth: "400px",
                margin: "20px auto",
            }}
        >
            <label>
                <input
                    type="text"
                    placeholder="Recipe ID"
                    value={recipeId}
                    onChange={(e) => setRecipeId(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px" }}
                />
            </label>
            <label>
                <input
                    type="text"
                    placeholder="Confirmation"
                    value={confirmation}
                    onChange={(e) => setConfirmation(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px" }}
                />
            </label>
            <button
                type="submit"
                style={{ padding: "8px 16px", fontSize: "16px", cursor: "pointer" }}
            >
                Delete Recipe
            </button>
            {message && <p>{message}</p>}
        </form>
    );
};

export default RecipeDeleteForm;
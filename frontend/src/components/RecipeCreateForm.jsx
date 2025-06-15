import React, { useState } from "react";

const categoryOptions = [
    "Appetizer","Soup","Main Course","Sauce","Salad","Pasta","Snack","Beverage","Dessert","Cake","Pie","Bakery"
];


const RecipeCreateForm = () => {
    const [recipeName, setRecipeName] = useState("");
    const [category, setCategory] = useState(categoryOptions[0]);
    const [ingredients, setIngredients] = useState("");
    const [instructions, setInstructions] = useState("");
    const [numberOfServings, setNumberOfServings] = useState("");
    const [tags, setTags] = useState("");
    const [message, setMessage] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const ingredientsArray = ingredients.split(",").map((item) => {
            const trimmed = item.trim();
            return { productName: { name: trimmed }, amount: 1, unit: "g" };
        });

        const tagsArray = tags.split(",").map((item) => item.trim()).filter((item) => item);

        const requestData = {
            recipeName,
            category,
            ingredients: ingredientsArray,
            instructions,
            numberOfServings: parseInt(numberOfServings, 10),
            tags: tagsArray,
        };

        try {
            const response = await fetch("/recipes", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestData),
            });
            const data = await response.json();
            setMessage(`Recipe created! Received: ${JSON.stringify(data)}`);
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px", maxWidth: "500px", margin: "20px auto"}}>
            <label>
                <input
                    type="text"
                    placeholder="Recipe Name"
                    value={recipeName}
                    onChange={(e) => setRecipeName(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px" }}
                />
            </label>

            <label>
                <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px", backgroundColor: "#333", marginRight: "10px", width: "430px",color: "gray", border: "2px solid gray",
                        borderRadius: "5px" }}
                >
                {categoryOptions.map((option) => (
                    <option key={option} value={option}>
                        {option}
                    </option>
                ))}
            </select>
        </label>

            <label>
                <textarea
                    value={ingredients}
                    onChange={(e) => setIngredients(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px", backgroundColor: "#333", width: "415px",marginRight: "10px", color: "gray", border: "2px solid gray",
                        borderRadius: "5px" }}
                    placeholder="Enter ingredient names separated by commas"
                />
            </label>

            <label>
                <textarea
                    placeholder="Instructions"
                    value={instructions}
                    onChange={(e) => setInstructions(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px",backgroundColor: "#333", marginRight: "10px", width: "415px", border: "2px solid gray",
                        borderRadius: "5px"  }}
                />
            </label>

            <label>
                <input
                    type="number"
                    placeholder="Number of Servings"
                    value={numberOfServings}
                    onChange={(e) => setNumberOfServings(e.target.value)}
                    required
                    style={{ padding: "8px", fontSize: "16px", backgroundColor: "#333", color: "gray"}}
                />
            </label>

            <label>
                <input
                    type="text"
                    placeholder="Tags (comma separated)"
                    value={tags}
                    onChange={(e) => setTags(e.target.value)}
                    style={{ padding: "8px", fontSize: "16px" }}
                />
            </label>

            <button type="submit">Create Recipe</button>

            {message && <p>{message}</p>}
        </form>
    );
};

export default RecipeCreateForm;
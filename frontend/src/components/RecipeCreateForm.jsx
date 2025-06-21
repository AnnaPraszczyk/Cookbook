import React, { useState } from "react";
import IngredientInput from "./IngredientInput";
import { HiX } from "react-icons/hi";
import { useNavigate } from "react-router-dom";

const categoryOptions = [
    "Appetizer","Soup","Main Course","Sauce","Salad","Pasta","Snack","Beverage","Dessert","Cake","Pie","Bakery"
];

const RecipeCreateForm = () => {
    const [recipeName, setRecipeName] = useState("");
    const [category, setCategory] = useState(categoryOptions[0]);
    const [ingredients, setIngredients] = useState([]);
    const [instructions, setInstructions] = useState("");
    const [numberOfServings, setNumberOfServings] = useState("");
    const [tags, setTags] = useState("");
    const [message, setMessage] = useState(null);

    const navigate = useNavigate();

    const handleAddIngredient = ing =>
        setIngredients(prev => [...prev, ing]);

    const handleRemoveIngredientAt = (indexToRemove) => {
        setIngredients((prev) =>
            prev.filter((_, index) => index !== indexToRemove)
        );
    };

    const handleRecipeSubmit = async (e) => {
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
            const response = await fetch("/api/recipes", {
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
    const messageClass =
        message?.type === "success"
            ? "mt-4 text-green-600"
            : "mt-4 text-red-600";


    return (
        <form onSubmit={handleRecipeSubmit} className="flex flex-col max-w-xl mx-auto space-y-6">
            <div>
                <input
                    type="text"
                    placeholder="Recipe Name"
                    value={recipeName}
                    onChange={(e) => setRecipeName(e.target.value)}
                    required
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-whiteborder-gray-400 rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"
                />
            </div>
            <div>
                <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    required
                    className="p-2 flex-1 text-lg border-2 border-gray-400 rounded-md bg-[#333] text-gray-400 my-3 mr-2.5 w-[450px] h-13 focus:outline-none focus:ring-2 focus:ring-white"
                >
                {categoryOptions.map((option) => (
                    <option key={option} value={option}>
                        {option}
                    </option>
                ))}
                </select>
            </div>
            <div>
                <IngredientInput
                    onAdd={handleAddIngredient}

                />

                <ul className="mt-2 list-disc list-inside space-y-1">
                    {ingredients
                        .filter(i => i.productName?.name)
                        .map((i, idx) => (
                        <li key={idx}
                            className="flex items-center justify-between bg-[#333] text-white px-3 py-1 rounded">
                            <span>
                                {i.productName.name} - {i.amount} {i.unit}
                            </span>
                            <button
                                type="button"
                                onClick={() => handleRemoveIngredientAt(idx)}
                                className="p-0 m-0 bg-[#333] border-none ml-2 text-black-400 leading-none hover:text-red-600 text-sm font-bold"
                            >
                                <HiX/>
                            </button>
                        </li>
                    ))}
                </ul>

            </div>
            <div>
                <textarea
                    placeholder="Instructions"
                    value={instructions}
                    onChange={(e) => setInstructions(e.target.value)}
                    required
                    className="p-3 flex-1 border-2 border-gray-400 text-lg text-gray-400 bg-[#333] w-[450px] mr-2.5 my-3 rounded-md h-32 resize-y focus:outline-none focus:ring-2 focus:ring-white"
                />
            </div>
            <div>
                <input
                    type="number"
                    placeholder="Number of Servings"
                    value={numberOfServings}
                    onChange={(e) => setNumberOfServings(e.target.value)}
                    required
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-whiteborder-gray-400 rounded text-gray-400 w-[450px] focus:outline-none focus:ring-2 focus:ring-white"
                />
            </div>
            <div>
                <input
                    type="text"
                    placeholder="Tags (comma separated)"
                    value={tags}
                    onChange={(e) => setTags(e.target.value)}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-whiteborder-gray-400 rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"
                />
            </div>
            <div>
                <button type="submit" className="mt-4 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Create Recipe
                </button>
            </div>
            {message && <p className={messageClass}>{message.text}</p>}
        </form>
    );
};

export default RecipeCreateForm;
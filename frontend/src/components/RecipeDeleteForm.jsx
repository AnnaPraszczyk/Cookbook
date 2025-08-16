import React, { useEffect, useState } from "react";
import {useNavigate, useParams, useLocation, Link} from "react-router-dom";
import axios from "axios";

const categoryOptions = [
    "Appetizer", "Soup", "Main Course", "Sauce", "Salad",
    "Pasta", "Snack", "Beverage", "Dessert", "Cake", "Pie", "Bakery"
];

const RecipeDeleteForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const [recipe, setRecipe] = useState(null);
    const [message, setMessage] = useState({ text: "", type: "" });

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const response = await axios.get(`/api/recipes/${id}`);
                setRecipe(response.data);
            } catch (error) {
                setMessage({ text: "Failed to load recipe.", type: "error" });
            }
        };
        fetchRecipe().catch((err) => {
            console.error("Unhandled fetchRecipe error:", err);
        });
    }, [id]);

    const handleDelete = async (e) => {
        e.preventDefault();
        const confirmed = window.confirm("Are you sure you want to delete this recipe?");
        if (!confirmed) return;
        try {
            await axios.delete(`/api/recipes/${id}`, {
                data: { recipeId: id, recipeName: recipe.name },
                headers: { "Content-Type": "application/json" },
            });
            setMessage({ text: "✅ Recipe deleted successfully!", type: "success" });
            setTimeout(() => navigate(`/recipes${location.search}`), 3000);
        } catch (error) {
            let msg = "An unexpected error occurred.";
            if (error.response?.status === 400) {
                msg = "❌ This recipe cannot be deleted because it is part of a shopping list.";
            } else if (error.response?.data?.message) {
                msg = `❌ ${error.response.data.message}`;
            } else {
                msg = `❌ ${error.message}`;
            }
            setMessage({ text: msg, type: "error" });
        }
    };

    if (!recipe) {
        return (
            <div className="text-white text-center mt-10">
                {message.text ? (
                    <p className={message.type === "error" ? "text-red-500" : "text-green-500"}>
                        {message.text}
                    </p>
                ) : (
                    <p>Loading recipe details...</p>
                )}
            </div>
        );
    }

    return (
        <form onSubmit={handleDelete} className="flex flex-col max-w-xl mx-auto space-y-6 text-white sm:px-0 mt-4 sm:mt-6">
            <div>
                <input
                    type="text"
                    value={recipe.name}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#292F33] text-gray-400 rounded w-full max-w-112"
                />
            </div>

            <div>
                <select
                    value={recipe.category}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#292F33] text-gray-400 rounded w-full max-w-112"
                >
                    {categoryOptions.map((option) => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
            </div>

            <div>
                <ul className="mt-2 list-disc list-inside space-y-1">
                    {recipe.ingredients?.map((ing, idx) => (
                        <li
                            key={idx}
                            className="flex items-center justify-between bg-[#292F33] text-white px-3 py-1 rounded"
                        >
        <span>
          {ing.productName} - {ing.amount} {ing.unit}
        </span>
                        </li>
                    ))}
                </ul>
            </div>

            <div>
        <textarea
            value={recipe.instructions}
            disabled
            className="p-3 text-lg border-2 border-gray-400 text-gray-400 bg-[#292F33] w-full max-w-112 rounded-md h-32 resize-y"
        />
            </div>

            <div>
                <input
                    type="number"
                    value={recipe.numberOfServings}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#292F33] text-gray-400 rounded w-full max-w-112"
                />
            </div>

            <div>
                <input
                    type="text"
                    value={(recipe.tags || []).join(", ")}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#292F33] text-gray-400 rounded w-full max-w-112"
                />
            </div>

            <div>
                <button type="submit" className="mt-4 text-lg px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition-colors duration-200">
                    Confirm Delete
                </button>
            </div>

            {message.text && (
                <p className={message.type === "success" ? "text-green-400" : "text-red-500"}>
                    {message.text}
                </p>
            )}
            <Link to={`/recipes${location.search}`} className="text-[#c0a060] hover:underline block mt-6 sm:text-base">
                ← Back to recipe list
            </Link>
        </form>
    );
};

export default RecipeDeleteForm;
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

const categoryOptions = [
    "Appetizer", "Soup", "Main Course", "Sauce", "Salad",
    "Pasta", "Snack", "Beverage", "Dessert", "Cake", "Pie", "Bakery"
];

const RecipeDeleteForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [recipe, setRecipe] = useState(null);
    const [message, setMessage] = useState({ text: "", type: "" });

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const response = await fetch(`/api/recipes/${id}`);
                if (!response.ok) throw new Error("Failed to load recipe.");
                const data = await response.json();
                setRecipe(data);
            } catch (error) {
                setMessage({ text: error.message, type: "error" });
            }
        };
        fetchRecipe();
    }, [id]);

    const handleDelete = async (e) => {
        e.preventDefault();
        const confirmed = window.confirm("Are you sure you want to delete this recipe?");
        if (!confirmed) return;
        try {
            const res = await fetch(`/api/recipes/${id}`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ recipeId: id, recipeName: recipe.name })
            });
            if (!res.ok) throw new Error(`Server responded with ${res.status}`);
            setMessage({ text: "✅ Recipe deleted successfully!", type: "success" });
            setTimeout(() => navigate("/recipes"), 1500);
        } catch (error) {
            setMessage({ text: `❌ ${error.message}`, type: "error" });
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
        <form onSubmit={handleDelete} className="flex flex-col max-w-xl mx-auto space-y-6 text-white">
            <div>
                <input
                    type="text"
                    value={recipe.name}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px]"
                />
            </div>

            <div>
                <select
                    value={recipe.category}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px]"
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
                            className="flex items-center justify-between bg-[#333] text-white px-3 py-1 rounded"
                        >
        <span>
          {ing.product?.productName?.name || ing.productName?.name} - {ing.amount} {ing.unit}
        </span>
                        </li>
                    ))}
                </ul>
            </div>

            <div>
        <textarea
            value={recipe.instructions}
            disabled
            className="p-3 text-lg border-2 border-gray-400 text-gray-400 bg-[#333] w-[450px] rounded-md h-32 resize-y"
        />
            </div>

            <div>
                <input
                    type="number"
                    value={recipe.numberOfServings}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px]"
                />
            </div>

            <div>
                <input
                    type="text"
                    value={(recipe.tags || []).join(", ")}
                    disabled
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px]"
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
        </form>
    );
};

export default RecipeDeleteForm;
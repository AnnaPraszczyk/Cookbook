import React, { useState, useEffect } from "react";
import {useParams, useLocation, Link} from "react-router-dom";
import IngredientInput from "./IngredientInput.jsx";

const categoryOptions = [
    "Appetizer", "Soup", "Main Course", "Sauce", "Salad",
    "Pasta", "Snack", "Beverage", "Dessert", "Cake", "Pie", "Bakery"
];

const RecipeUpdateForm = () => {
    const { id } = useParams();
    const [name, setName] = useState("");
    const [category, setCategory] = useState(categoryOptions[0]);
    const [ingredients, setIngredients] = useState([]);
    const [instructions, setInstructions] = useState("");
    const [numberOfServings, setNumberOfServings] = useState("");
    const [tags, setTags] = useState("");
    const [message, setMessage] = useState({ text: "", type: "" });
    const [productOptions, setProductOptions] = useState([]);
    const [autoCalculate, setAutoCalculate] = useState(false);
    const location = useLocation();

    useEffect(() => {
        fetch("http://localhost:8080/products")
            .then(res => res.json())
            .then(data => {
                console.log("✅ Products:", data);
                setProductOptions(data.map(p => p.productName));
            })
            .catch(err => console.error("❌ Failed to load products", err));
    }, []);

    useEffect(() => {
        if (!id) return;

        const fetchRecipe = async () => {
            try {
                const response = await fetch(`/api/recipes/${id}`);
                if (!response.ok) throw new Error("Recipe not found");

                const data = await response.json();

                setName(data.name || "");
                setCategory(data.category || categoryOptions[0]);
                setIngredients(data.ingredients || []);
                setInstructions(data.instructions || "");
                setNumberOfServings(data.numberOfServings?.toString() || "");
                setTags((data.tags || []).join(", "));
            } catch (error) {
                setMessage({ text: `Error fetching recipe: ${error.message}`, type: "error" });
            }
        };

        fetchRecipe();
    }, [id]);

    const handleAddIngredient = (ingredient) => {
        setIngredients((prev) => [...prev, ingredient]);
    };

    const handleRemoveIngredientAt = (indexToRemove) => {
        setIngredients((prev) =>
            prev.filter((_, index) => index !== indexToRemove)
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const tagsArray = tags.split(",").map((item) => item.trim()).filter((item) => item);

        const requestData = {
            name,
            category,
            ingredients: ingredients.map(i => ({
                productName: i.product?.productName?.name || i.productName?.name,
                amount: i.amount,
                unit: i.unit
            })),
            instructions,
            numberOfServings: autoCalculate ? 0 : parseInt(numberOfServings, 10),
            tags: tagsArray,
        };

        try {
            const response = await fetch(`/api/recipes/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestData),
            });
            const data = await response.json();
            setMessage({ text: "✅ Recipe updated!", type: "success" });
        } catch (error) {
            setMessage({text: "❌ Error updating recipe", type: error.message});
        }
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col max-w-xl mx-auto space-y-6 mt-6">
            <div>
                <input
                    type="text"
                    placeholder="Recipe Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px] focus:outline-none focus:ring-2 focus:ring-white"
                />
            </div>

            <div>
                <select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    required
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px] h-13 focus:outline-none focus:ring-2 focus:ring-white"
                >
                    {categoryOptions.map((option) => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
            </div>

            <div>
                <IngredientInput onAdd={handleAddIngredient} productOptions={productOptions} />

                <ul className="mt-2 list-disc list-inside space-y-1">
                    {ingredients.map((ing, idx) => (
                        <li
                            key={idx}
                            className="flex items-center justify-between bg-[#333] text-white px-3 py-1 rounded"
                        >
      <span>
        {ing.productName} - {ing.amount} {ing.unit}
      </span>
                            <button
                                type="button"
                                onClick={() => handleRemoveIngredientAt(idx)}
                                className="text-red-500 hover:text-red-700 ml-4"
                            >
                                ×
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
            className="p-3 border-2 border-gray-400 text-lg text-gray-400 bg-[#333] w-[450px] rounded-md h-32 resize-y focus:outline-none focus:ring-2 focus:ring-white"
        />
            </div>

            <div>
                <div className="flex items-center gap-2 mb-4 text-gray-300 ">
                    <input
                        type="checkbox"
                        id="autoCalculate"
                        checked={autoCalculate}
                        onChange={e => setAutoCalculate(e.target.checked)}
                        className="w-4 h-4"
                    />
                    <label htmlFor="autoCalculate" className="text-gray-300 text-sm">
                        Automatically calculate servings from ingredients
                    </label>
                </div>
                {!autoCalculate && (
                    <input
                        type="number"
                        min="1"
                        placeholder="Number of Servings"
                        value={numberOfServings}
                        onChange={(e) => setNumberOfServings(e.target.value)}
                        required
                        className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px] focus:outline-none focus:ring-2 focus:ring-white"
                    />
                )}
            </div>

            <div>
                <input
                    type="text"
                    placeholder="Tags (comma separated)"
                    value={tags}
                    onChange={(e) => setTags(e.target.value)}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333] text-gray-400 rounded w-[450px] focus:outline-none focus:ring-2 focus:ring-white"
                />
            </div>

            <div>
                <button
                    type="submit"
                    className="mt-4 text-lg px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                >
                    Update Recipe
                </button>
            </div>
            {message.text && (
                <p className={message.type === "success" ? "text-green-600" : "text-red-600"}>
                    {message.text}
                </p>
            )}
            <Link to={`/recipes${location.search}`} className="text-[#c0a060] hover:underline block mt-6">
                ← Back to recipe list
            </Link>
        </form>
    );
};
export default RecipeUpdateForm;
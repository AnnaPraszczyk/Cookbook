import React, { useEffect, useState } from "react";
import { useParams, Link, useLocation } from "react-router-dom";

const RecipeDetailsPage = () => {
    const { id } = useParams();
    const [recipe, setRecipe] = useState(null);
    const [loading, setLoading] = useState(true);
    const [newServings, setNewServings] = useState("");
    const [scaledRecipe, setScaledRecipe] = useState(null);
    const location = useLocation();


    const handleScale = async () => {
        if (!newServings || isNaN(newServings) || newServings <= 0) return;

        try {
            const res = await fetch("/api/recipes/scaling", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    recipeId: recipe.id,
                    servings: parseInt(newServings, 10),
                }),
            });
            if (!res.ok) throw new Error("Failed to scale recipe");
            const data = await res.json();
            const adjustedIngredients = data.ingredients.map((scaledIng, i) => ({
                ...recipe.ingredients[i],
                amount: scaledIng.amount}));
            setRecipe({
                ...recipe,
                numberOfServings: data.numberOfServings || data.servings,
                ingredients: data.ingredients.map((scaledIng, i) => ({
                    ...recipe.ingredients[i],
                    amount: scaledIng.amount
                }))
            });

            setNewServings("");
        } catch (e) {
            console.error("Scaling failed:", e);
        }
    };

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const res = await fetch(`/api/recipes/${id}`);
                if (!res.ok) throw new Error("Failed to fetch recipe");
                const data = await res.json();
                setRecipe(data);
            } catch (e) {
                console.error("Error loading recipe:", e);
            } finally {
                setLoading(false);
            }
        };

        fetchRecipe();
    }, [id]);

    if (loading) return <p className="text-center text-gray-400">Loading...</p>;
    if (!recipe) return <p className="text-center text-white">Recipe not found.</p>;

    return (
        <div className="w-full-max-5xl mx-auto p-6 bg-[#333] text-gray-300 rounded shadow">
            <h1 className="text-3xl font-bold mb-4">{recipe.name}</h1>
            <p className="mb-2"><strong>Category:</strong> {recipe.category}</p>
            <p className="mb-2"><strong>Servings:</strong> {recipe.numberOfServings || recipe.servings || "-"}</p>
            {recipe.tags?.length > 0 && (
                <p className="mb-4"><strong>Tags:</strong> {recipe.tags.join(", ")}</p>
            )}

            <h2 className="text-2xl font-semibold mt-6 mb-2">Ingredients</h2>
            <ul className="list-disc list-inside space-y-1">
                {recipe.ingredients.map((i, index) => (
                    <li key={index}>
                        {i.product?.productName?.name} — {i.amount} {i.unit}

                    </li>
                ))}
            </ul>

            <h2 className="text-2xl font-semibold mt-6 mb-2">Instructions</h2>
            <p className="whitespace-pre-line">{recipe.instructions}</p>

            <div className="mt-6 space-y-2">
                <label className="text-lg font-semibold">Scale to:</label>
                <div className="flex gap-4 items-center">
                    <input
                        type="number"
                        min="1"
                        value={newServings}
                        onChange={(e) => setNewServings(e.target.value)}
                        placeholder="New number of servings"
                        className="p-2 w-56 border-2 rounded bg-[#333] text-gray-300 border-gray-500"
                    />
                    <button
                        onClick={handleScale}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-gray-600 transition duration-200">
                        Scale
                    </button>
                </div>
            </div>
            <div className="mt-6">
                <Link to={`/recipes${location.search}`} className="text-[#c0a060] hover:underline">
                    ← Back to recipe list
                </Link>
            </div>
        </div>
    );
};

export default RecipeDetailsPage;
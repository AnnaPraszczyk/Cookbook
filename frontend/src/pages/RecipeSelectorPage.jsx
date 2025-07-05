import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getRecipesByQuery, addRecipeToList } from "../api/recipeListApi";
import RecipeScaleForm from "../components/RecipeScaleForm";

export default function RecipeSelectorPage() {
    const { listName } = useParams();
    const navigate = useNavigate();
    const [nameQuery, setNameQuery] = useState("");
    const [categoryQuery, setCategoryQuery] = useState("");
    const [recipes, setRecipes] = useState([]);
    const [selectedRecipe, setSelectedRecipe] = useState(null);

    const handleSearch = async () => {
        const results = await getRecipesByQuery(nameQuery, categoryQuery);
        setRecipes(results);
        setSelectedRecipe(null); // reset
    };

    const handleAddToList = async (id) => {
        await addRecipeToList(listName, id);
        alert("Recipe added to the list!");
    };

    useEffect(() => {
        console.log("URL param listName:", listName);
    }, [listName]);

    return (
        <div className="p-6 max-w-4xl mx-auto text-white space-y-6">
            <h1 className="text-3xl font-bold">Add Recipe to List: <span className="text-gray-400">{listName}</span></h1>

            <div className="space-y-4">
                <div className="flex flex-col gap-2">
                    <input
                        type="text"
                        value={nameQuery}
                        onChange={(e) => setNameQuery(e.target.value)}
                        placeholder="Search by name"
                        className="p-2 border bg-[#333] text-gray-200 rounded"
                    />
                    <select
                        value={categoryQuery}
                        onChange={(e) => setCategoryQuery(e.target.value)}
                        className="p-2 border bg-[#333] text-gray-300 rounded"
                    >
                        <option value="">Select category</option>
                        <option value="APPETIZER">Appetizer</option>
                        <option value="SOUP">Soup</option>
                        <option value="MAIN_COURSE">Main Course</option>
                        <option value="SAUCE">Sauce</option>
                        <option value="SALAD">Salad</option>
                        <option value="PASTA">Pasta</option>
                        <option value="SNACK">Snack</option>
                        <option value="BEVERAGE">Beverage</option>
                        <option value="DESSERT">Dessert</option>
                        <option value="CAKE">Cake</option>
                        <option value="PIE">Pie</option>
                        <option value="BAKERY">Bakery</option>
                    </select>
                    <button
                        onClick={handleSearch}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b18c47]"
                    >
                        Search
                    </button>
                </div>
            </div>

            {recipes.length > 0 && (
                <div className="space-y-4">
                    {recipes.map((r) => (
                        <div key={r.id} className="p-4 border rounded bg-[#333] shadow">
                            <div className="flex justify-between items-center">
                                <div>
                                    <h3 className="text-xl font-semibold">{r.name}</h3>
                                    <p className="text-gray-400">{r.category}</p>
                                </div>
                                <div className="flex gap-2">
                                    <button
                                        onClick={() => setSelectedRecipe(r)}
                                        className="bg-blue-600 px-4 py-1 rounded text-white hover:bg-blue-700"
                                    >
                                        Scale
                                    </button>
                                    <button
                                        onClick={() => handleAddToList(r.id)}
                                        className="bg-green-600 px-4 py-1 rounded text-white hover:bg-green-700"
                                    >
                                        Add to list
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {selectedRecipe && (
                <div className="mt-6 border-t border-gray-700 pt-4">
                    <h2 className="text-2xl font-bold mb-4">Scale: {selectedRecipe.name}</h2>
                    <RecipeScaleForm recipeId={selectedRecipe.id} />
                </div>
            )}
        </div>
    );
}
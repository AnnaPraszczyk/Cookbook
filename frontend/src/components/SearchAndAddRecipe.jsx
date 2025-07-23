import React, { useState } from "react";
import { searchRecipes, addRecipeToList } from "../api/recipeListApi";
import {Link} from "react-router-dom";

export default function SearchAndAddRecipe({ listName }) {
    const [searchTerm, setSearchTerm] = useState("");
    const [category, setCategory] = useState("");
    const [results, setResults] = useState([]);
    const [portions, setPortions] = useState(1);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");

    const searchByName = async () => {
        if (!searchTerm.trim()) return;
        try {
            const data = await searchRecipes({ name: searchTerm });
            setResults(data.content || []);
            setError(null);
            setSuccess("");
        } catch (err) {
            console.error(err);
            setError("Failed to search by name.");
        }
    };

    const searchByCategory = async () => {
        try {
            const data = await searchRecipes({ category });
            setResults(data.content || []);
            setError(null);
            setSuccess("");
        } catch (err) {
            console.error(err);
            setError("Failed to search by category.");
        }
    };

    const handleAdd = async (recipeId) => {
        try {
            await addRecipeToList({ listName, recipeId, portions });
            setSuccess("Recipe added to list!");
        } catch (err) {
            console.error(err);
            setError("Failed to add recipe.");
        }
    };

    return (
        <div className="p-6 bg-[#2c2c2c] text-white rounded-lg space-y-6">
            <h2 className="text-xl font-bold">Search & Add Recipe</h2>

            <div className="space-y-2">
                <label className="text-lg font-semibold">Search by name</label>
                <div className="flex gap-4 flex-wrap">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Type recipe name"
                        className="p-2 w-[300px] bg-[#333] border border-gray-400 rounded"
                    />
                    <button
                        onClick={searchByName}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                    >
                        Search
                    </button>
                </div>
            </div>

            <div className="space-y-2">
                <label className="text-lg font-semibold">Search by category</label>
                <div className="flex gap-4 flex-wrap">
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="p-2 w-[300px] bg-[#333] border border-gray-400 rounded text-gray-300">
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
                    onClick={searchByCategory}
                    className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                >
                    Search
                </button>
                </div>
            </div>


            {results.length > 0 && (
                <table className="w-full table-fixed border border-gray-500 text-left text-white">
                    <thead className="bg-[#333] text-gray-300">
                    <tr>
                        <th className="px-4 py-2 border">Name</th>
                        <th className="px-4 py-2 border">Category</th>
                        <th className="px-4 py-2 border w-[200px] text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {results.map((r) => (
                        <tr key={r.id} className="hover:bg-[#444] border-t">
                            <td className="px-4 py-2 border">{r.name}</td>
                            <td className="px-4 py-2 border">{r.category}</td>
                            <td className="px-4 py-2 border text-center">
                                <input
                                    type="number"
                                    min="1"
                                    value={portions}
                                    onChange={(e) => setPortions(parseInt(e.target.value))}
                                    className="w-[60px] p-1 bg-[#222] border border-gray-500 rounded mr-2"
                                />
                                <Link to={`/recipes/${r.id}?listName=${listName}`} className="text-[#c0a060] hover:underline">
                                    View details
                                </Link>
                                <button
                                    onClick={() => handleAdd(r.id)}
                                    className="px-3 py-1 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]"
                                >
                                    Add
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            {error && <p className="text-red-400">{error}</p>}
            {success && <p className="text-green-400">{success}</p>}
        </div>
    );
}
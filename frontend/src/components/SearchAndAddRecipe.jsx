import React, { useState } from "react";
import { searchRecipes, addRecipeToList } from "../api/recipeListApi";
import {Link, useNavigate} from "react-router-dom";

export default function SearchAndAddRecipe({ listName, defaultPortions }) {
    const [searchTerm, setSearchTerm] = useState("");
    const [category, setCategory] = useState("");
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");
    const [searchInitiated, setSearchInitiated] = useState(false);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const navigate = useNavigate();


    const searchByName = async () => {
        if (!searchTerm.trim()) return;
        setLoading(true);
        setSearchInitiated(true);
        try {
            const data = await searchRecipes({ name: searchTerm, page });
            setResults(data.content || []);
            setTotalPages(data.totalPages || 1);
            setError(null);
            setSuccess("");
        } catch (err) {
            console.error(err);
            setError("Failed to search by name.");
        }finally {
            setLoading(false);
        }
    };

    const searchByCategory = async () => {
        setLoading(true);
        setSearchInitiated(true);
        try {
            const data = await searchRecipes({category, page});
            setResults(data.content || []);
            setTotalPages(data.totalPages || 1);
            setError(null);
            setSuccess("");
        } catch (err) {
            console.error(err);
            setError("Failed to search by category.");
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async (recipe) => {
        const portions = recipe.defaultPortions || defaultPortions || 1;
        try {
            await addRecipeToList({ listName, recipeId: recipe.id, portions });
            setSuccess(`Recipe "${recipe.name}" added to list!`);
            setTimeout(() => {
                navigate(`/lists/${listName}/view`, {
                    state:{
                        message: {
                            text: `Recipe "${recipe.name}" added to list!`,
                            type: "success"
                        },
                        refresh: true
                    }
                });
            }, 1000);
        } catch (err) {
            console.error(err);
            setError("Failed to add recipe.");
        }
    };

    const goToNextPage = () => {
        if (page + 1 >= totalPages) return;
        setPage(prev => {
            const next = prev + 1;
            category ? searchByCategory() : searchByName();
            return next;
        });
    };

    const goToPreviousPage = () => {
        if (page === 0) return;
        setPage(prev => {
            const prevPage = prev - 1;
            category ? searchByCategory() : searchByName();
            return prevPage;
        });
    };

    return (
        <div className="p-6 text-white rounded-lg space-y-6">
            <h2 className="text-xl font-bold">Search & Add Recipe</h2>

            <div className="space-y-2">
                <div className="flex gap-4 flex-wrap">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Recipe name"
                        className="p-2 w-80 bg-[#333] border border-gray-400 rounded"
                    />
                    <button
                        onClick={searchByName}
                        className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]">
                        Search
                    </button>
                </div>
            </div>

            <div className="space-y-2">
                <div className="flex gap-4 flex-wrap">
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="p-2 w-80 bg-[#333] border border-gray-400 rounded text-gray-300">
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
                    className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d]">
                    Search
                </button>
                </div>
            </div>

            {searchInitiated && !loading && results.length === 0 && (
                <p className="text-gray-400 italic">No recipes found.</p>
            )}

            {searchInitiated && !loading && results.length > 0 && (
                <>
                    <table className="table-fixed w-full shadow rounded overflow-hidden bg-[#333] text-white border-2 border-gray-400">
                        <thead className="bg-[#222] text-gray-300">
                        <tr className="text-left text-gray-400 bg-[#333]">
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Name</th>
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Category</th>
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {results.map((r) => (
                            <tr
                                key={r.id}
                                className="border-t cursor-pointer bg-[#333] border-2 border-gray-400 hover:bg-[#444]">
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">{r.name}</td>
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">{r.category}</td>
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">
                                    <div className="flex gap-2 justify-center items-center flex-wrap">
                                        <Link
                                            to={`/recipes/${r.id}`}
                                                state={{
                                                    listName,
                                                    defaultPortions: r.defaultPortions || defaultPortions || 1,
                                                    fromSearch: { searchTerm, category, page },
                                            }}
                                            className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm"
                                        >
                                            View
                                        </Link>
                                        <button
                                            onClick={() => handleAdd(r)}
                                            className="px-2 py-1 bg-[#c0a060] text-white rounded hover:bg-[#b8944d] text-sm">
                                            Add
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <div className="flex justify-center items-center gap-4 pt-4">
                        <button
                            onClick={goToPreviousPage}
                            disabled={page === 0}
                            className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700">
                            Previous
                        </button>
                        <span className="text-white text-lg">
                Page {page + 1} / {totalPages}
            </span>
                        <button
                            onClick={goToNextPage}
                            disabled={page + 1 >= totalPages}
                            className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700">
                            Next
                        </button>
                    </div>
                </>
            )}
            {error && <p className="text-red-400 pt-4">{error}</p>}
            {success && <p className="text-green-400 pt-4">{success}</p>}
        </div>
    );
}
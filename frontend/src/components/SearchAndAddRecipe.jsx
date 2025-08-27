import React, { useState, useEffect } from "react";
import { searchRecipes, addRecipeToList } from "../api/recipeListApi";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";

export default function SearchAndAddRecipe({ listName, defaultPortions }) {
    const [searchTerm, setSearchTerm] = useState("");
    const [category, setCategory] = useState("");
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState("");
    const [searchInitiated, setSearchInitiated] = useState(false);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const navigate = useNavigate();
    const [listPortions, setListPortions] = useState(defaultPortions || 1);

    useEffect(() => {
        if (defaultPortions && defaultPortions > 0) {
            setListPortions(defaultPortions);
        }
        const fetchListDetails = async () => {
            try {
                const { data } = await axios.get(`/api/lists/${listName}`);
                if (data.expectedPortions && data.expectedPortions > 0) {
                    setListPortions(data.expectedPortions);}
            } catch (err) {
                console.error("Failed to fetch list details:", err);
            }
        };
        fetchListDetails().catch(e => console.error(e));
    }, [listName, defaultPortions]);

    const fetchRecipesUnified = async (currentPage = 0) => {
        if (!searchTerm.trim() && !category) {
            setError("Please provide a name or select a category.");
            return;
        }
        setLoading(true);
        setSearchInitiated(true);
        try {
            const params = { page: currentPage };
            if (searchTerm.trim()) params.recipeName = searchTerm.trim();
            if (category) params.category = category;

            const data = await searchRecipes(params);
            setResults(data.content || []);
            setTotalPages(Number.isInteger(data.totalPages) ? data.totalPages : 0);
            setError(null);
            setSuccess("");
        } catch (err) {
            console.error(err);
            setError("Failed to search recipes.");
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = async (recipe) => {
        const portions = listPortions || 1;
        try {
            await addRecipeToList({ listName, recipeId: recipe.recipeId, portions });
            setSuccess(`Recipe "${recipe.recipeName}" added to list with ${portions} portions!`);
            setTimeout(() => {
                navigate(`/lists/${listName}/view`, {
                    state:{
                        message: {
                            text: `Recipe "${recipe.recipeName}" added to list!`,
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
        const next = page + 1;
        setPage(next);
        fetchRecipesUnified(next);
    };

    const goToPreviousPage = () => {
        if (page === 0) return;
        const prevPage = page - 1;
        setPage(prevPage);
        fetchRecipesUnified(prevPage);
    };

    return (
        <div className="p-6 text-white rounded-lg space-y-6">
            <h2 className="text-xl font-bold">Search & Add Recipe</h2>
            <form className="flex gap-4 items-end">
                <div className="flex flex-col">
                    <label className="text-[#c0a060] mb-2 text-lg">Recipe Name</label>
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="Recipe name"
                        className="p-2 w-112 bg-[#292F33] border border-gray-400 rounded"/>
                </div>
                <div className="flex flex-col">
                    <label className="text-[#c0a060] mb-2 text-lg">Category (optional)</label>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="p-2 bg-[#292F33] border border-gray-400 rounded text-gray-300 w-64">
                        <option value="">All categories</option>
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
                </div>
                <button
                    type="button"
                    onClick={() => { setPage(0); fetchRecipesUnified(0); }}
                    className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d] self-end">
                    Search
                </button>
            </form>
            {searchInitiated && !loading && results.length === 0 && (
                <p className="text-gray-400 italic">No recipes found.</p>
            )}
            {searchInitiated && !loading && results.length > 0 && (
                <>
                    <table className="table-fixed w-full shadow rounded overflow-hidden bg-[#292F33] text-white border-2 border-gray-400">
                        <thead className="bg-[#222] text-gray-300">
                        <tr className="text-left text-gray-400 bg-[#292F33]">
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Name</th>
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Category</th>
                            <th className="px-4 py-2 text-center border-2 border-gray-400">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {results.map((r) => (
                            <tr
                                key={r.recipeId}
                                className="border-t cursor-pointer bg-[#292F33] border-2 border-gray-400 hover:bg-[#444]">
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">{r.recipeName}</td>
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">{r.category}</td>
                                <td className="px-4 py-2 border-2 border-gray-400 text-center">
                                    <div className="flex gap-2 justify-center items-center flex-wrap">
                                        <Link
                                            to={`/recipes/${r.recipeId}`}
                                            className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">
                                            View
                                        </Link>
                                        <button
                                            onClick={() => handleAdd(r)}
                                            className="px-4 py-2 bg-[#c0a060] text-white rounded hover:bg-[#b8944d] mb-4 text-sm self-end">
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
                            {`Page ${page + 1} / ${totalPages}`}
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
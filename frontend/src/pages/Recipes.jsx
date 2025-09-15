import React, { useState, useEffect } from "react";
import {Link, useLocation, useSearchParams} from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Recipes = () => {
    const [nameQuery, setNameQuery] = useState("");
    const [recipes, setRecipes]   = useState([]);
    const [page, setPage]         = useState(0);
    const [totalPages, setTotalPages]  = useState(0);
    const [loading, setLoading]   = useState(false);
    const [searchInitiated, setSearchInitiated] = useState(false);
    const [categoryQuery, setCategoryQuery] = useState("");
    const [selectedRecipeId] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();
    const params = new URLSearchParams( location.search);
    const activeCategory = params.get("category");
    const activeName = params.get("name");
    const showCategoryViewOnly = !!activeCategory && !activeName;
    const showFullPanel = !activeCategory && !activeName;
    const [searchParams] = useSearchParams();
    const searchTerm = searchParams.get("search") || "";
    const recipeNameParam = searchParams.get("recipeName") || "";
    const categoryParam = searchParams.get("category") || "";

    const handleSearch = () => {
        const params = new URLSearchParams();
        if (nameQuery.trim()) params.append("recipeName", nameQuery.trim());
        if (categoryQuery.trim()) params.append("category", categoryQuery.trim());
        params.append("page", "0");
        navigate(`/recipes?${params.toString()}`);
    };

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const recipeName = params.get("recipeName");
        const category = params.get("category");
        const pageParam = parseInt(params.get("page") || "0");
        setPage(pageParam);
        if (recipeName || category) {
            setSearchInitiated(true);
            if (recipeName && category) {
                fetchRecipes({ recipeName, category }, pageParam);
            } else if (recipeName) {
                fetchRecipes({ recipeName }, pageParam);
            } else if (category) {
                fetchRecipes({ category }, pageParam);
            }
        } else {
            setSearchInitiated(false);
            setRecipes([]);
        }
    }, [location.search]);

    const fetchRecipes = async (filters, currentPage= 0) => {
        setLoading(true);
        try {
            const { data }  = await axios.get("/api/recipes/search", {params: {
                    page: currentPage,
                    size: 10,
                    ...filters },});
            setRecipes(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (e) {
            console.error("Search failed:", e);
            setRecipes([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto">
            {showCategoryViewOnly ? (
                <h1 className="text-3xl font-bold mt-6">
                    Category: <span className="text-[#c0a060]">{activeCategory}</span>
                </h1>
            ) : (
                <h1 className="text-3xl font-bold mt-6">Recipes Management</h1>
            )}
            {showFullPanel && (
                <>
                <div className="flex justify-between items-end mb-6 flex-wrap gap-y-4">
                <Link
                    to="/recipes/create"
                    className="px-4 py-2 text-lg rounded transition-colors duration-200 hover:bg-[#ad9854]">
                    Add Recipe
                </Link>
            </div>
            <form
                className="flex flex-wrap gap-4 items-end">
                <div className="flex flex-col">
                    <label className="text-[#c0a060] mb-2 text-lg">Recipe Name</label>
                    <input
                    type="text"
                    value={nameQuery}
                    onChange={e => setNameQuery(e.target.value)}
                    placeholder={"Search by name"}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#292F33] rounded text-gray-400 focus:outline-none focus:ring-2 w-112 focus:ring-white"/>
                </div>
                <div className="flex flex-col">
                    <label className="text-[#c0a060] mb-2 text-lg">Category (optional)</label>
                    <select
                        value={categoryQuery}
                        onChange={e => setCategoryQuery(e.target.value)}
                        className="p-2 text-lg bg-[#292F33]  text-gray-500 border-2 border-gray-400 rounded w-64">
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
                    onClick={() => {
                        const params = new URLSearchParams();
                        if (nameQuery.trim()) params.append("recipeName", nameQuery.trim());
                        if (categoryQuery && categoryQuery!=="") params.append("category", categoryQuery);
                        params.append("page", "0");
                        navigate(`/recipes?${params.toString()}`);
                    }}
                    className="mt-12 text-lg px-4 py-2 bg-[#c0a060] mb-4 text-white rounded hover:bg-gray-600 transition-colors duration-200 self-end">
                    Search
                </button>
            </form>
                </>
            )}
            {loading && <p>Loading…</p>}

            {searchInitiated && !loading && recipes.length === 0 && (
                <p>No recipes found.</p>
            )}
            {searchInitiated && !loading && recipes.length > 0 && (

                <table className="table-fixed w-full shadow rounded overflow-hidden bg-[#292F33] text-white border-2 border-gray-400">
                    <thead className="bg-[#222] text-gray-300">
                    <tr className="text-left text-gray-400 bg-[#292F33]">
                        <th className="px-4 py-2 bg-[#292F33] text-white text-center border-2 border-gray-400">Name</th>
                        <th className="px-4 py-2 text-center bg-[#292F33] text-white border-2 border-gray-400">Category</th>
                        <th className="px-4 py-2 bg-[#292F33] text-white border-2 border-gray-400 text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {recipes.map(r => (
                        <tr key={r.recipeId} className="border-t cursor-pointer bg-[#292F33] text-white border-2 border-gray-400 hover:bg-[#444] hover:text-white">
                            <td className="px-4 py-2 bg-[#292F33] text-white border-2 border-gray-400">{r.recipeName}</td>
                            <td className="px-4 py-2 bg-[#292F33] text-white border-2 border-gray-400">{r.category}</td>
                            <td className="px-4 py-2 bg-[#292F33] text-white border-2 border-gray-400">
                                    <div className="flex gap-2">
                                        <Link to={`/recipes/${r.recipeId}?${recipeNameParam ? `recipeName=${recipeNameParam}` : `category=${categoryParam}`}`}
                                              className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">View</Link>
                                        <Link to={`/recipes/update/${r.recipeId}?${recipeNameParam ? `recipeName=${recipeNameParam}` : `category=${categoryParam}`}`}
                                              className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">Update</Link>
                                        <Link to={`/recipes/delete/${r.recipeId}?${recipeNameParam ? `recipeName=${recipeNameParam}` : `category=${categoryParam}`}`}
                                              className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">Delete</Link>
                                    </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            {selectedRecipeId && (
                <div className="flex gap-4 justify-center mt-6">
                    <Link
                        to={`/recipes/scale/${selectedRecipeId}`}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">
                        View
                    </Link>
                    <Link
                        to={`/recipes/update/${selectedRecipeId}`}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">
                        Update
                    </Link>
                    <Link
                        to={`/recipes/delete/${selectedRecipeId}`}
                        className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">
                        Delete
                    </Link>
                </div>
            )}
            {searchInitiated && recipes.length > 0 && (
                <div className="flex justify-center items-center gap-4 pt-4">
                <button
                    onClick={() => {
                        const params = new URLSearchParams();
                        params.append("page", String(page - 1));
                        if (recipeNameParam) params.append("recipeName", recipeNameParam);
                        if (categoryParam) params.append("category", categoryParam);
                        navigate(`/recipes?${params.toString()}`);
                    }}
                    disabled={page === 0}
                    className="mt-4 text-lg px-4 py-2 bg-gray-500 mb-4 w-[100px] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Previous
                </button>
                <span>
          Page {page + 1} / {totalPages}
        </span>
                <button
                    onClick={() => {
                        const params = new URLSearchParams();
                        params.append("page", String(page + 1));
                        if (recipeNameParam) params.append("recipeName", recipeNameParam);
                        if (categoryParam) params.append("category", categoryParam);
                        navigate(`/recipes?${params.toString()}`);
                    }}
                    disabled={page + 1 >= totalPages}
                    className="mt-4 text-lg px-4 py-2 bg-gray-500 mb-4 w-[100px] text-white rounded hover:bg-gray-600 transition-colors duration-200"
                >
                    Next
                </button>
            </div>
            )}
        </div>
    );
};

export default Recipes;

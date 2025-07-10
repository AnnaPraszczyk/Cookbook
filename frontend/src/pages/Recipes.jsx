import React, { useState, useEffect } from "react";
import {Link, useLocation} from "react-router-dom";
import { useNavigate } from "react-router-dom";

const Recipes = () => {
    const [nameQuery, setNameQuery] = useState("");
    const [recipes, setRecipes]   = useState([]);
    const [page, setPage]         = useState(0);
    const [size] = useState(10);
    const [totalPages, setTotal]  = useState(0);
    const [loading, setLoading]   = useState(false);
    const [searchInitiated, setSearchInitiated] = useState(false);
    const [categoryQuery, setCategoryQuery] = useState("");
    const [selectedRecipeId, setSelectedRecipeId] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();
    const params = new URLSearchParams( location.search);
    const activeCategory = params.get("category");
    const activeName = params.get("name");
    const showCategoryViewOnly = !!activeCategory && !activeName;
    const showFullPanel = !activeCategory && !activeName;

    const fetchRecipes = async (query, type, currentPage= 0) => {
        setLoading(true);
        const params = new URLSearchParams({ page: currentPage, size: 10 });
        params.append(type, query.trim());

        try {
            const res  = await fetch(`/api/recipes/search?${params}`);
            if (!res.ok) throw new Error(`Server ${res.status}`);
            const data = await res.json();
            setRecipes(data.content || []);
            setTotal(data.totalPages || 0);
        } catch (e) {
            console.error("Search failed:", e);
            setRecipes([]);
        } finally {
            setLoading(false);
        }
    };
    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const category = params.get("category");
        const name = params.get("name");
        const pageParam = parseInt(params.get("page") || "0");
        setPage(pageParam);
        setSearchInitiated(true);
        if (category) {
            fetchRecipes(category, "category", pageParam);
        } else if (name) {
            fetchRecipes(name, "name", pageParam);
        } else {
            setRecipes([]);
        }
    }, [location.search]);

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto">
            {showCategoryViewOnly ? (
                <h1 className="text-3xl font-bold">
                    Category: <span className="text-[#c0a060]">{activeCategory}</span>
                </h1>
            ) : (
                <h1 className="text-3xl font-bold">Recipes Management</h1>
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
                className="flex flex-wrap gap-2 items-center">

                <div className="flex flex-col gap-4 items-start">
                    <label className="block text-[#c0a060] mb-2 text-lg">Search by Name</label>
                    <div className="flex gap-4">

                    <input
                    type="text"
                    value={nameQuery}
                    onChange={e => setNameQuery(e.target.value)}
                    placeholder={"Search by name"}
                    className="p-2 text-lg border-2 border-gray-400 bg-[#333]  rounded text-gray-400 focus:outline-none focus:ring-2 w-[450px] focus:ring-white"
                />
                <button
                    type="button"
                    onClick={() => {navigate(`/recipes/search?name=${nameQuery}&page=0`);}}
                        className="mt-4 text-lg px-4 py-2 bg-[#c0a060] mb-4 text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Search
                </button>
                        </div>
                        </div>
                        <div className="flex flex-col gap-4 items-start">
                            <label className="block text-[#c0a060] mb-2 mt-4 text-lg">Search by Category</label>
                            <div className="flex gap-4">
                                <select
                                    value={categoryQuery}
                                    onChange={e => setCategoryQuery(e.target.value)}
                                    className="p-2 text-lg bg-[#333]  text-gray-500 border-2 border-gray-400 rounded w-[450px]"
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
                                    type="button"
                                    onClick={() => {  navigate(`/recipes/search?category=${categoryQuery}&page=0`);}}
                                    className="mt-4 text-lg px-4 py-2 bg-[#c0a060] mb-4 text-white rounded hover:bg-gray-600 transition-colors duration-200">
                                    Search
                                </button>
                            </div>
                        </div>

            </form>
                </>
            )}
            {loading && <p>Loading…</p>}

            {searchInitiated && !loading && recipes.length === 0 && (
                <p>No recipes found.</p>
            )}
            {searchInitiated && !loading && recipes.length > 0 && (

                <table className="table-fixed w-full shadow rounded overflow-hidden bg-[#333] text-white border-2 border-gray-400">
                    <thead className="bg-[#222] text-gray-300">
                    <tr className="text-left text-gray-400 bg-[#333]">
                        <th className="px-4 py-2 bg-[#333] text-white text-center border-2 border-gray-400">Name</th>
                        <th className="px-4 py-2 text-center bg-[#333] text-white border-2 border-gray-400">Category</th>
                        <th className="px-4 py-2 bg-[#333] text-white border-2 border-gray-400 text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {recipes.map(r => (
                        <tr key={r.id} className="border-t cursor-pointer bg-[#333] text-white border-2 border-gray-400 hover:bg-[#444] hover:text-white">
                            <td className="px-4 py-2 bg-[#333] text-white border-2 border-gray-400">{r.name}</td>
                            <td className="px-4 py-2 bg-[#333] text-white border-2 border-gray-400">{r.category}</td>
                            <td className="px-4 py-2 bg-[#333] text-white border-2 border-gray-400">
                                    <div className="flex gap-2">
                                        <Link to={`/recipes/${r.id}`} className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">View</Link>
                                        <Link to={`/recipes/update/${r.id}`} className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">Update</Link>
                                        <Link to={`/recipes/delete/${r.id}`} className="px-2 py-1 text-white rounded hover:bg-gray-700 text-sm">Delete</Link>
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
                    onClick={() => navigate(`/recipes/search?${categoryQuery
                        ? `category=${categoryQuery}&page=${page - 1}`
                        : `name=${nameQuery}&page=${page - 1}`}`)
                    }
                    disabled={page === 0}
                    className="mt-4 text-lg px-4 py-2 bg-gray-500 mb-4 w-[100px] text-white rounded hover:bg-gray-600 transition-colors duration-200">
                    Previous
                </button>
                <span>
          Page {page + 1} / {totalPages}
        </span>
                <button
                    onClick={() => navigate(`/recipes/search?${categoryQuery
                        ? `category=${categoryQuery}&page=${page + 1}`
                        : `name=${nameQuery}&page=${page + 1}`}`)
                    }
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

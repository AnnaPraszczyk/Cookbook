import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";


const Recipes = () => {
            const [query, setQuery]       = useState("");
            const [recipes, setRecipes]   = useState([]);
            const [page, setPage]         = useState(0);
            const [size] = useState(10);
            const [totalPages, setTotal]  = useState(0);
            const [loading, setLoading]   = useState(false);
            const nav = useNavigate();


    const fetchRecipes = async (q= query, p= page) => {
        setLoading(true);
        const params = new URLSearchParams({ page: p, size, ...(q && { name: q }) });
        try {
            const res  = await fetch(`/api/recipes/search?${params}`);
            const body = await res.json();
            setRecipes(body.content);
            setTotal(body.totalPages);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchRecipes(); }, [page]);

    const onSearch = e => {
        e.preventDefault();
        setPage(0);
        fetchRecipes();
    };

    const onDelete = async id => {
        if (!window.confirm("Are you sure?")) return;
        await fetch(`/api/recipes/${id}`, { method: "DELETE" });
        fetchRecipes();
    };

    return (
        <div className="p-6 space-y-6 max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold">Recipes Management</h1>

            {/* Akcje globalne */}
            <div className="flex flex-wrap gap-4">
                <Link
                    to="/recipes/create"
                    className="px-4 py-2 rounded transition-colors duration-200 hover:bg-[#ad9854]"
                >
                    Add Recipe
                </Link>
            </div>

            <form
                onSubmit={onSearch}
                className="flex flex-wrap gap-2 items-center"
            >
                <input
                    type="text"
                    value={query}
                    onChange={e => setQuery(e.target.value)}
                    placeholder="Search by name, category or tags"
                    className="flex-1 border rounded px-3 py-2 focus:outline-none focus:ring"
                />
                <button
                    type="submit"
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    Search
                </button>
            </form>

            {loading ? (
                <p>Loading…</p>
            ) : recipes.length === 0 ? (
                <p>No recipes found.</p>
            ) : (
                <table className="w-full table-auto bg-white shadow rounded overflow-hidden">
                    <thead className="bg-gray-100">
                    <tr>
                        <th className="px-4 py-2 text-left">Name</th>
                        <th className="px-4 py-2 text-left">Category</th>
                        <th className="px-4 py-2">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {recipes.map(r => (
                        <tr key={r.recipeId} className="border-t">
                            <td className="px-4 py-2">{r.recipeName}</td>
                            <td className="px-4 py-2">{r.category}</td>
                            <td className="px-4 py-2 flex gap-2 justify-center">
                                <Link
                                    to={`/recipes/update/${r.recipeId}`}
                                    className="px-2 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500"
                                >
                                    Edit
                                </Link>
                                <button
                                    onClick={() => onDelete(r.recipeId)}
                                    className="px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}

            <div className="flex justify-center items-center gap-4 pt-4">
                <button
                    onClick={() => setPage(p => Math.max(p - 1, 0))}
                    disabled={page === 0}
                    className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                >
                    Previous
                </button>
                <span>
          Page {page + 1} / {totalPages}
        </span>
                <button
                    onClick={() => setPage(p => Math.min(p + 1, totalPages - 1))}
                    disabled={page + 1 >= totalPages}
                    className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
                >
                    Next
                </button>
            </div>
        </div>
    );
};

export default Recipes;
